/** Tan Le (tle51) & Janki Patel (jpate53)
 * CS 342 - Project 3: RSA Encryption/Decryption
 * Rsa Algorithm Class - Implement RSA Algorithm
 */
import java.io.*;
import java.lang.*;
import java.nio.file.Files;

public class RsaAlgorithm
{
    private String e, d, n, p, q, phi;
    private HugeUnsignedInteger hui1;
    private HugeUnsignedInteger hui2;
    private HugeUnsignedInteger one;
    //tags for XML file format
    private String startRsaTag = "<rsakey>";
    private String endRsaTag = "</rsakey>";
    private String fileName;
    private int k = 0;
    
    //Constructor
    public RsaAlgorithm(String prime1, String prime2, String pubFile, String priFile)
    {
        p = prime1;
        q = prime2;
        
        System.out.println("prime1 is " + p);
        System.out.println("prime2 is " + q);
        
        generatePhi();
        System.out.println(phi);
        generateN();
        System.out.println("n in main " + n);
        generateE();
        generateD();
        
        //create public and private keys
        createPubKey(pubFile);
        createPriKey(priFile);
    }
    
    //phi = (p-1) * (q-1)
    public void generatePhi()
    {
        hui1 = new HugeUnsignedInteger(p);
        hui2 = new HugeUnsignedInteger(q);
        
        //HugeInt object of value 1
        one = new HugeUnsignedInteger("1");
        
        String x = "";
        String y = "";
        try{
            x = hui1.subtraction(one); //p-1
            y = hui2.subtraction(one); //q-1
        }
        catch(SubtractionException e){
            System.err.println("Subtraction result is a negative number");
        }
        
        //HUI for p-1 and q-1 value
        HugeUnsignedInteger p_1 = new HugeUnsignedInteger(x);
        HugeUnsignedInteger p_2 = new HugeUnsignedInteger(y);
        
        //phi = (p-1) * (q-1)
        phi = p_1.multiplication(p_2);
        System.out.println("phi is" + phi);
    }
    
    // n = p * q
    public void generateN()
    {
        hui1 = new HugeUnsignedInteger(p);
        hui2 = new HugeUnsignedInteger(q);
        System.out.println(p + "  " + q);
        System.out.println("HUI1 " + hui1.value);
        System.out.println("HUI2 " + hui2.value);
        
        n = hui1.multiplication(hui2);
        System.out.println("Value of n: " + n);
    }
    
    //Check if the number is GCD using Euclidean Algorithm
    public int isGcd(String s)
    {
        String temp = "0";
        String phiTemp = phi; //set the value of phi to a temp variable
        
        while(!phiTemp.equals("0"))
        {
            //objects for s and phi
            hui1 = new HugeUnsignedInteger(s);
            HugeUnsignedInteger phiHui = new HugeUnsignedInteger(phiTemp);
            temp = phiTemp;
            phiTemp = hui1.modulus(phiHui);   //e%phi
            //System.out.println("after mod call in gcd " + phi);
            s = temp; //update s
        }
        
        return Integer.parseInt(s);
    }
    
    //e is less than n and relative prime to phi
    public void generateE()
    {
        //Assign e to 3 because it is guaranteed to be less than n
        e = "2";
        hui1 = new HugeUnsignedInteger(n);
        one = new HugeUnsignedInteger("1");
        int temp = Integer.parseInt(e);
        
        if(isGcd(e) == 1)
        {
            e = Integer.toString(temp);
        }
        else
        {
            //int i = 2;
            while(isGcd(e) != 1)
            {
                temp = (Integer.parseInt(e)) + 1;
                e = Integer.toString(temp);
                //i++;
            }
        }
        System.out.println("e val is " + e);
    }
    
    //Inverse of e mod phi i.e. (1+k*phi)/e
    //Let k = 1
    public void generateD()
    {
        hui1 = new HugeUnsignedInteger(phi);
        hui2 = new HugeUnsignedInteger(Integer.toString(k)); //HUI k value
        
        String temp = hui1.multiplication(hui2); //Stores k * phi
        //System.out.println("temp in gen D  " + phi + " " +temp);
        
        //HUI obj for 1 and k*phi value
        one = new HugeUnsignedInteger("1");
        HugeUnsignedInteger inverseHUI = new HugeUnsignedInteger(temp);
        
        //compute 1+*k*phi)
        String inverse = inverseHUI.addition(one);
        
        HugeUnsignedInteger iHui = new HugeUnsignedInteger(inverse);
        HugeUnsignedInteger eHui = new HugeUnsignedInteger(e);
        
        while(!iHui.modulus(eHui).equals("0"))
        {
            k += 1;
            HugeUnsignedInteger kHui = new HugeUnsignedInteger(Integer.toString(k));
            
            temp = hui1.multiplication(kHui); //Stores k * phi
            one = new HugeUnsignedInteger("1");
            inverseHUI = new HugeUnsignedInteger(temp);
            inverse = inverseHUI.addition(one);
            iHui = new HugeUnsignedInteger(inverse);
        }
        
        System.out.println("k is "+ k);
        
        //Make numerator and denominator HUI
        HugeUnsignedInteger numerator = new HugeUnsignedInteger(inverse);
        HugeUnsignedInteger denominator = new HugeUnsignedInteger(e);
        
        d = numerator.division(denominator);
    }
    
    //get the value of d
    public String getD()
    {
        return d;
    }
    
    public void createPubKey(String file)
    {
        String fileName = file.concat(".txt");
        System.out.println("N in pub " + n);
        try(BufferedWriter writing = new BufferedWriter(new FileWriter(fileName)))
        {
            //write XML format to the file
            writing.write(startRsaTag);
            writing.newLine();
            //write values of e and n
            writing.write("<evalue>" + e + "</evalue>");
            writing.newLine();
            writing.write("<nvalue>" + n + "</nvalue>");
            writing.newLine();
            writing.write(endRsaTag);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void createPriKey(String file)
    {
        fileName = file.concat(".txt");
        try(BufferedWriter writing = new BufferedWriter(new FileWriter(fileName)))
        {
            //write XML format to the file
            writing.write(startRsaTag);
            writing.newLine();
            //write values of d and n
            writing.write("<dvalue>" + d + "</dvalue>");
            writing.newLine();
            writing.write("<nvalue>" + n + "</nvalue>");
            writing.newLine();
            writing.write(endRsaTag);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
}//End of class
