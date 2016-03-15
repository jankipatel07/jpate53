/** Tan Le (tle51) & Janki Patel (jpate53)
 * CS 342 - Project 3: RSA Encryption/Decryption
 * Huge-Unsigned-Integer Class - Hold very large unsigned integer values and include operations
 */
import java.io.*;
import java.lang.*;

public class HugeUnsignedInteger{
    public String value;
    public int[] arr;
    public int numDigit;
    private int i, j;
    //Division
    private String tempResult;
    private HugeUnsignedInteger tempArr2;
    
    //Constructor
    public HugeUnsignedInteger(String input){
        value = input;
        toArray();
    }
    
    //Covert value to array
    public void toArray(){
        numDigit = value.length();  //Count number of digits
        arr = new int[numDigit];  //Dynamically allocate array space
        
        //Store value to array
        StringReader strRead = new StringReader(value);
        try{
            for(i=numDigit-1; i>=0; i--){
                char c = (char) strRead.read();
                arr[i] = Character.getNumericValue(c);
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    //Addition Operation
    public String addition(HugeUnsignedInteger value2){
        String result = "";
        int tempSize;
        int tempCount;
        //Check for the larger array size of the two operands
        if(numDigit > value2.numDigit){
            tempSize = numDigit;
        }
        else{
            tempSize = value2.numDigit;
        }
        //Initialize temp array to zero
        int[] tempArr = new int[tempSize+1];
        for(i=0; i<tempSize+1; i++){
            tempArr[i] = 0;
        }
        //Add two large int array together
        j=0;
        while(j < value2.numDigit || j < numDigit){
            //If end of array1 -> copy rest of array2 contents
            if(j >= numDigit){
                tempArr[j] = tempArr[j] + value2.arr[j];
                if(tempArr[j] >= 10){
                    tempArr[j] = tempArr[j] - 10;
                    tempArr[j+1] = tempArr[j+1] + 1;
                }
            }
            //If end of array2 -> copy rest of array1 contents
            else if(j >= value2.numDigit){
                tempArr[j] = tempArr[j] + arr[j];
                if(tempArr[j] >= 10){
                    tempArr[j] = tempArr[j] - 10;
                    tempArr[j+1] = tempArr[j+1] + 1;
                }
            }
            else{
                int tempAdd = arr[j] + value2.arr[j];
                //Addition result is less than 10
                if(tempAdd < 10){
                    int tempAdd2 = tempArr[j] + tempAdd;
                    if(tempAdd2 < 10){
                        tempArr[j] = tempAdd2;
                    }
                    else{
                        tempArr[j] = tempAdd2 - 10;
                        tempArr[j+1] = tempArr[j+1] + 1;
                    }
                }
                else{
                    tempArr[j] = tempArr[j] + (tempAdd - 10);
                    tempArr[j+1] = tempArr[j+1] + 1;
                }
            }
            
            for(i=0; i<tempSize; i++){
                
            }
            
            j++;
        }
        //If there is an extra leading zero in the tempArr
        if(tempArr[tempSize] == 0){
            //Remove the extra space
            tempCount = tempSize;
        }
        else{
            tempCount = tempSize+1;
        }
        //Convert int array to string
        for(i=tempCount-1; i>=0; i--){
            result = result + Integer.toString(tempArr[i]);
        }
        
        return result;
    }
    
    //Subtraction Operation
    public String subtraction(HugeUnsignedInteger value2) throws SubtractionException{
        String result = "";
        int tempSize;
        int tempCount;
        //Check for the larger array of the two operands
        if(numDigit > value2.numDigit){
            tempSize = numDigit;
        }
        else{
            tempSize = value2.numDigit;
        }
        //Copy array1 to tempArr
        int[] tempArr = new int[tempSize+1];
        for(i=0; i<tempSize+1; i++){
            if(i < numDigit){
                tempArr[i] = arr[i];
            }
            //Set remaining spaces of tempArr to 0
            else{
                tempArr[i] = 0;
            }
        }
        //Copy array2 to subArr
        int[] subArr = new int[tempSize+1];
        for(i=0; i<tempSize+1; i++){
            if(i < value2.numDigit){
                subArr[i] = value2.arr[i];
            }
            else{
                subArr[i] = 0;
            }
        }
        //Subtract two large int array together
        j=0;
        while(j < tempSize){
            int tempSub = tempArr[j] - subArr[j];
            //Normal subtraction
            if(tempSub >= 0){
                tempArr[j] = tempSub;
            }
            //Need to borrow
            else{
                tempArr[j] = (tempArr[j] + 10) - subArr[j];
                //Check if next number is a 0
                if(tempArr[j+1] < 0){
                    //Borrow from next number and set to 9
                    tempArr[j+1] = 9;
                    tempArr[j+2] = tempArr[j+2] - 1;
                }
                else{
                    //Normal borrowing
                    tempArr[j+1] = tempArr[j+1] - 1;
                }
            }
            j++;
        }
        //Check if negative number
        if(tempArr[tempSize] < 0){
            throw new SubtractionException("Number is negative");
        }
        else{
            //If have leading zero
            tempCount = numDigit;
            for(i=numDigit-1; i>=0; i--){
                if(tempArr[i] == 0){
                    tempCount = tempCount-1;
                }
                else{
                    break;
                }
            }
            for(i=tempCount-1; i>=0; i--){
                result = result + Integer.toString(tempArr[i]);
            }
        }
        //Check if array is all zero
        int zeroCount = 0;
        for(i=0; i<tempCount; i++){
            if(tempArr[i] == 0){
                zeroCount++;
            }
        }
        //If all number in array is zero -> set to zero
        if(zeroCount == tempCount){
            result = "0";
        }
        return result;
    }
    
    //Multiplication Operation
    public String multiplication(HugeUnsignedInteger value2){
        String result = "";
        int tempSize;  //Size of product1
        int tempCount;  //Size for result string
        int productSize;  //Size of product2
        int[] tempArr;
        int[] productArr;
        //Determine the larger digit number
        if(numDigit >= value2.numDigit){
            tempSize = numDigit;
            //Copy array1 to tempArr
            tempArr = new int[tempSize];
            for(i=0; i<tempSize; i++){
                tempArr[i] = arr[i];
            }
            //Copy array2 to productArr
            productSize = value2.numDigit;
            productArr = new int[value2.numDigit];
            for(i=0; i< value2.numDigit; i++){
                productArr[i] = value2.arr[i];
            }
        }
        else{
            tempSize = value2.numDigit;
            //Copy array2 to tempArr
            tempArr = new int[tempSize];
            for(i=0; i<tempSize; i++){
                tempArr[i] = value2.arr[i];
            }
            //Copy array1 to productArr
            productSize = numDigit;
            productArr = new int[numDigit];
            for(i=0; i<numDigit; i++){
                productArr[i] = arr[i];
            }
        }
        //Initialize resultArr
        int[] resultArr = new int[tempSize+productSize];
        for(i=0; i<tempSize+productSize; i++){
            resultArr[i] = 0;
        }
        //Multiply two array (tempArr x productArr)
        for(i=0; i<productSize; i++){  //Go thru productArr
            for(j=0; j<tempSize; j++){  //Go thru tempArr
                int tempProduct = productArr[i] * tempArr[j];
                if(tempProduct < 10){
                    int tempAddition = resultArr[i+j] + tempProduct;
                    if(tempAddition < 10){
                        resultArr[i+j] = tempAddition;
                    }
                    else{
                        resultArr[i+j] = tempAddition - 10;
                        resultArr[i+j+1] = resultArr[i+j+1] + 1;
                    }
                }
                else{
                    //Check if need to carry
                    int tempAdd = resultArr[i+j] + (tempProduct % 10);
                    if(tempAdd < 10){
                        resultArr[i+j] = tempAdd;
                    }
                    else{
                        resultArr[i+j] = tempAdd - 10;
                        resultArr[i+j+1] = resultArr[i+j+1] + 1;
                    }
                    int tempAdd2 = resultArr[i+j+1] + (tempProduct / 10 % 10);
                    if(tempAdd2 < 10){
                        resultArr[i+j+1] = tempAdd2;
                    }
                    else{
                        resultArr[i+j+1] = tempAdd2 - 10;
                        resultArr[i+j+2] =  resultArr[i+j+2] + 1;
                    }
                }
            }
        }
        //Remove leading zero
        if(resultArr[tempSize+productSize-1] == 0){
            tempCount = tempSize+productSize-1;
        }
        else{
            tempCount = tempSize+productSize;
        }
        //Convert int array to string
        for(i=tempCount-1; i>=0; i--){
            result = result + Integer.toString(resultArr[i]);
        }
        //Multiplying by 0
        if((tempArr[tempSize-1] == 0 && tempSize == 1) || (productArr[productSize-1] == 0 && productSize == 1)){
            result = "0";
        }
        
        return result;
    }
    
    //Division
    public String division(HugeUnsignedInteger value2){
        String result = "";
        HugeUnsignedInteger tempInt;
        HugeUnsignedInteger tempSub;  //Used for looping subtraction
        String subString = "";  //Used for looping subtraction
        String pullDown = "";  //Might need to make it private global
        int[] quotientArr = new int[numDigit];  //Same lenght as dividend
        
        //Initialize quotient array
        for(i=0; i<numDigit; i++){
            quotientArr[i] = 0;
        }
        
        //Check if array1 is 0
        if(arr[0] == 0 && numDigit == 1){
            result = "0";
            return result;
        }
        
        //Check if divisor is greater than dividend
        if(lessThan(value2) == 1){
            result = "0";
            return result;
        }
        
        //Division Process
        for(i=numDigit-1; i>=0; i--){
            //System.out.println("STEP 1");
            //Pull down a number from dividend
            pullDown = pullDown + Integer.toString(arr[i]);
            //System.out.println("FIRST:" + pullDown);
            tempInt = new HugeUnsignedInteger(pullDown);
            //Pulldown should not be less than divisor
            if(tempInt.numDigit >= value2.numDigit){
                //System.out.println("STEP 2");
                //If equal length - should dividend should not be less than divisor in value
                if(tempInt.equalTo(value2) == 1 || tempInt.greaterThan(value2)== 1){
                    //Looping subtraction
                    while(true){
                        try{
                            //System.out.println("STEP 3");
                            //Result of the subtraction
                            subString = tempInt.subtraction(value2);
                            tempSub = new HugeUnsignedInteger(subString);
                            //Increment quotient answer
                            quotientArr[i] = quotientArr[i] + 1;
                            //Check if subtraction is still avaliable
                            if(tempSub.lessThan(value2) == 1){
                                //Set pullDown string
                                pullDown = tempSub.value;
                                //System.out.println("Remainder: " + pullDown);
                                break;
                            }
                            tempInt = new HugeUnsignedInteger(subString);
                        }
                        catch(SubtractionException e){
                            //System.out.println("Caught Exception");
                            break;
                            //System.out.println("Subtraction is negative");
                        }
                    }
                }
            }
        }
        //Convert array to string
        int convertSize = numDigit;
        //Remove leading 0
        for(i=numDigit-1; i>=0; i--){
            if(quotientArr[i] == 0){
                convertSize--;
            }
            else{
                break;
            }
        }
        for(i=convertSize-1; i>=0; i--){
            result = result + Integer.toString(quotientArr[i]);
        }
        return result;
    }
    
    
    //Modulus Operation
    public String modulus(HugeUnsignedInteger value2){
        String tempDivide = division(value2);
        //System.out.println(tempDivide);
        HugeUnsignedInteger x1 = new HugeUnsignedInteger(tempDivide);
        String result = "";
        String tempMultiply = value2.multiplication(x1);
        //System.out.println(tempMultiply);
        x1 = new HugeUnsignedInteger(tempMultiply);
        try{
            result = subtraction(x1);
        }
        catch(SubtractionException e){
            //System.out.print("Negative subtraction");
        }
        return result;
    }
    
    //Relational Operation - Equal To
    public int equalTo(HugeUnsignedInteger value2){
        if(numDigit == value2.numDigit){
            for(i=numDigit-1; i>=0; i--){
                if(arr[i] != value2.arr[i]){
                    return 0;
                }
            }
            return 1;  //Equal
        }
        return 0;  //Not equal
    }
    
    //Relational Operation - Greater Than
    public int greaterThan(HugeUnsignedInteger value2){
        if(numDigit > value2.numDigit){
            return 1;
        }
        else if(numDigit == value2.numDigit){
            for(i=numDigit-1; i>=0; i--){
                if(arr[i] > value2.arr[i]){
                    return 1;
                }
            }
        }
        else{
            return 0;
        }
        return 0;
    }
    
    //Relational Operation - Less than
    public int lessThan(HugeUnsignedInteger value2){
        if(numDigit < value2.numDigit){
            return 1;
        }
        else if(numDigit == value2.numDigit){
            for(i=numDigit-1; i>=0; i--){
                if(arr[i] > value2.arr[i]){
                    return 0;
                }
                else if(arr[i] < value2.arr[i]){
                    return 1;
                }
            }
            return 0;
        }
        else{
            return 0;
        }
    }
    
    //------------Test----------------
    public int getDigit(){
        return numDigit;
    }
    public void printValue(){
        System.out.println(value);
        for(i=0; i<numDigit; i++){
            System.out.print(arr[i]);
        }
    }
    //--------------------------------
    
}  //End of class

//Subtraction Exception
class SubtractionException extends Exception{
    public SubtractionException(String msg){
        super(msg);
    }
}