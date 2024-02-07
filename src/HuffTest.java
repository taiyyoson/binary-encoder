//import
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

class HuffTest {
	//class variables
	public int illegalChar2 = 0;
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
		//recursion
		return testIllegal(illegalChar2, file);
	}
	
	//Throws exceptions
	public static void main(String[] args) throws ArrayIndexOutOfBoundsException, IOException  {	
		//try catch
		try {
			//Create object to access testIllegal(), create files for access
			HuffTest hf = new HuffTest();
			File fileInput = new File(args[0]);
			File decodeOutput = new File(args[1]);
		
			FileReader input = new FileReader(fileInput);
			FileReader output = new FileReader(decodeOutput);
			
			//if boolean is true, output PASS, if not, output FAIL (label which chars and at what index)
			boolean passFail = true;
			//Variables, holds decimal code value of characters called by input/output.read()
			int inputChar = 0;
			int outputChar = 0;
			
			//Hold index values
			int x = 0;
			int y = 0;
			
			//Strings to hold input/output
			String txtInput = "";
			String txtOutput = "";
			
			
			//While loop, read characters until reach end of both input/output files; read files while not empty
			while (inputChar != -1 || outputChar != -1 ) {
				//Read first characters
				inputChar = input.read();
				outputChar = output.read();
				
				//Class method: checks for illegal characters
				inputChar = hf.testIllegal(inputChar, input);
				outputChar = hf.testIllegal(outputChar, output);
		
			
				//if the chars do not match, tester fails and breaks loop
				if (inputChar != outputChar) {
					passFail = false; 
					break;
				}
				
				//Add on to strings, if one char reaches the end(eg inputChar = -1) before the other, then have to subtract index by 1
				//By doing this, fail output won't write the null char, but the last char used
				//prevents ArrayIndexOutOfBoundsException
				txtInput += (char)inputChar;
				txtOutput += (char)outputChar;
				
				//Increment index values
				x++;
				y++;
					
			}
			//Closing FileReaders
			input.close();
			output.close();
			
			//Tests if either inputChar or outputChar is null, if so, use char before null by subtracting index
			if (inputChar == -1) {
				x--;
				inputChar = txtInput.charAt(x);
			}
			if (outputChar == -1) {
				y--;
				outputChar = txtOutput.charAt(y);
			}
			
			//Use boolean to determine pass or fail
			if (passFail) {
				System.out.println("PASS");
			}
			else {
				System.out.println("FAIL input " + (char)inputChar + " @ " + x + " output " + (char)outputChar + " @ " + y);
			}
		}
		//catch FileNotFoundException, give resulting error
		catch (Exception e) {
			System.err.println("Error: File not found. Wrong file name or path");
		}
	}
	
}












