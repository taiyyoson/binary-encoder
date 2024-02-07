//importing
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;	

//exceptions
import java.io.IOException;

class Encode {
	//class/static variables
	static int illegalChar2 = 0;
	//testIllegal method takes a char and the FileReader, uses recursion to check for illegal characters (if consecutive)
	//if there are illegal characters, skip over them
	public int testIllegal(int illegalChar, FileReader file) throws IOException {
		//if within legal range, return (base case)
		if (illegalChar >= '\u0007' && illegalChar <= '\u00fe' || illegalChar == -1) {
			return illegalChar;
		}
		//if outside of legal range, skip over it
		if (illegalChar < '\u0007' || illegalChar > '\u00fe') {
			illegalChar2 = file.read();
		}
		//Recursion
		return testIllegal(illegalChar2, file);
	}
	
	public static void main (String[] args) throws IOException {
		try {
			//Make new class object
			Encode ec = new Encode();
			//Setting up Files for access
			File inputStr = new File(args[0]);
			FileWriter writeOutput = new FileWriter(args[1]);
			File codebook = new File("codebook.txt");
			
			//Readers & scanners initialization
			Scanner scan = new Scanner(codebook);
			FileReader input = new FileReader(inputStr);
			
			//Setting up HashMap 
			HashMap<Integer, String> hm = new HashMap<>();
			
			//Entering codebook Huffman code data into HashMap for easy access
			String[] codebookSplit;
			while (scan.hasNextLine()) {
				//Split by colon, then put two values into HashMap
				codebookSplit = scan.nextLine().split(":");
				hm.put(Integer.parseInt(codebookSplit[0]), codebookSplit[1]);
			}
			
			//Writing encoded/compressed data to String output
			int inputChar;
			String output = "";
			while ((inputChar = input.read()) != -1) {
				//Test for illegal chars before converting and putting to output
				inputChar = ec.testIllegal(inputChar, input);
				//if testIllegal returns empty char (-1), do not add it to output
				if (inputChar != -1) {
					output += hm.get(inputChar);
				}
			}
			//Adding EOT character
			output += hm.get(4);
			
			//Writing output to file
			writeOutput.write(output);
			//closing objects
			writeOutput.close();
			scan.close();
			input.close();
			
		}
		//catch FileNotFoundException
		catch (Exception e) {
			System.err.println("Error: File not found. Wrong file name or path");
		}
	}
}
