Tan Le (tle51) & Janki Patel (jpate53)
CS 342 - Project 3: RSA Encryption/Decryption
ReadMe

We got the algorithm to check for prime number from the following website:
http://www.mkyong.com/java/how-to-determine-a-prime-number-in-java/

We used the following website to get the algorithm for GCD:
http://introcs.cs.princeton.edu/java/23recursion/Euclid.java.html

How to Use This Program:
- Run the program using Main.java class.
- User must enter file name without any file extensions.
  (e.g. “id_rsa.pub”)
- There are 2 text fields provided to enter prime numbers. 
- If you don’t enter the prime numbers, and select “Create Key” option from the RSA dropdown menu, 
  then the computer will generate two prime numbers from the text file called “primeNumbers.rsc”.
  This file would be located in the same directory as the code.
	- Then the user is prompt to enter file names of public and private key. 
- One the key has been created, you can make a block file.
	- First, the user is prompt to enter a file name that contains a message that needs to be blocked.
	- Then, the user is prompt to enter a file name where the block will be saved.
	- Finally, the user is asked to enter a block size. Since the block size is deepened on the length 	  of the prime number, we have given some suggestions when the user is prompted for the block size.
- When the “Encrypt” menu is selected, the user is asked to enter a file name of the public key.
  User is also asked to enter file name to save the encrypted block. 
- To decrypt a file, enter a file of of the private key and file name of the encrypted block 
  and a file name to store the decrypted block. 
- To unblock the decryption file, enter the file name of the decrypted block. 
