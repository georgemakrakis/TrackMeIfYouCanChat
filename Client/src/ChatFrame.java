
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;

public class ChatFrame extends javax.swing.JFrame {
    
    //Constructor when receiving request from other user (unicast and multicast)
    public ChatFrame(DefaultListModel jl1,String chat_id,I2P_Send sender2) {
        initComponents();
        
        jTextArea2.requestFocusInWindow();//Set focus on the TextArea the user is going to type
        jList1.setSelectionModel(new DisabledItemSelectionModel());//Set the custom selectionmodel
        jList1.setModel(jl1);//DefaultListModel of users in this chat
        chatid = chat_id;
        sender=sender2;
        jTextArea2.setText("");
        jTextArea2.setEditable(true);
        jButton3.setEnabled(true);
        this.setTitle("ChatFrame - "+Client.nickname);
        
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);//Dispose instead of exit app
        
        //check for new messages for this chat every 5 seconds
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Client.new_msgs.size(); i++) {
                    if (Client.new_msgs.get(i).get_chatid().equals(chatid)) {
                        if (Client.new_msgs.get(i).get_newchat())//for multicast mode
                        {
                            jl1.addElement(Client.new_msgs.get(i).get_nickname());//add user nickname to the userlist of this chat
                            jTextArea1.append(Client.new_msgs.get(i).get_nickname() + " Connected to chat..!!!!"+ "\n");
                            
                        }
                        else
                        {
                            //incert new message to jTextArea
                            jTextArea1.append(Client.new_msgs.get(i).get_nickname() + ": "+Client.new_msgs.get(i).get_msg()+"\n");
                        }
                        Client.new_msgs.remove(i);
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
    
    //Constructor when sending request to other user
    public ChatFrame(String user,String di2p, String chat_id, Message msg) {
        initComponents();
        this.setTitle("ChatFrame - "+Client.nickname);
        jTextArea2.requestFocusInWindow();//Set focus on the TextArea the user is going to type
        jList1.setSelectionModel(new DisabledItemSelectionModel());//Set the custom selectionmodel, disable selection on user list
        chatid = chat_id;
        sender = new I2P_Send(di2p);
        sender.send(msg);
        jTextArea1.append("Waiting for "+ user +" to connect....." + "\n");
        exec2 = Executors.newSingleThreadScheduledExecutor();
        
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);//Dispose instead of exit app
        
        
        DefaultListModel jl1 = new DefaultListModel();//New model for the user that will be in the new chat
        jl1.addElement(" ");
        
        jList1.setModel(jl1);//DefaultListModel of users in this chat
        
        //check for user respons every 5 seconds then shutdown executor
        exec2.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Client.new_msgs.size(); i++) {
                    if (Client.new_msgs.get(i).get_msg().equals(chatid + "true_newchat")&&Client.new_msgs.get(i).get_newchat()) {
                        jl1.removeElementAt(0);
                        jl1.addElement(Client.new_msgs.get(i).get_nickname());//add user nickname to the userlist of this chat
                        jTextArea1.append(Client.new_msgs.get(i).get_nickname() + " Connected to chat..!!!!"+ "\n");
                        Client.new_msgs.remove(i);
                        
                        jTextArea2.setText("");
                        jTextArea2.setEditable(true);
                        jButton3.setEnabled(true);
                        
                        exec2.shutdown();
                    }
                    else if(Client.new_msgs.get(i).get_msg().equals(chatid + "false_newchat")) {
                        jTextArea1.append(Client.new_msgs.get(i).get_nickname() + " Declined your request!!!!" + "\n");
                        jTextArea2.setText("");
                        Client.new_msgs.remove(i);
                        exec2.shutdown();
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
        
        //check for new messages for this chat every 5 seconds
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Client.new_msgs.size(); i++) {
                    if (Client.new_msgs.get(i).get_chatid().equals(chatid)&&!Client.new_msgs.get(i).get_msg().equals(chatid + "true_newchat")&&!Client.new_msgs.get(i).get_msg().equals(chatid + "false_newchat")&&!Client.new_msgs.get(i).get_newchat()) {
                        //insert new message to jTextArea
                        jTextArea1.append(Client.new_msgs.get(i).get_nickname() + ": " + Client.new_msgs.get(i).get_msg() + "\n");
                        Client.new_msgs.remove(i);
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TrackMeIfYouCanChat");
        setResizable(false);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "User 1", "User 2", "User 3", "User 4", "User 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setFocusable(false);
        jScrollPane1.setViewportView(jList1);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton3.setText("Send");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText("Please wait for user respond");
        jScrollPane3.setViewportView(jTextArea2);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Users");
        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 21, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Message msg = new Message(chatid,jTextArea2.getText(),Client.nickname,Client.i2p_dest);
        
        sender.send(msg);//Send new message to user
        jTextArea1.append(Client.nickname+ ": "+jTextArea2.getText()+"\n");
        jTextArea2.setText("");
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
    private String chatid;
    private ScheduledExecutorService exec2;
    private I2P_Send sender;
    
    class DisabledItemSelectionModel extends DefaultListSelectionModel {

        @Override
        public void setSelectionInterval(int index0, int index1) {
            super.setSelectionInterval(-1, -1);//Disable selection of users in chat
        }
    }
}
