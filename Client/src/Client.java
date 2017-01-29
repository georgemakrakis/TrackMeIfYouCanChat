
import java.io.FileInputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import net.i2p.I2PException;
import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.client.streaming.I2PSocketManagerFactory;
import net.i2p.util.I2PThread;

//icsd13107, icsd13008, icsd13022
public class Client
{   
    public static DefaultListModel model1 = new DefaultListModel();//List of users available
    public static String i2p_dest = "";//i2p_dest
    public static String nickname = "";//Local nickname
    public static boolean registered=false;//nickname registered in Registrar
    public static ArrayList<Message> new_msgs = new ArrayList();
    public static ArrayList<Message> users = new ArrayList();//keep the username and the i2p destination
    public static SSLContext createSSLContext()
    {
        try
        {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("A2KeyStore.jks"), "1234567890".toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "1234567890".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();

            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(km, tm, null);

            return sslContext;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
    
    //Static method to call every time we need to create an SSLSocket
    public static SSLSocket get_SSLSock()
    {
        SSLSocket sslSock=null;
        try
        {
            SSLContext sslContext = createSSLContext();
                        
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory(); 
            //SSLSocketFactory sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();//for testing purpose(have to remove the 2 previous commands - is not working
            // Create socket
            sslSock = (SSLSocket) sslSocketFactory.createSocket("localhost", 5999);

            System.out.println("SSL for client");    
                
            sslSock.setEnabledCipherSuites(sslSock.getSupportedCipherSuites());
            // Start handshake
            sslSock.startHandshake();

            // Get session after the connection is established
            SSLSession sslSession = sslSock.getSession();

            System.out.println("SSLSession :");
            System.out.println("\tProtocol : " + sslSession.getProtocol());
            System.out.println("\tCipher suite : " + sslSession.getCipherSuite());   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return sslSock;
    }
    public static void main(String[] args) throws I2PException, ConnectException, SocketTimeoutException
    {
        JOptionPane.showConfirmDialog(null,"Getting I2P destination address\nPlease wait...","I2P", JOptionPane.DEFAULT_OPTION);
        
        I2PSocketManager manager = I2PSocketManagerFactory.createManager();
        I2PServerSocket serverSocket = manager.getServerSocket();
        I2PSession session = manager.getSession();

        i2p_dest = session.getMyDestination().toBase64();//save the i2p destination 
     
        try
        {
            Nickname NN = new Nickname();//Nickname Frame for nickname unique validation
            NN.setVisible(true);
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Send HeartBeat");
                    new Heartbeat(nickname,i2p_dest);
                }
            }, 60, 60, TimeUnit.SECONDS);//60: start 60 seconds after Client starts. 60: run every 60 seconds
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        //Create socket to handle clients
        while (true) 
        {  
            I2PSocket sock = serverSocket.accept();
            I2PThread t = new I2PThread(new I2P_Receive(sock));
            t.setName("clienthandler1");
            t.setDaemon(false);
            t.start();
        }
    }
    
}
