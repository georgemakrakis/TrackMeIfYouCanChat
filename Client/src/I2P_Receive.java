
import java.io.ObjectInputStream;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import net.i2p.client.streaming.I2PSocket;


public class I2P_Receive implements Runnable {

        private I2PSocket sock;

        public I2P_Receive(I2PSocket socket) {
            this.sock = socket;
        }

        public void run() {
            try
            {
                if (sock != null) 
                {
                    ObjectInputStream objectinstream = new ObjectInputStream(sock.getInputStream());
                    Message msg = (Message) objectinstream.readObject();
                    if (msg.get_chatid().equals("new_chat")&&msg.get_newchat())
                    {
                        int dialogButton = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(null, msg.get_nickname()+" send you a chat request! Do you want to accept it?", "Chat request!", dialogButton);
                        if (dialogResult == 0) {//if yes
                            Message msg2 = new Message(" ",msg.get_msg() + "true_newchat",Client.nickname,Client.i2p_dest,true);
                            //inside msg.get_msg() is the chatID so we have [chatID]+"true_newchat" for verification
                            
                            I2P_Send sender = new I2P_Send(msg.get_i2p_dest());
                            sender.send(msg2);
                            
                            DefaultListModel dfl1 = new DefaultListModel();
                            dfl1.addElement(msg.get_nickname());
                            
                            ChatFrame CF = new ChatFrame(dfl1,msg.get_msg(),sender);
                            CF.setVisible(true);
                            
                            Message msg3;
                            while (true)
                            {
                                msg3 = (Message) objectinstream.readObject();
                                System.out.println("(new chat)New message added!: " + msg3.get_msg());
                                Client.new_msgs.add(msg3);
                            }
                        } else {//if no
                            Message msg2 = new Message(" ",msg.get_msg()+"false_newchat",Client.nickname);
                            
                            new I2P_Send(msg.get_i2p_dest(),msg2);
                        }
                    }
                    else if(msg.get_newchat())//new chat response means we have established a new chat
                    {
                        Client.new_msgs.add(msg);
                        System.out.println("newchat");
                        
                        Message msg3;
                        while (true) {//so the socket wont close
                            msg3 = (Message) objectinstream.readObject();
                            System.out.println("(I started new chat)New message added!: " + msg3.get_msg());
                            Client.new_msgs.add(msg3);
                        }
                    }
                    else
                    {
                        System.out.println("New message added!: "+msg.get_msg());
                        Client.new_msgs.add(msg);   
                    }
                }
            }
            catch(Exception ex){}
            
        }
}
