/** Tan Le (tle51) & Janki Patel (jpate53)
 * CS 342 - Project 3: RSA Encryption/Decryption
 * Message Blocking Class - Break the message into block size chunks
 */

import java.io.*;
import java.util.Scanner;

public class MessageBlocking{
    private String messageFile;
    private File blockFile;  //Block file
    private String outputName;  //Name of output file (block file)
    private int blockSize;
    private String message;
    private String asciiValue = "";
    private int i, j;
    
    //Constructor
    public MessageBlocking(String nameString, int sizeInput, String file){  //Take in block file name and block size
        outputName = nameString.concat(".txt");
        messageFile = file.concat(".txt");
        
        blockSize = sizeInput;
        blockFile = new File(outputName);
        convertMessage();
        blockMessage();
    }
    
    //Convert message to decimal value
    public void convertMessage(){
        FileReader fRead = null;
        String tempChar = "";
        try{
            fRead = new FileReader(messageFile);
            int c;
            while((c = fRead.read()) != -1){
                //NULL Character
                if(c == 0){
                    tempChar = "00";
                }
                //Vertical Tab
                else if(c == 11){
                    tempChar = "01";
                }
                //Horizontal Tab
                else if(c == 9){
                    tempChar = "02";
                }
                //New Line
                else if(c == 10){
                    tempChar = "03";
                }
                //Carriage Return
                else if(c == 13){
                    tempChar = "04";
                }
                //Printable characters
                else{
                    int tempCal = c - 27;
                    //Fill in leading zero
                    if(tempCal < 10){
                        tempChar = "0" + Integer.toString(tempCal);
                    }
                    else{
                        c = c - 27;
                        tempChar = Integer.toString(c);
                    }
                }
                asciiValue =  tempChar + asciiValue;
            }
            //System.out.println(asciiValue);
            fRead.close();
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    //Blocking Operation
    public void blockMessage(){
        int incrementCount = blockSize * 2;  //Each character consists of 2 digits
        HugeUnsignedInteger blockValue = new HugeUnsignedInteger(asciiValue);  //Store into array
        String blockString = "";
        int blockCount = 0;
        String leftoverString = "";
        BufferedWriter fWrite;
        
        //Create file if does not exists
        try{
            if(blockFile.exists()){
                blockFile.delete();
            }
            blockFile.createNewFile();
        }
        catch(IOException e){
            System.err.println(e);
        }
        
        //Loop through all the value of the message
        for(i=0; i<blockValue.numDigit; i++){
            //Grab block size amount of characters (2 digits)
            if(blockCount != incrementCount){
                blockString = Integer.toString(blockValue.arr[i]) + blockString;
                blockCount++;
            }
            //Grab enough
            if(blockCount == incrementCount || i==blockValue.numDigit){
                System.out.println(blockString);
                try{
                    //Write to file
                    fWrite = new BufferedWriter(new FileWriter(blockFile,true)); //Temp file for testing
                    fWrite.write(blockString);
                    fWrite.newLine();
                    //fWrite.flush();
                    fWrite.close();
                }
                catch(IOException e){
                    System.err.println(e);
                }
                blockCount = 0;
                blockString = "";
            }
        }
        
        //Leftover values
        int leftoverLength = incrementCount - blockString.length();
        for(i=0; i<leftoverLength; i++){
            //Fill in 0's
            blockString = "0" + blockString;
        }
        //System.out.println(blockString);
        //Write leftover to file
        try{
            fWrite = new BufferedWriter(new FileWriter(blockFile,true)); //Temp file for testing
            fWrite.write(blockString);
            //fWrite.newLine();
            fWrite.close();
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    
    //  //----------------TEST----------------
    //  public static void main(String args[]){
    //    MessageBlocking block = new MessageBlocking("block.txt", 8);    
    //  }
    //  //------------------------------------
    
}