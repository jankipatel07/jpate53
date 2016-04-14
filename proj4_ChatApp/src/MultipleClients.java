/********************************************************************************
 * PROJECT 4 : NETWORK CHAT APPLICATION
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: MultipleClients.java : This file stores thread's connection information.
 ********************************************************************************/
import java.net.InetAddress;

public class MultipleClients{

	public String name;
	public static InetAddress addr;
	public static int port;
	public int ID;
	
	public MultipleClients(){}
	
	public MultipleClients(String name , InetAddress address , int port, int ID){
		this.name = name;
		this.addr = address;
		this.port = port;
		this.ID =ID;
		
		System.out.println(name + "  " + address + "  " + port + "  " + ID);
	}
	
	public int getID(){
		return ID;
	}
	public int getPort()
	{
		return port;
	}
	
	public InetAddress getIp()
	{
		return addr;
	}
	
	public String getName()
	{
		return name;
	}
}
