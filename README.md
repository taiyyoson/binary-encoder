# Binary Encoder

## What Does This Project Do? 
binaryEncoder is a project with three functions: it can encode some text file, it can decode an encoded text file, and it can compare two text files to ensure they are the EXACT same with the exception of illegal characters not provided in the ASCII range. All these programs are written in Java. This was a big program that helped me explore binary trees, I/O (input/output) scanning, and a fun project that taught me basic security (how to encode, I use this project as a reference when learning encryption).

## Implementation/Files
While my `src` is messy, I ultimately use 4 files: `HuffTest.java`, `Encode.java `, `Decode.java`, and `codebook.txt`, and in the end they take `test.txt` as input and output `output.txt` to either decode or encode some file. 

`HuffTest.java` is what I wrote first, kind of as an introduction to the project. I throw `ArrayIndexOutOfBoundsException` and `IOException` to catch any errors (since we ARE dealing with file scanning here). In my main method, I open File Readers to scan whatever files are given in the arguments on the command line. The main calls the `testIllegal` method, which deals with illegal characters not in ASCII, so that reading text files runs more smoothly. I then simply use the if condition: 
```
if (inputChar != outputChar) {
					passFail = false; 
					break;
				}
```
This is simply checks if the current char matches or not. If not, it returns that the files are not the same.

`Encode.java` is also a pretty simple program. I first copy `testIllegal` for obvious reasons, so that no illegal characters are interpreted through the codebook. I call a `codebook.txt`, which is formatted so that every legal character has an encoded version. I push `codebook.txt` into a HashMap, so that I can call the encoded version of a character imply by calling a character. This is a Key-Value data structure, which makes accessing specific entries really easy (so we don't have to iterate through the whole thing). Afterwards, I simply write the input file to its encoded output file using the Hashmap (denoted `hm`): Code block below: 

```
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
			//Adding EOT character,
			output += hm.get(4);
```

`Decode.java` is the fun, juicy part. This requires a binary tree. Because, the codebook only uses 0s and 1s to encode a character, so to decode, the logic goes as follows: It parses the encoded file by reading number by number. If the number is a 0, the traversal pointer goes left--and rules out the character denoted by the 1--and continues. Basically, if a sample character--we'll say 'z'--was encoded to `001001`. The traversal pointer would start at the head. It would visit the left node first: if the node holes a value of `1`, it would go to the right node (because our first value is 0). Then, it eliminates all possibilities in the left subtree. This process repeats and eliminates possible characters until it finds the node equal to the encoded character. By this node, we now know the character has to be 'z'. It writes that down, then repeats with the next set of characters.

Here is `Decode.java`'s construction of the binary tree:
```
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
```

And then here is how I use the binary tree:
```
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
```

## Sample Input/Output

<img width="1285" alt="Screenshot 2024-03-09 at 4 06 06 PM" src="https://github.com/taiyyoson/binaryEncoder/assets/123409233/d55d1089-a5bb-40c7-8d46-86a160fd6b63">

Here, `test.txt` is the input file. Notice the illegal characters at the end. When those get fed into `Encode.java`, those do not get encoded. The encoded output is `output.txt`. 

### Notes
Haha apologies for inserting entire code blocks, I know you can see these simply by accessing my src directory. I was just excited to share my work. THANKS for reading!
