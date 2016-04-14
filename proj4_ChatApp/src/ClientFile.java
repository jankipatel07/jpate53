/********************************************************************************
 * PROJECT 4 : NETWORK CHAT APPLICATION
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: clientFile.Java : This is the client side of the application 
 * 				and and clients will use this to send and recieve messages
 ********************************************************************************/
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientFile implements ActionListener{
	
	public List<MultipleClients>list = new ArrayList<MultipleClients>();
	// Here we will add all the required components of the chat application
    private JFrame frame = new JFrame("ClientSide: Chat Application");
    private JTextArea textArea = new JTextArea();
    private JLabel online = new JLabel("Currently Online");
    private JTextArea clientDisplay = new JTextArea();
    private JTextField textField = new JTextField(30);
    private JButton sendButton = new JButton("Send");
    private JTextField portfeild = new JTextField("6789");
    private JTextField ip = new JTextField("127.0.0.1");
    private JTextField getUsername = new JTextField(10);
    private JButton portbutton = new JButton("Connect");
    // to send the stream to  the other clients
    private ObjectOutputStream sendStream;
    // to get the stream from the other clients
    private ObjectInputStream getStream;
    // establishing a server
    private ServerSocket CentralServer;
    // setting up the connection between computers
    private Socket connection;
    // IP ADDRESS OF THE USER
    private String userIP ;
    private String message ="";
    private String username = ""; 
    private InetAddress ipAddr;
    
    // constructor of the clientfile takes in the ip address of the user
    public ClientFile(){
    	userIP = "";
    	//FRAME FOR THE DISPLAY
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // creates a new panel to add all the fields in the jframes
        Panel p1 = new Panel();
       
        // Adds the send button with the action listener here
        sendButton.addActionListener(this);
        // adds the text field to view the messages exchanged
        textArea.setEditable(false);
        textField.setEditable(false);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(430, 275));
        
        // add actionlistner to the port button
        portbutton.addActionListener(this);
        //adds the text to view all the clients(current online users)
        clientDisplay.setEditable(false);
        JScrollPane areaScrollPane1 = new JScrollPane(clientDisplay);
        areaScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane1.setPreferredSize(new Dimension(120, 200));
        online.setPreferredSize(new Dimension(120,40));
        p1.add(getUsername);
        p1.add(portfeild);
        p1.add(ip);
        p1.add(portbutton);
        // Adds all the fields in the panel
        p1.add(areaScrollPane);
        //add a label to the client display
        p1.add(areaScrollPane1);
        p1.add(textField);
        p1.add(sendButton);
        // adds the panel to the frame
        frame.add(p1);
        frame.setVisible(true);
    }
 
    //when connecting to the server
    public void run(){
    	try{
    		toConnect(); // it will connect to the specific server
    		setupStreams(); // to connect streams
    		while(true)
    		{
	        	chatting(); //when the chatting actually takes place
    		}
    	}catch(EOFException r){
    		showMessage("\n No Valid connection");
    	}catch(IOException t){
    		t.printStackTrace();
    	}finally{
    		closeApp();
    	}
    	
    }
    
    // Method toConnect : to connect to the central server
    private void toConnect() throws IOException{
    	System.out.println("1");
    	showMessage("Connecting to Central Server ...\n");
    	//userIP = "192.168.1.67";
    	int port = getportnumber();
    	connection = new Socket(InetAddress.getByName(userIP),port);
    	System.out.println("connection print " + connection.toString());
    	//connection = new Socket(ipAddr, port);
    	showMessage("Connected to : " + connection.getInetAddress().getHostAddress());
    }
    
    //Method: setupStreams ---> similar to the Central Server
    private void setupStreams() throws IOException{
    	sendStream = new ObjectOutputStream(connection.getOutputStream());
    	sendStream.flush();

    	getStream = new ObjectInputStream(connection.getInputStream());
    	showMessage("\n Streams setup completedly \n"); ////-----?> update the clients list here
    }
    // method : chatting : chatting between the central server and clients
    private void chatting() throws IOException{
    	String message = "Connected to server...." ;
    	sendMessage(message);
    	canType(true);
    	do{
    		try{
				message = (String) getStream.readObject();
				//show message to the client gui
				showMessage("\n " + message);
				System.out.println("message: "+ message);
    		}catch(ClassNotFoundException c){
    			showMessage("\n Enable to display!");
    		}
    		
    	}while(!message.equals("END"));
    }
    
    // method closes the streams and sockets once done with using
    private void closeApp(){
    	showMessage("\n Closing App");
    	canType(false);
    	try{
    		sendStream.close();
    		getStream.close();
    		connection.close();
    	}catch(IOException c){
    		c.printStackTrace();
    	}
    	
    }
   
    // to get the port number address from the feild
    private int getportnumber(){
    	
    	return Integer.parseInt(portfeild.getText());
    }
    // to get the ip address from the feild
    private String getip(){
    	if(ip.getText() == ""){
    		JOptionPane.showMessageDialog(frame,
    			   "Server feild Empty!");
    	}
    	return ip.getText(); 
    }
    // Method send message  -- sends the message to the display
    private void sendMessage(String w){
    	try{
    		sendStream.writeObject(w);
    		sendStream.flush();
    		showMessage("\n " + w);
    	}catch(IOException z){
    		textArea.append("\n Message not send\n");
    	}
    }
    
    // Method to show the message on the screen
    private void showMessage(final String w){
    	System.out.println("inside show message");
    	SwingUtilities.invokeLater(
    			new Runnable(){
    				public void run(){
    					textArea.append(w);
    				}
    			}
    	);
    }
    
 // Method canType : allows the user to type
    private void canType(final boolean tof){
    	SwingUtilities.invokeLater(
    			new Runnable(){
    				public void run(){
    					textField.setEditable(tof);
    				}
    			}
    	);
    	
    }
    
 // HERE WE ONLY HAVE ONE ACTION LISTNER WHICH WILL BE LOCATED ON THE SEND
    //BUTTON WHUCH READ THE MESSAGE FROM THE TEXT BOX AND DISPLAYS ON THE SCREEN
    @Override public void actionPerformed(ActionEvent arg0) {
    	
    	 if(arg0.getSource() == sendButton){
    		 String message = textField.getText();
    	
	    	//get client's username
	    	username = getUsername.getText();
	    	getUsername.setEditable(false);
	    	//if user didn't enter username
	    	if(username.equals(""))
	    	{	
	    		username = "Anonymous";
	    	}
	    	clientDisplay.setText(username);
	    	String print = username + ": " + message;
	        sendMessage(print);
	        textField.setText("");
    	 }
    }
    
    public static void main(String[]  args){
		ClientFile client1 = new ClientFile();
		client1.run();
	}
}