/** Tan Le (tle51) & Janki Patel (jpate53)
 * CS 342 - Project 3: RSA Encryption/Decryption
 * Encryption Class - Encrypt a block file using a public key
 */

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class Encryption{
    private HugeUnsignedInteger e;
    private HugeUnsignedInteger n;
    private int i;
    private String xmlName;
    private File xmlFile;
    private String bName, eName;
    private File bFile;
    private File eFile;
    
    //Constructor
    public Encryption(String filePath, String blockPath, String ePath){
        xmlName = filePath.concat(".txt");
        xmlFile = new File(xmlName);
        bName = blockPath.concat(".txt");
        bFile = new File(bName);
        eName = ePath.concat(".txt");
        eFile = new File(eName);
        readXML();
        encrypt();
    }
    
    //Read XML file
    public void readXML(){
        try{
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docBuilder.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList nList = doc.getElementsByTagName("rsakey");
            String tempE, tempN;
            for(i=0; i<nList.getLength(); i++){
                Node node = nList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element elementValue = (Element) node;
                    //Get e value
                    tempE = elementValue.getElementsByTagName("evalue").item(0).getTextContent();
                    e = new HugeUnsignedInteger(tempE);
                    //Get n value
                    tempN = elementValue.getElementsByTagName("nvalue").item(0).getTextContent();
                    n = new HugeUnsignedInteger(tempN);
                }
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    
    //Encrypt blocked file
    public void encrypt(){
        String tempString = "";
        HugeUnsignedInteger inputNumber;
        HugeUnsignedInteger outputNumber;
        HugeUnsignedInteger tempNumber;
        String resultString;
        String tempString2 = "";
        int intE = Integer.parseInt(e.value);
        //Read each block
        try{
            BufferedReader fRead = new BufferedReader(new FileReader(bFile));
            
            //Output File
            if(eFile.exists()){
                eFile.delete();
            }
            eFile.createNewFile();
            BufferedWriter fWrite = new BufferedWriter(new FileWriter(eFile, true));
            
            //int c;
            while((tempString = fRead.readLine()) != null){
                //Remove leading zero
                StringReader strRead = new StringReader(tempString);
                tempString2 = "";
                int zeroCount = 0;
                try{
                    for(i=0; i<tempString.length(); i++){
                        char cc = (char) strRead.read();
                        if(cc == '0' && zeroCount == 0){
                            zeroCount = 0;
                        }
                        else{
                            zeroCount = 1;
                            tempString2 = tempString2 + cc;
                        }
                    }
                }
                catch(IOException e){
                    System.err.println(e);
                }
                //If 00
                if(tempString2.equals("")){
                    tempString2 = "0";
                }
                
                //Convert to HugeUnsignedInteger
                inputNumber = new HugeUnsignedInteger(tempString2);
                //C=M^e mod n
                outputNumber = new HugeUnsignedInteger("1");  //C = 1
                for(i=0; i<intE; i++){
                  resultString = outputNumber.multiplication(inputNumber);
                  tempNumber = new HugeUnsignedInteger(resultString);
                  resultString = tempNumber.modulus(n);
                  outputNumber = new HugeUnsignedInteger(resultString);
                }
                //Write to file
                fWrite.write(outputNumber.value);
                fWrite.newLine();
            }
            fWrite.close();
            fRead.close();
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    //-----TEST------
    //  public static void main(String[] args){
    //    Encryption ee = new Encryption("rsakey1.txt");
    //  }
    
    
}