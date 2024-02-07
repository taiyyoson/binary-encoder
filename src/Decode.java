//importing
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;

//class, makes binary tree
class Tree {
	//Member class, makes nodes in tree
	private class Node {
		int value; 
		Node left;
		Node right;
		//Constructor takes int, assigns node's value
		Node (int v) {
			value = v;
			right = null;
			left = null;
		}
		//Method returns value of node
		int value() {
			return value;
		}
	}
	//Create root/head node
	Node root;
	//Set root to null 
	public Tree() {
		root = null;
	}
	//Add method, takes character (int, is in decimal code point format) and String of 1s and 0s
	//Builds tree
	void add(int decimalCode, String symbol) {
		//pointer/current node
		Node pointer = root;
		//split into individual 1s and 0s
		String[] splitSymbol = symbol.split("");
		//if root is null, set to 3 (or any random value, but != 1 || != 0)
		if (pointer == null) {
			pointer = new Node(3);
			root = pointer;
		}
		//for loop, put 1s and 0s from string array splitSymbol into int v
		int v = 0;
		for (int i=0; i<splitSymbol.length; i++) {
			v = Integer.parseInt(splitSymbol[i]);
			//Moving right if current symbol is 1
			if (v == 1) {
				//if the right branch is empty, fill it with int v
				if (pointer.right == null) {
					//if just a 1, put int v
					if (i != splitSymbol.length - 1) {
						pointer.right = new Node(v);
					}
					//if reached final index of input string, put the character num in, create leaf node
					else {
						pointer.right = new Node(decimalCode);
					}
				}
				//if pointer.right is not null, and has a value, just set the pointer to that node
				pointer = pointer.right;
			}
			//Moving left if current symbol is 0 
			//Same as above, but since v == 0, pointer moves to left branch instead
			if (v == 0) {
				if (pointer.left == null) {
					if (i != splitSymbol.length - 1) {
						pointer.left = new Node(v);
					}
					else {
						pointer.left = new Node(decimalCode);
					}
				}
				pointer = pointer.left;
			}
		}
	}
	
	//decoder method: uses built tree to decode some String input
	public String decoder(String input) {
		//Set pointer to root
		Node pointer = root;
		if (pointer == null) {
			pointer = new Node(3);
			root = pointer;
		}
		//split input so each 0s and 1s go through tree
		String[] splitInput = input.split("");
		//Variables
		int v = 0;
		String output = ""; 
		int decimalCode = 0; 
		char toChar;
		
		//for loop, loops through 0s and 1s
		for (int i=0; i < splitInput.length; i++) {
			v = Integer.parseInt(splitInput[i]);
			//if current input is at 1, go through right branch
			if (v == 1) {
				//Check that right branch is not empty (shouldn't be unless at leaf node)
				if (pointer.right != null) {
					//Set pointer to right branch, set new value of node to decimalCode
					pointer = pointer.right;
					decimalCode = pointer.value;
				}
				//if reached leaf node
				if (decimalCode != 0 && decimalCode != 1 && decimalCode != root.value()) {
					//Convert decimalCode to character conversion, add to output string
					toChar = (char)decimalCode;
					output += toChar;
					//Move the pointer back to the root node, begin conversion for next character
					pointer = root;
					//if reached EOT character, break statement and go to end
					if (decimalCode == 4) {
						break;
					}
				}
			}
			//Same as above, but since v == 0, move down to the left branch instead of right branch
			if (v == 0) {
				if (pointer.left != null) {
					pointer = pointer.left;
					decimalCode = pointer.value;
				}
				if (decimalCode != 0 && decimalCode != 1 && decimalCode != root.value()) {
					toChar = (char)decimalCode;
					output += toChar;
					pointer = root;
					if (decimalCode == 4) {
						break;
					}
				}
			}
 		}
		//return final output
		return output;
	}
	
	
	
}
//main class
public class Decode {
	public static void main(String[] args) throws IOException {
		//try catch
		try {
			//Initialize tree object
			Tree tr = new Tree();
			//Create File/FileWriter
			File input = new File(args[0]);
			File codebookFile = new File("codebook.txt");
			FileWriter outputFile = new FileWriter(args[1]);
			//Create scanners
			Scanner scan = new Scanner(codebookFile);
			Scanner scanInput = new Scanner(input);
			//Read codebook file into scanner
			//for loop add each codebook line into binary tree
			//Once loop ends, we have a full built binary tree
			String[] codebook;
			while (scan.hasNextLine()) {
				codebook = scan.nextLine().split(":");
				tr.add(Integer.parseInt(codebook[0]), codebook[1]);
			}
			
			//Run decoder method on input.txt file
			//write to output file
			String output = tr.decoder(scanInput.next());
			outputFile.write(output);
			
			//close
			outputFile.close();
			scan.close();
			scanInput.close();
		}
		//catch exceptions, (namely FileNotFoundException) print to err of specific error
		catch (Exception e) {
			System.err.println("Error: input file name/path not found");
		}
	}
}















 