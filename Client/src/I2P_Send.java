
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.i2p.I2PException;
import net.i2p.client.streaming.I2PSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.client.streaming.I2PSocketManagerFactory;
import net.i2p.data.DataFormatException;
import net.i2p.data.Destination;

public class I2P_Send {
    private ObjectOutputStream objectoutstream2;
    private String destinationS;
    public I2P_Send(String destinationString,Message msg)
    {
        this.destinationS=destinationString;
        I2PSocketManager manager = I2PSocketManagerFactory.createManager();
        
        Destination destination;
        try {
            destination = new Destination(destinationString);
        } catch (DataFormatException ex) {
            System.out.println("Destination string incorrectly formatted.");
            return;
        }
        I2PSocket socket;
        try {
            socket = manager.connect(destination);
        } catch (I2PException ex) {
            System.out.println("General I2P exception occurred!");
            return;
        } catch (ConnectException ex) {
            System.out.println("Failed to connect!");
            return;
        } catch (NoRouteToHostException ex) {
            System.out.println("Couldn't find host!");
            return;
        } catch (InterruptedIOException ex) {
            System.out.println("Sending/receiving was interrupted!");
            return;
        }
        try 
        {
            ObjectOutputStream objectoutstream = new ObjectOutputStream(socket.getOutputStream());
            
            objectoutstream.writeObject(msg);
            objectoutstream.flush();//Flush to make sure everything got sent
            
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error occurred while sending!");
        }
    }
    public I2P_Send(String destinationString) {
        this.destinationS=destinationString;
        I2PSocketManager manager = I2PSocketManagerFactory.createManager();

        Destination destination;
        try {
            destination = new Destination(destinationString);
        } catch (DataFormatException ex) {
            System.out.println("Destination string incorrectly formatted.");
            return;
        }
        I2PSocket socket;
        try {
            socket = manager.connect(destination);
        } catch (I2PException ex) {
            System.out.println("General I2P exception occurred!");
            return;
        } catch (ConnectException ex) {
            System.out.println("Failed to connect!");
            return;
        } catch (NoRouteToHostException ex) {
            System.out.println("Couldn't find host!");
            return;
        } catch (InterruptedIOException ex) {
            System.out.println("Sending/receiving was interrupted!");
            return;
        }
        try {
            this.objectoutstream2 = new ObjectOutputStream(socket.getOutputStream());

            

            //socket.close();
        } catch (IOException ex) {
            System.out.println("Error occurred while sending!");
        }
    }
    public void send(Message msg2)
    {
        try {
            this.objectoutstream2.writeObject(msg2);
            this.objectoutstream2.flush();//Flush to make sure everything got sent
        } catch (IOException ex) {
            Logger.getLogger(I2P_Send.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String getDestination()
    {
        return destinationS;
    }
   
}
