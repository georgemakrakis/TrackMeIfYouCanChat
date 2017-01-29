
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;



public class NewConnection {
    private ArrayList<Message> ar1;
 
    public NewConnection(String nickname,String i2p_dest)
    {
        try 
        {
            // Create socket
            SSLSocket sslSocket=Client.get_SSLSock();
            //Socket s1 = new Socket("localhost", 5999);
            ObjectOutputStream out = new ObjectOutputStream(sslSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sslSocket.getInputStream());
            
            out.writeObject(new Message(nickname, i2p_dest));//here goes nickname and i2p_dest
            out.flush();
            
            ar1 = (ArrayList<Message>) in.readObject();//get users from server
            for (int i = 0; i < ar1.size(); i++) {//remove curent user from list
                if (ar1.get(i).get_i2p_dest().equals(i2p_dest)) {
                    ar1.remove(i);
                }
            }
            System.out.println("New Connection");
            System.out.print("Users:");
            for (int i = 0; i < ar1.size(); i++) {
                System.out.print(" " + ar1.get(i).get_nickname());
            }
            System.out.println();
            Client.registered=true;
            sslSocket.close();
            
            Client.model1.removeAllElements();//remove users
            for (int i = 0; i < ar1.size(); i++) {//insert active users
                Client.model1.addElement(ar1.get(i).get_nickname());
            }
            Client.users=ar1;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(NewConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
