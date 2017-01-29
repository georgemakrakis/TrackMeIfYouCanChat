
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

//icsd13107, icsd13008, icsd13022
public class Registrar
{
    public static ArrayList <Message> ar1 =new ArrayList();
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
    public static void main(String[] args)
    {
        
        try
        {
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(new Runnable() 
            {
                @Override
                public void run() 
                {
                    Date d = new Date();//Get current date
                    for (int i = 0; i < ar1.size(); i++) 
                    {
                        Date tmp1 = ar1.get(i).get_d();//Get the date of the last heartbeat saved
                        long dif = d.getTime() - tmp1.getTime();//Calculate the difference
                        if (dif > 122000)//if the difference is bigger than 2 min and 2 sec remove user from arraylist
                        {   //for more readable presentation
                            System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                            System.out.println("\u001B[31m"+"----------USER--REMOVED----------"+"\u001B[0m");
                            System.out.println(ar1.get(i).get_nickname());//print the nickname of the removed user
                            System.out.println("\u001B[31m"+"----------USER--REMOVED----------"+"\u001B[0m");
                            System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                            ar1.remove(i);//remove user from arraylist
                        }
                    }
                    System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                    System.out.println("\u001B[32m"+"--------------USERS--------------"+"\u001B[0m");
                    for(int i=0; i<ar1.size();i++)
                    {   //print the active users
                        System.out.print(" "+ar1.get(i).get_nickname());
                    }
                    System.out.println();
                    System.out.println("\u001B[32m"+"--------------USERS--------------"+"\u001B[0m");
                    System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                }
            }, 0, 30, TimeUnit.SECONDS);//0: start when Registar starts. 30: run every 30 (seconds in this case)
            
            /* SSL Region */
            SSLContext sslContext = createSSLContext();
            try
            {
                // Create server socket factory
                SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

                // Create server socket
                SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(5999);

                System.out.println("SSL server started");
                while (true)
                {
                    SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

                    // Start the server thread
                    Thread t = new Thread(new ClientServiceThread(sslSocket));
                    t.setName("clienthandler1");
                    t.setDaemon(false);
                    t.start();
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            
            
            /*ServerSocket sock= new ServerSocket(5999);
            while (true)//new thread every time server accepts new connection
            {
                Socket sock2 = sock.accept();
                Thread t = new Thread(new ClientServiceThread(sock2));
                t.setName("clienthandler1");
                t.setDaemon(false);
                t.start();
            }*/
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
