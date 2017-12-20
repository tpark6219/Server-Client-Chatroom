import	java.util.*;
import	java.io.*;
import	java.net.*;

public class SessionThread extends Thread {

	
	private Socket		socket;
	public  File file1;
	FileWriter fw;
	BufferedReader FileRead;
	public String username;
	private int ID = -1;
	private Server  server    = null;
	PrivateStack privateStack;
	private int Flag;


	public SessionThread(Server server,Socket s , File f) throws IOException
	{	this.server = server;
		socket = s;
		file1 = f;
		int x = socket.getPort();
		username = Integer.toString(x);
		ID = socket.getPort();
		privateStack = new PrivateStack(20);
		Flag = 0;
		if(server.k == 6)
			server.k = 0;
		else
		server.k++;
		
	}
	public synchronized boolean CheckName(String s)
	{
		for(int i = 0;i<server.clients.length; i++)
		{   
			if(server.clients[i] != null)
			{
				if(server.clients[i].username.equals(s))
					return true;
			}
		}
		return false;
	}
	
	public int getID()
	{  return ID;
	
    }
	    
	public synchronized void writeToText(String s, PrintWriter toClient) throws IOException
	{
		StringBuffer buffer;
		PrintWriter	PrivateClient;
			
		
		
		if(s.startsWith("@exit") && s.length() == 5)
		{
			buffer = new StringBuffer("Exiting...");
			toClient.println(buffer);
			server.remove(username);
			
			socket.close();
			System.exit(0);
			
		}else if(s.startsWith("@private") && s.length() > 9)
		{	int num = server.find(s.substring(9));
			if(num >= 0)
				{	if(server.clients[num].Flag ==1)
				{
					buffer = new StringBuffer("User is already in private");
					toClient.println(buffer);
					return;
				}
					privateStack.push(server.clients[num]);	
					server.clients[num].Flag = 1;
				
				}else{
					
					buffer = new StringBuffer("Invalid User");
					toClient.println(buffer);
				}
			
			return;
					
		}else if(s.startsWith("@end") && s.length() > 5)
		{	
				if(!privateStack.findIfActive(s.substring(5)))
				{
					buffer = new StringBuffer(s.substring(5) + " is not your user");
					toClient.println(buffer);
					return;
				}
				int flag = privateStack.remove(s.substring(5));	
				
				if(flag == -1 )
				{
					buffer = new StringBuffer("Invalid User");
					
				}else{
					buffer = new StringBuffer(s.substring(5) + " has been removed.");
					server.clients[server.find(s.substring(5))].Flag = 0;
				}
				toClient.println(buffer);
				return;
		
		}else if(s.startsWith("@name") && s.length() > 6)
		{	if(s.startsWith("@name") && s.length() == 6)
			{	
				buffer = new StringBuffer("Invalid User");
				toClient.println(buffer);
				return;
			}else if (CheckName(s.substring(6)))
			{
				buffer = new StringBuffer("Name already Exists");
				toClient.println(buffer);
				return;
			}
			username = s.substring(6);
			return;
		}	
			
			if(privateStack.users >= 0 )
			{
				
			    PrivateClient = new PrintWriter( new OutputStreamWriter(privateStack.stackArray[privateStack.users].socket.getOutputStream() ), true );
				buffer = new StringBuffer(this.username + " to " + privateStack.stackArray[privateStack.users].username + "(Private):" + s);
				toClient.println(buffer);
				PrivateClient.println(buffer);
				
				
				return;
			}
			
		fw.write(username + ": " + server.colorArray[server.k]+s+"\u001B[0m");
		fw.write(System.getProperty( "line.separator" ));

	}
	
	
	public synchronized void run()
	{
		BufferedReader	fromClient;
		PrintWriter	toClient;
		String s;
		StringBuffer	buffer;
		
	
		try {
			
			fromClient = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			toClient = new PrintWriter( new OutputStreamWriter( socket.getOutputStream() ), true );
			FileRead = new BufferedReader(new FileReader(file1));
			if(file1.exists())
			{
				
		
				while ( (s = FileRead.readLine()) != null )
				{	//buffer = new StringBuffer(server.colorArray[0] + s );
					toClient.println(s);
				}
				toClient.println(s);

				}

				while (true)
				{   
					
					fw = new FileWriter(file1,true);
				
							
					if((s = FileRead.readLine()) != null)
					{
						
						
						toClient.println(s);
						

					}
					
					
					if(fromClient.ready())
					{
					if((s = fromClient.readLine()) != null)
					{
						
						writeToText(s, toClient);
						
					}else
					{
						break;
					}
					
					
					}
					//How do I keep reading from file but constantly read from client.
					fw.close();
				}
				fw.close();
				FileRead.close();
				socket.close();
				System.out.println("end");
			}
		catch ( Exception e )
		{
			System.out.println( "Exception in SessionThread." );
		}
	}
}