/** Tan Le (tle51) & Janki Patel (jpate53)
 * CS 342 - Project 3: RSA Encryption/Decryption
 * Message Unblocking Class - Convert block file into message
 */

import java.io.*;
import java.util.Scanner;

public class MessageUnblocking{
    private String blockName;
    private File blockFile;
    private String unblockName;
    private File unblockFile;
    private String stringInt = "";
    private int i;
    
    //Constructor
    public MessageUnblocking(String filePath, String savePath){
        blockName = filePath.concat(".txt");
        //blockName = filePath;
        blockFile = new File(blockName);
        unblockName = savePath.concat(".txt");
        unblockFile = new File(unblockName);
        //unblockingMessage();
        //convertDecimal();
        convertBlock();
    }
    
    //Unblocking Operation
    public void unblockingMessage(){
        String tempString = "";
        //Combine the blocking together
        try{
            BufferedReader fRead = new BufferedReader(new FileReader(blockFile));
            int c;
            while((tempString = fRead.readLine()) != null){
                stringInt = tempString + stringInt;
            }
            System.out.println(stringInt);
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    //Convert decimal value to ASCII characters
    public void convertDecimal(){
        StringReader strRead = new StringReader(stringInt);
        int tempCounter = 0;
        int tempLen = stringInt.length();
        String reverseString = "";
        String tempString = "";
        //Reverse decimal string
        try{
            for(i=0; i<tempLen; i++){
                char c = (char) strRead.read();
                if(i%2 == 0){
                    tempString = tempString + c;
                }
                else{
                    tempString = tempString + c;
                    reverseString = tempString + reverseString;
                    tempString = "";
                }
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
        System.out.println(reverseString);
        //Convert and write to file
        strRead = new StringReader(reverseString);
        BufferedWriter fWrite;
        tempString = "";
        
        try{
            //Create file
            if(unblockFile.exists()){
                unblockFile.delete();
                unblockFile.createNewFile();
            }
            else{
                unblockFile.createNewFile();
            }
            //Read decimal string
            fWrite = new BufferedWriter(new FileWriter(unblockFile, true));
            for(i=0; i<reverseString.length(); i++){
                char c = (char) strRead.read();
                if(i%2 == 0){
                    tempString = tempString + c;
                }
                //Grab two digits
                else{
                    tempString = tempString + c;
                    //Vertical tab
                    if(tempString.equals("01")){
                        tempString = "11";
                    }
                    //Horizontal tab
                    else if(tempString.equals("02")){
                        tempString = "9";
                    }
                    //New line
                    else if(tempString.equals("03")){
                        tempString = "10";
                    }
                    //Carriage Return
                    else if(tempString.equals("04")){
                        tempString = "13";
                    }
                    else if(tempString.equals("00")){
                        tempString = "00";
                    }
                    else{
                        //System.out.println(tempString);
                        int tempInt = Integer.parseInt(tempString) + 27;
                        tempString = Integer.toString(tempInt);
                    }
                    //Write to file
                    int charValue = Integer.parseInt(tempString);
                    char asciiChar = (char) charValue;
                    if(charValue != 0){
                        fWrite.write(asciiChar);
                        //fWrite.newLine();
                    }
                    tempString = "";
                    
                }
            }
            fWrite.close();
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    //Convert each block to ASCII characters
    public void convertBlock(){
        String tempString = "";
        int tempLen;
        String reverseString = "";
        String resultString = "";
        String outputString = "";
        try{
            //Check if file exist
            if(unblockFile.exists()){
                unblockFile.delete();
                unblockFile.createNewFile();
            }
            else{
                unblockFile.createNewFile();
            }
            //Read block file
            BufferedReader fRead = new BufferedReader(new FileReader(blockFile));
            while((tempString = fRead.readLine()) != null){
                //Read string
                StringReader strRead = new StringReader(tempString);
                tempLen = tempString.length();
                //System.out.println(tempString);
                reverseString = "";
                outputString = "";
                for(i=0; i<tempLen; i++){
                    char c = (char) strRead.read();
                    if(i%2 == 0){
                        outputString = outputString + c;
                    }
                    else{
                        outputString = outputString + c;
                        reverseString = outputString + reverseString;
                        outputString = "";
                    }
                }
                //System.out.println(reverseString);
                
                BufferedWriter fWrite = new BufferedWriter(new FileWriter(unblockFile,true));
                //Convert to ascii characters
                StringReader revRead = new StringReader(reverseString);
                for(i=0; i<reverseString.length(); i++){
                    char c = (char) revRead.read();
                    if(i%2 == 0){
                        resultString = resultString + c;
                    }
                    else{
                        resultString = resultString + c;
                        //Vertical tab
                        if(resultString.equals("01")){
                            resultString = "11";
                        }
                        //Horizontal tab
                        else if(resultString.equals("02")){
                            resultString = "09";
                        }
                        //New line
                        else if(resultString.equals("03")){
                            resultString = "10";
                        }
                        //Carriage return
                        else if(resultString.equals("04")){
                            resultString = "13";
                        }
                        //Null
                        else if(resultString.equals("00")){
                            resultString = "00";
                        }
                        else{
                            int tempInt = Integer.parseInt(resultString) + 27;
                            resultString = Integer.toString(tempInt);
                        }
                        //Write to file
                        int charValue = Integer.parseInt(resultString);
                        char asciiChar = (char) charValue;
                        //System.out.println(asciiChar);
                        if(charValue != 0){
                            fWrite.write(asciiChar);
                        }
                        resultString = "";
                    }
                }  
                fWrite.close();                                           
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    //----------------TEST----------------
    //public static void main(String args[]){
    // MessageUnblocking unblock = new MessageUnblocking("block.txt");    
    //    String one = "01";
    //    System.out.println(Integer.parseInt(one));
    //}
    //------------------------------------
}