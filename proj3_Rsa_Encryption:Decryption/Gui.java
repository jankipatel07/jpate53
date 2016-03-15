/** Tan Le (tle51) & Janki Patel (jpate53)
 * CS 342 - Project 3: RSA Encryption/Decryption
 * Gui Class - Sets up Gui
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

public class Gui extends JFrame implements ActionListener{
    private JTextField primeField, primeField2;
    private JButton bt1;
    private JMenuBar menuBar, helpBar;
    
    private JMenuItem menu, keyMenu, blockMenu,
    unblockMenu, encrMenu, decrMenu, exitMenu;
    
    private JMenuItem aboutMenu, helpMenu, help;
    
    private JPanel panel; //For Dropdown Menu
    private JPanel panel2; //Panel to display the output
    private JPanel blockPanel;
    private JPanel unblockPanel;
    private JPanel encryptPanel;
    private JPanel decryptPanel;
    private JPanel fieldPanel; //Panel for 2 text fields
    private Container container;
    
    private String primeStr1, primeStr2;
    private String blockSize, blockFile, eFile;
    
    //Constructor
    public Gui()
    {
        super("RSA Encryption Decryption");
        
        panel = new JPanel(); //panel object
        fieldPanel = new JPanel();
        bt1 = new JButton("Enter"); //Button to Submit the prime keys.
        menuBar = new JMenuBar(); //object for menu bar
        helpBar = new JMenuBar();
        primeField = new JTextField(20); //text field object to get prime number
        primeField2 = new JTextField(20); //text field object to get 2nd prime number
        
        //Set up text field to get prime numbers
        primeField.setText("Prime 1");
        primeField2.setText("Prime 2");
        bt1.addActionListener(this);
        
        /* MenuBar RSA has the following Menu list:
         * "Create a Key", "Block a File", "Unblock a File", "Encrypt", "Decrypt", "Exit"
         */
        menu = new JMenu("RSA");
        menuBar.add(menu);
        
        // HelpBar creates a menu containing help and about section
        helpMenu = new JMenu("Help");
        helpBar.add(helpMenu);
        
        //Menu Items for RSA menu
        keyMenu = new JMenuItem("Create Key");
        menu.add(keyMenu); //add menu to the menu bar
        keyMenu.addActionListener(this); //set action listener for the menu item
        
        blockMenu = new JMenuItem("Block a File");
        menu.add(blockMenu);
        blockMenu.addActionListener(this);
        
        unblockMenu = new JMenuItem("Unblock a File");
        menu.add(unblockMenu);
        unblockMenu.addActionListener(this);
        
        encrMenu = new JMenuItem("Encrypt");
        menu.add(encrMenu);
        encrMenu.addActionListener(this);
        
        decrMenu = new JMenuItem("Decrypt");
        menu.add(decrMenu);
        decrMenu.addActionListener(this);
        
        exitMenu = new JMenuItem("Exit");
        menu.add(exitMenu);
        exitMenu.addActionListener(this);
        
        //Menu Items for Help Menu Bar
        aboutMenu = new JMenuItem("About");
        helpMenu.add(aboutMenu);
        aboutMenu.addActionListener(this);
        
        help = new JMenuItem("Help");
        helpMenu.add(help);
        help.addActionListener(this);
        
        //Set size of the text field
        primeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, primeField.getMinimumSize().height));
        primeField2.setMaximumSize(new Dimension(Integer.MAX_VALUE, primeField2.getMinimumSize().height));
        
        //Add the text field to the panel
        fieldPanel.add(primeField);
        fieldPanel.add(primeField2);
        fieldPanel.add(bt1);
        
        //Add the menu to the panel
        panel.add(menuBar);
        panel.add(helpBar);
        
        //add panel to the container
        container = getContentPane();
        container.add(panel, BorderLayout.PAGE_START);
        container.add(fieldPanel);
        
        //Set screen size and make it visible
        setSize(600, 300);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String str = e.getActionCommand();
        
        //get the textbox input
        String prime1 = primeField.getText();
        String prime2 = primeField2.getText();
        
        //Enter button is clicked
        if(e.getActionCommand().equals("Enter"))
        {
            //Check that user has entered values for both prime numbers
            if(prime1.equals("Prime 1") || prime2.equals("Prime 2"))
            {
                JOptionPane.showMessageDialog(null, "You must enter values for both prime numbers.",
                                              "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                primeHandler();
            }
        }
        
        if(str.equals("Create Key"))
            createKey();
        else if(str.equals("Block a File"))
            blockMenu();
        else if(str.equals("Unblock a File"))
            unblockMenu();
        else if(str.equals("Encrypt"))
            encryptMenu();
        else if(str.equals("Decrypt"))
            decryptMenu();
        else if(str.equals("Exit"))
            System.exit(0);
        else if(str.equals("About"))
            about();
        else if(str.equals("Help"))
            help();
    }
    
    //Get the value of the two prime numbers
    public void primeHandler()
    {
        String prime1 = primeField.getText();
        String prime2 = primeField2.getText();
        
        //		primeStr1 = prime1;
        //		primeStr2 = prime2;
        
        //Check if the values are prime number
        if(primeCheck(prime1) && primeCheck(prime2))
        {
            //Set prime numbers to the user input
            JOptionPane.showMessageDialog(null, "Both values are valid prime numbers.",
                                          "Prime Numbers", JOptionPane.INFORMATION_MESSAGE);
            primeStr1 = prime1;
            primeStr2 = prime2;
        }
        //Both numbers are not prime
        else if(!primeCheck(prime1) && !primeCheck(prime2))
        {
            JOptionPane.showMessageDialog(null, "The values are not prime numbers.\n" + "Please enter the values again.",
                                          "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //First value is not prime
        else if(!primeCheck(prime1))
        {
            JOptionPane.showMessageDialog(null, "First value is not a prime number.\n" + "Please enter the value again.",
                                          "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //Second value is not prime
        else if(!primeCheck(prime2))
        {
            JOptionPane.showMessageDialog(null, "Second value is not a prime number.\n" + "Please enter the value again.",
                                          "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
    
    //Check if for prime number
    public static Boolean primeCheck(String p)
    {
        HugeUnsignedInteger p1 = new HugeUnsignedInteger(p);  //number to be checked (n)
        HugeUnsignedInteger two = new HugeUnsignedInteger("2");
        HugeUnsignedInteger i = new HugeUnsignedInteger("3"); //counter
        
        //Check if n is a multiple of 2
        if(p1.modulus(two).equals("0")){
            return false;
        }
        //i * i
        HugeUnsignedInteger iSquare = new HugeUnsignedInteger(i.multiplication(i));
        //Check the odd numbers
        while(iSquare.lessThan(p1) == 1 || iSquare.equalTo(p1) == 1){
            if(p1.modulus(i).equals("0")){
                return false;
            }
            i = new HugeUnsignedInteger(i.addition(two)); //i += 1
            iSquare = new HugeUnsignedInteger(i.multiplication(i)); //squaring
        }
        
        return true;
    }
    
    //Create random prime numbers
    public void generatePrime()
    {
        String[] arr = new String[20];
        //Read primeNumbers.rsc file
        try
        {
            int i = 0;
            Scanner sc = new Scanner(new File("primeNumbers.rsc.txt"));
            
            while(sc.hasNextLine())
            {
                String s = sc.nextLine();
                arr[i] = s;
                i++;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
        
        //Generate random number
        int temp = (int)(Math.random() * 20);
        int temp2 = (int)(Math.random() * 20);
        
        while(temp == temp2)
        {
            temp = (int)(Math.random() * 20);
            temp2 = (int)(Math.random() * 20);
        }
        
        //assign prime number to the random index of the array.
        primeStr1 = arr[temp];
        primeStr2 = arr[temp2];
        
    }
    //Key object to display the key information.
    public void createKey()
    {
        panel2 = new JPanel();
        
        //Check if User entered prime number or not
        while(primeStr1 == null || primeStr2 == null)
        {
            generatePrime();
        }
        
        //Get name of public and private files.
        String publicFile = JOptionPane.showInputDialog("Enter file name for public key.");
        String privateFile = JOptionPane.showInputDialog("Enter file name for private key.");
        
        if(publicFile != null && privateFile != null)
        {
            RsaAlgorithm key = new RsaAlgorithm(primeStr1, primeStr2, publicFile, privateFile);
            
            System.out.println("prime read is " + primeStr1);
            System.out.println("prime2 read is "+ primeStr2);
            
            JLabel l = new JLabel("Public key " + publicFile + " and private key " + privateFile + " was created successfully.");
            
            //make room between text field of prime numbers and the message.
            panel2.add(Box.createRigidArea(new Dimension(0, 200)));
            
            panel2.add(l);
            container = getContentPane();
            container.add(panel2, BorderLayout.SOUTH);
            setVisible(true);
        }
    }
    
    //Display Blocked File's information
    public void blockMenu()
    {
        //container.remove(panel2);
        
        blockPanel = new JPanel();
        
        //Get name of the block file.
        String messageFile = JOptionPane.showInputDialog("Enter file name of the message.");
        blockFile = JOptionPane.showInputDialog("Enter file name to save the block.");
        
        blockSize = JOptionPane.showInputDialog("Enter block size.\n" + "Suggessition:\n" +
                                                "Block should be same or one more than the length of prime number. \n" +
                                                "If your prime number was generated by the program, enter the block size of 10.");
        
        while(Integer.parseInt(blockSize) < 0 || Integer.parseInt(blockSize) > 10)
        {
            JOptionPane.showMessageDialog(null, "Block size must be between 1 and 10.\n",
                                          "Error", JOptionPane.INFORMATION_MESSAGE);
            blockSize = JOptionPane.showInputDialog("Enter block size.\n" + "Suggessition:\n" +
                                                    "Block should be same or one more than the length of prime number. \n" +
                                                    "If your prime number was generated by the program, enter the block size of 10.");
        }
        
        if(messageFile != null && blockFile != null && blockSize != null)
        {
            MessageBlocking blocking = new MessageBlocking(blockFile, Integer.parseInt(blockSize), messageFile);
            JLabel l = new JLabel("Block File created successfully.");
            blockPanel.add(l);
            
            //make room between text field of prime numbers and the message.
            blockPanel.add(Box.createRigidArea(new Dimension(0, 200)));
            
            blockPanel.add(l);
            container = getContentPane();
            container.add(blockPanel, BorderLayout.SOUTH);
            
            setVisible(true);
        }
    }
    
    //Display Unblocked File's information
    public void unblockMenu()
    {
        unblockPanel = new JPanel();
        
        //Get name of the block file.
        String unblockFile = JOptionPane.showInputDialog("Enter filename containg the decrypted block file.");
        
        if(unblockFile != null)
        {
            MessageUnblocking unblocking = new MessageUnblocking(unblockFile);
            
            JLabel l = new JLabel("Unblocking File successfully done.");
            unblockPanel.add(l);
            
            //make room between text field of prime numbers and the message.
            unblockPanel.add(Box.createRigidArea(new Dimension(0, 200)));
            
            unblockPanel.add(l);
            container = getContentPane();
            container.add(unblockPanel, BorderLayout.SOUTH);
            
            setVisible(true);
        }
    }
    
    //Display Unblocked File's information
    public void encryptMenu()
    {
        encryptPanel = new JPanel();
        
        //Get name of the block file.
        String encryptFile = JOptionPane.showInputDialog("Enter file name of the public key.");
        eFile = JOptionPane.showInputDialog("Enter file name to save the encrypt file.");
        
        System.out.println(blockFile);
        if(encryptFile != null && eFile != null)
        {
            Encryption encrypt = new Encryption(encryptFile, blockFile, eFile);
            JLabel l = new JLabel("Encryption done successfully.");
            encryptPanel.add(l);
            
            //make room between text field of prime numbers and the message.
            encryptPanel.add(Box.createRigidArea(new Dimension(0, 200)));
            
            encryptPanel.add(l);
            container = getContentPane();
            container.add(encryptPanel, BorderLayout.SOUTH);
            
            setVisible(true);
        }
    }	
    
    //Display Unblocked File's information
    public void decryptMenu()
    {
        decryptPanel = new JPanel();
        
        //Get name of the file to decrypt from
        String decryptFile = JOptionPane.showInputDialog("Enter private key file name.");
        String eFile2 = JOptionPane.showInputDialog("Enter file name of the encrypted file.");
        String saveFile = JOptionPane.showInputDialog("Enter file name to save the decrypt file.");
        
        if(decryptFile != null && saveFile != null && eFile2 != null)
        {
            Decryption decrypt = new Decryption(decryptFile, Integer.parseInt(blockSize), saveFile, eFile2);		
            JLabel l = new JLabel("Decryption done successfully.");
            decryptPanel.add(l);
            
            //make room between text field of prime numbers and the message.
            decryptPanel.add(Box.createRigidArea(new Dimension(0, 200)));
            
            decryptPanel.add(l);
            container = getContentPane();
            container.add(decryptPanel, BorderLayout.SOUTH);
            
            setVisible(true);
        }
    }
				
    //Message displaying information about the program.
    private void about()
    {
        JOptionPane.showMessageDialog(null, "This RSA Encryption/Descryption program is implemented for Software Design course (CS-342) Class at UIC.\n"
                                      +"The team members for this project are Tan Le (tle51) and Janki Patel (jpate53)\n", "About RSA Encryption/Decryption", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Help message for the program.
    private void help()
    {
        //TODO
        JOptionPane.showMessageDialog(null, "Program's functions are located under the RSA Menu.\n"
                                      + "Provide 2 prime numbers to generate public and private keys.\n" 
                                      + "Follow this general procedure:\n"
                                      + "1. Person 1 creates a public-private key set.\n"
                                      + "2. Person 1 sends the public key to Person 2 but keeps the private key\n"
                                      + "3. Person 2 creates an ASCII text message.\n"
                                      + "4. Person 2 blocks the message into a blocked file with block size X.\n"
                                      + "5. Person 2 uses Person 1’s public key to encrypt the blocked file.\n"
                                      + "6. Person 2 send the encrypted blocked file to Person 1\n"
                                      + "7. Person 1 decrypts the encrypted blocked file using Person 1’s \n" 
                                      + "   private key to get the original blocked file Person 2 created in step 4.\n"
                                      + "8. Person 1 unblocks the file created in step 7 to get the original ASCII text message.\n",
                                      "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}//End of Class
