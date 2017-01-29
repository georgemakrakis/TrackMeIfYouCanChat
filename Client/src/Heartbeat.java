
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.net.ssl.SSLSocket;


public class Heartbeat {
    private ArrayList<Message> ar1;
   
    public Heartbeat(String nickname,String i2p_dest)
    {
        try 
        {
            SSLSocket sslSocket=Client.get_SSLSock();
            
            //Socket s1 = new Socket("localhost", 5999);
            ObjectOutputStream out = new ObjectOutputStream(sslSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sslSocket.getInputStream());
            
            
            Message msg = new Message();
            msg.set_name(Client.nickname);
            msg.set_HB();
            out.writeObject(msg);//here goes i2p_dest for heartbeat
            out.flush();
            
            String check = (String)in.readObject();//Read string to know what to do next
            if (check.equals("ArrayList"))
            {
                ar1 = (ArrayList<Message>) in.readObject();
                for (int i=0;i<ar1.size();i++)//remove curent user from list
                {
                    if(ar1.get(i).get_i2p_dest().equals(i2p_dest))
                    {
                        ar1.remove(i);
                    }
                }
                System.out.println("Received HeartBeat");
                System.out.print("Users:");
                for (int i = 0; i < ar1.size(); i++) {
                    System.out.print(" " + ar1.get(i).get_nickname());
                }
                System.out.println();
                Client.model1.removeAllElements();//remove all users from model1
                for (int i = 0; i < ar1.size(); i++) {
                    Client.model1.addElement(ar1.get(i).get_nickname());//add all active users
                }
                Client.users=ar1;
            }
            else
            {
                Client.registered=false;//Set that client is not registered in Registrar
                new NewConnection(nickname, i2p_dest);
            }
            
            sslSocket.close();
        } catch (IOException | ClassNotFoundException ex) 
        {
            ex.printStackTrace();
        }
    }
}
