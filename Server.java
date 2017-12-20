import	java.util.*;
import	java.io.*;
import	java.net.*;

public class Server implements Runnable{

	public static File file1;
	private static int clientCount = 0;
	public SessionThread clients[] = new SessionThread[20];
	Socket		socket;
	ServerSocket	serverSocket;
	private Thread thread = null;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public String[] colorArray = new String[7];
	public int k = -1;
	
	 public Server(int port)
	   {  try
	      {  
	         serverSocket = new ServerSocket(port, 20);  
	         System.out.println("Server started: " + serverSocket);
	         start(); }
	      catch(IOException ioe)
	      {  System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); }
	  
	   colorArray[0] = ANSI_RED;
	   colorArray[1] = ANSI_WHITE;
	   colorArray[2] = ANSI_GREEN;
	   colorArray[3] = ANSI_YELLOW;
	   colorArray[4] = ANSI_BLUE;
	   colorArray[5] = ANSI_PURPLE;
	   colorArray[6] = ANSI_CYAN;
	   
	   
	   
	   }
	 
	   public void start()  { 
		   if (thread == null)
	       {
	           // Starts new thread for client
	           thread = new Thread(this);
	           thread.start();
	       } }
	public void run()
	   {  		try {
		   
		   serverSocket.setReuseAddress( true );
		  FileOutputStream File1 = new FileOutputStream("output.txt");
		   file1 = new File("output.txt");
		   
		while ( thread != null )
			{
			
					if((socket = serverSocket.accept()) == null)
			{
				break;
			}

				if (clientCount < clients.length)
			      {  System.out.println("Client accepted: " + socket);
			      
			         clients[clientCount] = new SessionThread(this, socket , file1);
			         
			         clients[clientCount].start();
			         
			         clientCount++;
			       
			      }	
			      else
			         System.out.println("Client refused: maximum " + clients.length + " reached.");
				

				
			}
		serverSocket.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   }

	 
	   public Boolean getStatus() {
	       return serverSocket != null;
	   }
	   
	   
	   public synchronized void remove(String username)
	   {  int position = find(username);
	      if (position >= 0)
	      { 
	    	  
	    	 SessionThread removeClient = clients[position];
	         System.out.println("Removing client thread " + username + " at " + position);
	        
	         if (position < clientCount-1)
	            for (int i = position+1; i < clientCount; i++)
	               clients[i-1] = clients[i];
	         clientCount--;
	         try
	         {   if (socket != null)    
	        	 socket.close();
	         }
	         catch(IOException ioe)
	         {  System.out.println(ioe); }
	         removeClient.stop(); }
	   }
	   
     public int find(String username)
	   {  
    	 
    	 for (int i = 0; i < clientCount; i++)
    	 {
	         if (clients[i].username.equals(username))
	            return i;
    	 }
	      
    	 	return -1;
	   }
     

     
	public static void main( String [] arg ) throws Exception
	{	Server server;
		server = new Server(3000);
		if (server.getStatus())
	           server.start();
	}	
	}

/*TODO: 1. @name thing. If user does not put a name, use a number from 1-20
 		2. Read from the text file so everyone can see. 
 		3. Private chat. @_____ (use the an array of list of names)
 */

