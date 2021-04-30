import java.net.*; 
import java.io.*; 
import java.util.*;

public class GroupChat extends ED
{ 

	private static final String TERMINATE = "leave";
    static String name; 
    static volatile boolean finished = false;
    static Scanner sc= new Scanner(System.in); 

    public static void main(String[] args) throws Exception
    { 
        if (args.length != 2) 
            System.out.println("Two arguments required: <multicast-host> <port-number>"); 

        else
        { 
            try
            { 
                InetAddress group = InetAddress.getByName(args[0]); 
                int port = Integer.parseInt(args[1]); 
                
                Scanner sc = new Scanner(System.in); 
                System.out.print("Enter your name: "); 
                name = sc.nextLine();
                
                MulticastSocket socket = new MulticastSocket(port); 
 
                socket.setTimeToLive(0); 
                socket.joinGroup(group); 

                Thread t = new Thread(new ReadThread(socket,group,port)); 

                // Spawn a thread for reading messages. 
                t.start();  

                System.out.println("Start typing messages...\n"); 

                while(true) 
                { 
                	String message; 
                    // System.out.println("\u001B[31m");

                    message = sc.nextLine(); 
                    // System.out.println("\u001B[0m");
                    if(message.equalsIgnoreCase(GroupChat.TERMINATE)) 
                    { 
                        finished = true; 
                        socket.leaveGroup(group); 
                        socket.close(); 
                        break;
                    } 

                    message = name + ": " + message;
                    byte[]cipherTextArray= encrypt(message);
                    byte[]buffer= message.getBytes();
                    DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port); 
                    socket.send(datagram); 
                } 
            } 

            catch(SocketException se) 
            { 
                System.out.println("Error creating socket"); 
                se.printStackTrace(); 
            } 

            catch(IOException ie) 
            { 
                System.out.println("Error reading/writing from/to socket"); 
                ie.printStackTrace(); 
            } 
        } 
    } 
} 

class ReadThread extends ED implements Runnable 
{ 
    private MulticastSocket socket; 
    private InetAddress group; 
    private int port; 
    private static final int MAX_LEN = 117; 

    ReadThread(MulticastSocket socket,InetAddress group,int port) 
    { 
        this.socket = socket; 
        this.group = group; 
        this.port = port; 
    } 

    @Override
    public void run()
    { 
        while(!GroupChat.finished) 
        { 
        	byte[]cipherTextArray = new byte[ReadThread.MAX_LEN]; 
            DatagramPacket datagram = new DatagramPacket(cipherTextArray,cipherTextArray.length,group,port); 
            String message;

            try
            {
                socket.receive(datagram);
                //message = decrypt(cipherTextArray);
                message= new String(cipherTextArray, 0, datagram.getLength(), "UTF-8");
                /*byte[]cipher= message.getBytes();
                message= decrypt(cipher);*/

                if(!message.startsWith(GroupChat.name)) 
                    System.out.println("\n" + message +"\n");
            } 

            catch(IOException e) 
            { 
                System.out.println("Socket closed!"); 
            }
            
            catch (Exception e)
            {
				e.printStackTrace();
			} 
        } 
    } 
}