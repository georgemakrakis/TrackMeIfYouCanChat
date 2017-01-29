
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author George Makrakis
 */
public class ClientServiceThread extends Thread 
{
    /* SSL Region */
    private SSLSocket sslSocket = null;

    ClientServiceThread(SSLSocket sslSocket)
    {
        this.sslSocket = sslSocket;
    }

    public void run()
    {
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

        try
        {
            // Start handshake
            sslSocket.startHandshake();

            // Get session after the connection is established
            SSLSession sslSession = sslSocket.getSession();

            System.out.println("SSLSession :");
            System.out.println("\tProtocol : " + sslSession.getProtocol());
            System.out.println("\tCipher suite : " + sslSession.getCipherSuite());

            ObjectInputStream in = new ObjectInputStream(sslSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(sslSocket.getOutputStream());
            
            Message msg = (Message) in.readObject();
            
            if(msg.get_HB())//heartbeat
            {
                boolean ok=false;
                System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                System.out.println("\u001B[32m"+"------------HEARTBEAT------------"+"\u001B[0m");
                System.out.println(msg.get_nickname());
                System.out.println("\u001B[32m"+"------------HEARTBEAT------------"+"\u001B[0m");
                for(int i=0;i<Registrar.ar1.size();i++)
                {
                    if(Registrar.ar1.get(i).get_nickname().equals(msg.get_nickname()))
                    {
                        Registrar.ar1.get(i).set_d(new Date());// refresh time for user
                        ok=true;
                        System.out.println("\u001B[32m"+"---------TIME--REFRESHED---------"+"\u001B[0m");
                        System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                    }
                }
                if(ok)
                {
                    out.writeObject("ArrayList");
                    out.flush();
                    
                    out.writeObject(Registrar.ar1);
                    out.flush();
                }
                else
                {
                    out.writeObject("You have been disconnected. Please try again.");
                    out.flush();
                    System.out.println("\u001B[31m"+"---------USER--NOT--FOUND--------"+"\u001B[0m");
                    System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                }  
            }
            else if(msg.get_i2p_dest().equals("null"))//nickname check
            {
                boolean check1=true;
                for (int i = 0; i < Registrar.ar1.size(); i++) {
                    if (Registrar.ar1.get(i).get_nickname().equals(msg.get_nickname())) {
                        check1 = false;
                    }
                }
                out.writeObject(check1);
                out.flush();
            }
            else//user registration
            {
                System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                System.out.println("\u001B[32m"+"------------NEW--USER------------"+"\u001B[0m");
                System.out.println(msg.get_nickname()+ " " + msg.get_i2p_dest());
                System.out.println("\u001B[32m"+"------------NEW--USER------------"+"\u001B[0m");
                System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
            
                out.writeObject(Registrar.ar1);
                out.flush();
                
                msg.set_d(new Date());//setting the time that message arrived
                Registrar.ar1.add(msg);//adding user to List
            }
            
            
            sslSocket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    /*Socket clientSocket;
    
    ClientServiceThread(Socket s) 
    {
      clientSocket = s;
    }
    
    public void run() 
    {
        try
        {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            
            Message msg = (Message) in.readObject();
            
            if(msg.get_HB())//heartbeat
            {
                boolean ok=false;
                System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                System.out.println("\u001B[32m"+"------------HEARTBEAT------------"+"\u001B[0m");
                System.out.println(msg.get_i2p_dest());
                System.out.println("\u001B[32m"+"------------HEARTBEAT------------"+"\u001B[0m");
                for(int i=0;i<Registrar.ar1.size();i++)
                {
                    if(Registrar.ar1.get(i).get_i2p_dest().equals(msg.get_i2p_dest()))
                    {
                        Registrar.ar1.get(i).set_d(new Date());// refresh time for user
                        ok=true;
                        System.out.println("\u001B[32m"+"---------TIME--REFRESHED---------"+"\u001B[0m");
                        System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                    }
                }
                if(ok)
                {
                    out.writeObject("ArrayList");
                    out.flush();
                    
                    out.writeObject(Registrar.ar1);
                    out.flush();
                }
                else
                {
                    out.writeObject("You have been disconnected. Please try again.");
                    out.flush();
                    System.out.println("\u001B[31m"+"---------USER--NOT--FOUND--------"+"\u001B[0m");
                    System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                }  
            }
            else if(msg.get_i2p_dest().equals("null"))//nickname check
            {
                boolean check1=true;
                for (int i = 0; i < Registrar.ar1.size(); i++) {
                    if (Registrar.ar1.get(i).get_nickname().equals(msg.get_nickname())) {
                        check1 = false;
                    }
                }
                out.writeObject(check1);
                out.flush();
            }
            else//user registration
            {
                System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
                System.out.println("\u001B[32m"+"------------NEW--USER------------"+"\u001B[0m");
                System.out.println(msg.get_nickname()+ " " + msg.get_i2p_dest());
                System.out.println("\u001B[32m"+"------------NEW--USER------------"+"\u001B[0m");
                System.out.println("\u001B[37m"+"---------------------------------"+"\u001B[0m");
            
                out.writeObject(Registrar.ar1);
                out.flush();
                
                msg.set_d(new Date());//setting the time that message arrived
                Registrar.ar1.add(msg);//adding user to List
            }
            
            
            clientSocket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        
        
    }*/
}