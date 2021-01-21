/* Feier Zhang, 8589976 */

import java.io.*;
import java.util.*;

 // Hash Table

class Main{
	static int testCase;
	static int n;              // Number of cars;
	static int bestK;
	static int hashTableSize;
	static int ferryL = 0;     // Ferry Length

	static int[] totalBestK;
	static int[] currX;
	static int[] bestX;
	static boolean[][] state;
	static int[] stateTable; 

	static ArrayList<int[]> totalBestX; 			   
	static List<Integer> carL; // List of each car length
	
	static String firstLine;
	static String fileFirstLine;


	static int hashFuntion(int key, int value)
	{
		return (key+value) % hashTableSize;
	}


	static void insert(int key, int value)
	{
		int hashValue = key + value;
		int hashPos = hashFuntion(key, value);
		while(stateTable[hashPos] != -1)
		{
			++hashPos;
			hashPos %= hashTableSize; 
		}
		stateTable[hashPos] = hashValue;
	}


	static boolean find(int key, int value)
	{
		int hashValue = key + value;
		int hashPos = hashFuntion(key, value);

		while(stateTable[hashPos] != -1)
		{
			if(stateTable[hashPos] == hashValue)
			{
				return true;
			}
			++hashPos;
			hashPos %= hashTableSize;
		}
		return false;
	}


	static void backtrackSolve(int currK, int currS) throws IOException
	{
	
		if(currK > bestK)
		{
			bestK = currK;			
			bestX = new int[bestK];

			for(int i=0; i<bestK; i++)
			{					
				bestX[i] = currX[i]; 
			}
		}

		if(currK < n)
		{
			int currCarSum = 0;
			for(int i=0; i<currK; i++)
			{
				currCarSum = currCarSum + carL.get(i);
			}
			
			// Add car to left
			int row = currK + 1;
			int column = currS - carL.get(currK);
 
			if(currS >= carL.get(currK) && find(row, column) == false)
			{
				currX[currK] = 1;
				int newS = currS - carL.get(currK);
				backtrackSolve(currK+1, newS);
				//state[currK+1][newS] = true;
				insert(row, newS);	
						
			}
			
			// Add car to right
			if((ferryL - currCarSum + ferryL - currS) >= carL.get(currK) && find(row, currS) == false)
			{
				currX[currK] = 0;
				backtrackSolve(currK+1, currS);
				//state[currK+1][currS] = true;
				insert(row, currS);
			}
		}
	}


	static void readInput(String line, BufferedReader reader) throws IOException
	{
		testCase = Integer.parseInt(line);  // FirstLine -- Number of Test Case
		int count = 0;
		totalBestK= new int[testCase];
		totalBestX = new ArrayList<>();

		while(count<testCase)
		{
			if(reader.readLine().equals(""))
			{
				count=count+1;
				String ferryLStr = reader.readLine();
				ferryLStr = ferryLStr.replace(" ", "");
				ferryL = Integer.parseInt(ferryLStr);  // The length of Ferry
				ferryL = ferryL * 100;
			}

			carL= new ArrayList<>(); // List of each car length
			boolean flag = true;
	
			while(flag)
			{
				String eachCarL = reader.readLine();  // The length of each car
				if(eachCarL.equals(""))
				{
					continue;
				}
				eachCarL = eachCarL.replace(" ", "");
				carL.add(Integer.parseInt(eachCarL));

				if(eachCarL.equals("0"))
				{
					carL.remove(carL.size()-1);
					n = carL.size();  // The total number of cars
					break;
				}
			}

			/* Initialize variables */
			hashTableSize = n + ferryL;
			stateTable = new int[hashTableSize];
			Arrays.fill(stateTable, -1);

			bestK = -1;

			state = new boolean[n+1][ferryL+1];
			for(boolean row[]: state)
			{
				Arrays.fill(row, false);
			}

			currX = new int[n];
			for(int i=0; i<n; i++)
			{
				currX[i] = -1;
			}
			
			backtrackSolve(0, ferryL);
			
			totalBestK[count-1] = bestX.length;
			totalBestX.add(bestX);
			
			if(count == testCase)
			{
				break;
			}			
		}
	}


	static void printSolution() throws IOException
	{
		for(int i=0; i<testCase; i++)
		{
			System.out.println(totalBestK[i]);
			int[] str = totalBestX.get(i);
			for(int k=0; k<str.length; k++)
			{
				if(str[k] == 1)
				{
					System.out.println("port");
				}
				else if(str[k] == 0)
				{
					System.out.println("starboard");
				}
			}

			if(i<testCase-1)
			{
				System.out.println();
			}			
		}
	}


	static void writeSolutionToFile(String inputfilename, String outputFileName) throws IOException
	{
		String content = "";
		File directory = new File("");
		outputFileName = directory.getCanonicalPath() + "/" + outputFileName;
		File outputFile = new File(outputFileName);

		if(!outputFile.exists())
		{
			outputFile.createNewFile();
		}

		FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for(int i=0; i<testCase; i++)
		{
			String strBestK = String.valueOf(totalBestK[i]);
			bw.write(strBestK + "\n");

			int[] str = totalBestX.get(i);
			
			for(int k=0; k<str.length; k++)
			{
				if(str[k] == 1)
				{
					bw.write("port" + "\n");
				}
				else if(str[k] == 0)
				{
					bw.write("starboard" + "\n");
				}
			}

			if(i<testCase-1)
			{
				bw.write("\n");
			}
		}

		bw.close();
	}


	public static void main(String[]args) throws IOException 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String currentLine = "";
		String fileCurrentLine = "";
		
		if((currentLine = br.readLine()) != null)
		{
			firstLine = currentLine;
		}

		/* When inputs are datas, directly print the solution in command line */
		if(firstLine.length() <= 4)
		{
			firstLine.replace(" ", "");
			readInput(firstLine, br);
			printSolution();
		}

		/* When input is only an input file name. Ex: "input1.txt" 
		 * Directly print the solution in command line.
		 */
		else if(firstLine.length() > 4 && firstLine.length() <= 12)
		{
			String filename = firstLine;
			File directory = new File("");
			filename = directory.getCanonicalPath() + "/" + filename;
			FileReader fr1 = new FileReader(filename);
			BufferedReader reader1 = new BufferedReader(new FileReader(filename));

			if((fileCurrentLine = reader1.readLine()) != null)
			{
				fileFirstLine = fileCurrentLine;
				fileFirstLine.replace(" ", "");
			}

			readInput(fileFirstLine, reader1);
			printSolution();
		
		}

		/* When inputs are input file and output file. Ex: "input1.txt output1.txt".
		 * Write the solution to the output file.	
		 */
		else
		{
			String[] filenames = firstLine.split("\\s+");
			String inputFileName = filenames[0];
			String outputFileName = filenames[1];

			File directory = new File("");
			inputFileName = directory.getCanonicalPath() + "/" + inputFileName;
			FileReader fr1 = new FileReader(inputFileName);
			BufferedReader reader1 = new BufferedReader(new FileReader(inputFileName));

			if((fileCurrentLine = reader1.readLine()) != null)
			{
				fileFirstLine = fileCurrentLine;
				fileFirstLine.replace(" ", "");
			}

			readInput(fileFirstLine, reader1);
			writeSolutionToFile(inputFileName, outputFileName);		
		}
	}
}