package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;

/* To-do list:
 *  Add the testScoring in the decryptor
 *  Let producers put stuff into a queue and then consumers to take it off
 *  Look at tutorial on moodle (Git)
 * 	Make it fancy at the end
 */
public class Runner {
	public static void main(String [] args) throws Exception
	{
		Scanner console = new Scanner(System.in);
		String file = "4grams.txt";
		System.out.print("Enter text to be ciphered: ");
		String plainText = console.nextLine();
		
		//String book = new Scanner(new File("3lpigs.txt")).useDelimiter("\\Z").next();
		String cypherText = new RailFence().encrypt(plainText, 4);	
		
		System.out.println();
		//System.out.println(cypherText);
		System.out.println();
		System.out.println("The highest possible key (cipherText/2) is " + (cypherText.length()/2));
		System.out.println("Can't have a key of less than 2 (not encrypted)");
		System.out.println("Decrypting for every key starting from 2 and ending with " + (cypherText.length()/2));
		System.out.println();
		
		Map<String, Double> map = new ConcurrentHashMap<String, Double>();
		map = FileParser.parse(file);
		
		CypherBreaker cb = new CypherBreaker(cypherText, map);
		
		console.close();
		/*String encryption = new RailFence().encrypt(input, 3);
		System.out.println(encryption);
		
		String decryption = new RailFence().decrypt(encryption, 3);
		System.out.println(decryption);*/
	}
}
