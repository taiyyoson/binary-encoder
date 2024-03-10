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

### Sample Input/Output

<img width="1285" alt="Screenshot 2024-03-09 at 4 06 06 PM" src="https://github.com/taiyyoson/binaryEncoder/assets/123409233/d55d1089-a5bb-40c7-8d46-86a160fd6b63">

Here, `test.txt` is the input file. Notice the illegal characters at the end. When those get fed into `Encode.java`, those do not get encoded. The encoded output is `output.txt`. 

