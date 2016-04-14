/********************************************************************************
 * PROJECT 4 : NETWORK CHAT APPLICATION
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: CentralServer.java : This file Establishes the central
 * 				connection between the clients by streaming
 * 			    and sockets for the program as well  as the basic GUI for 
 *              running this application is established within
 *              this class.
 ********************************************************************************/
import java.awt.Dimension;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
 
public class CentralServer implements ActionListener {
	
		// for multiple clients
		public List<MultipleClients>clients = new ArrayList<MultipleClients>();
		
		// Here we will add all the required components of the chat application
		public JFrame frame = new JFrame("Project 4 - Chat Application");
        public JTextArea textArea = new JTextArea();
        public JLabel online = new JLabel("Currently Online");
        public JTextArea clientDisplay = new JTextArea();
        public JTextField textField = new JTextField(30);
        public JButton sendButton = new JButton("Send");
        // to send the stream to  the other clients
        public ObjectOutputStream sendStream;
        // to get the stream from the other clients
        public ObjectInputStream getStream;
        // establishing a server
        public ServerSocket CentralServer;
        // setting up the connection between computers
        public Socket connection;

        public  SocketThread temp;
        // constructor for the chatApp
        public CentralServer() {
        		
        		//FRAME FOR THE DISPLAY
                frame.setSize(600, 375);
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
                
                //adds the text to view all the clients(current online users)
                clientDisplay.setEditable(false);
                JScrollPane areaScrollPane1 = new JScrollPane(clientDisplay);
                areaScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                areaScrollPane1.setPreferredSize(new Dimension(120, 200));
                online.setPreferredSize(new Dimension(120,40));
                // Adds all the fields in the panel
                p1.add(areaScrollPane);
                //add a label to the client display
                //p1.add(online);
                p1.add(areaScrollPane1);
                p1.add(textField);
                p1.add(sendButton);
                // adds the panel to the frame
                frame.add(p1);
                frame.setVisible(true);
                //temp = new ConnectionThread(this);
                new ConnectThread(this);
        }

        // HERE WE ONLY HAVE ONE ACTION LISTNER WHICH WILL BE LOCATED ON THE SEND
        //BUTTON WHUCH READ THE MESSAGE FROM THE TEXT BOX AND DISPLAYS ON THE SCREEN
        @Override public void actionPerformed(ActionEvent arg0) {
                String message = textField.getText();
                
                try {
                	//ConnectionThread temp = new ConnectionThread(this);
                	//temp.RunServer();
                	new ConnectThread(this);
					temp.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                textField.setText("");
        }
}

//thread to connect server to client
class ConnectThread extends Thread
{
	CentralServer gui;
	public ConnectThread(CentralServer cs)
	{
		gui = cs;
		start();
	}
	public void run()
	{
		//keep reading the socket
		try {
			while(true)
		    {
		        //open a socket
	    		ServerSocket ss = new ServerSocket(6789,100);
    			while(true)
				{
    				//create socket for all threads
    				SocketThread client1 = new SocketThread(gui, ss.accept());
	 
    				//add threads to the list
	            	gui.clients.add(new MultipleClients("first", client1.getInetAddress(),6789,50));
				}
			}
		}catch (IOException e) {
				e.printStackTrace();
			}
	}
}

class SocketThread extends Thread
{
	CentralServer gui;
	Socket soc;
	
	public SocketThread(CentralServer c, Socket socket)
	{
		gui = c;
		soc = socket;
		start();
	}
		//Get Ip address of the machine
        public InetAddress getInetAddress() throws IOException {
        	InetAddress ip = InetAddress.getLocalHost();
			return ip;
		}
		//Once the GUI is set we now will establish our connection
        // Class : RunServer -
        public void run(){
        	
        	try{
        		// 6789 - port number , where we want to connect
        		// 100 - backlog , number of people can wait
        		while(true){
        			try{
        				// here we will connect and have a chat
        				setupStream(); 
        				chatting(); 
        			}catch(EOFException c){
        				// this will be encountered when the stream ends
        				showMessage("\n Connection End !\n ");
        			}finally{ // make sure we close the socket once we are done
        				closeApp();//closeCrap();
        			}
        		}
        	}catch(IOException c){
        		c.printStackTrace();
        	}
        	
        }

     // Method : setupStream : set up the Stream with the connection found
        private void setupStream() throws IOException{
        	// setup the output stream
        	gui.sendStream = new ObjectOutputStream(soc.getOutputStream());
        	gui.sendStream.flush();
        	
        	gui.getStream = new ObjectInputStream(soc.getInputStream());
            showMessage("Current Status : Stream Setup Complete \n");
        }
     // Method : chatting - the method in which the actual chatting takes 
        //      place
        private void chatting() throws IOException{
        	String message = "Connected...." ;
        	sendMessage(message);
        	canType(true);
        	// continue a conversation until the client wants to
        	while(!message.equals("END")){
        		try{
        			message = (String)gui.getStream.readObject();
        			showMessage("\n" +message);
        		}catch(ClassNotFoundException r){
        			showMessage("\n Not a valid object");
        		}
        	}//while(!message.equals("END"));
        }
        
        //Method : closeApp - Close the application(streams and socket) when done with it
        private void closeApp(){
        	showMessage("\n Current Status :Closing the connections \n");
        	canType(false);
        	try{
        		gui.sendStream.close();
        		gui.getStream.close();
        		soc.close();
        		
        	}catch(IOException f){
        		f.printStackTrace();
        	}
        }
        
        // Method : sendMessage : sends the message to the client
        public void sendMessage(String w) throws IOException{
        	try{
        		gui.sendStream.writeObject("SERVER - " + w);
        		gui.sendStream.flush();
        		showMessage("\n SERVER - " + w);
        	}catch(IOException z){
        		gui.textArea.append("\n Message not send\n");
        	}
        }
        
        // Method : showMessage: shows the message on the text feild
        private void showMessage(final String text){
        	SwingUtilities.invokeLater(
        			new Runnable(){
        				public void run(){
        					gui.textArea.append(text);
        				}
        			}
        	);
        }
        
        // Method canType : allows the user to type
        private void canType(final boolean tof){
        	SwingUtilities.invokeLater(
        			new Runnable(){
        				public void run(){
        					gui.textField.setEditable(tof);
        				}
        			}
        	);
        	
        }
}
