import	java.util.*;
import	java.io.*;
import	java.net.*;


public class Client2 {

	public static final String ANSI_RED = "\u001B[31m";
	
	private static Socket connect( String host ) throws Exception
	{
		try
		{
			return new Socket( host, 3000 );
		}
		catch ( ConnectException ce )
		{
			return null;
		}
	}

	public static void main( String [] arg ) throws Exception
	{	
		Socket		socket;
		BufferedReader	stdIn;
		BufferedReader	fromServer;
		PrintWriter	toServer;
		String		s;
		String		result;
		
		do {
			socket = connect("localhost" );
		} while ( socket == null );
		System.out.println("Connecting to server...");
		stdIn = new BufferedReader( new InputStreamReader( System.in ) );
		fromServer = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
		toServer = new PrintWriter( new OutputStreamWriter( socket.getOutputStream() ), true );
		
		
		while ((result = fromServer.readLine()) != null)
		{	if(result.equalsIgnoreCase("null"))
			break;
			System.out.println(result);
		}
		

		
		while (true)
		{	
			if(stdIn.ready())
		{

			if((s = stdIn.readLine()) != null )
			{
				
			toServer.println( s );
			}else{
				break;	
			}
		}
			
			if(fromServer.ready())
			{
			result = fromServer.readLine();
			System.out.println(result);
			}
		}
		socket.close();
	}
}