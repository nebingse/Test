import java.io.*;
import java.util.*;

/**
 *
 * @author skonlc brinkmwj and karroje and krumpenj
 * @Date : 2009-10-04, 2011-03-10, 2015-09-27, 2017-04-22, 2017-11-27 Sources :
 *       All code is original Purpose : The purpose of this file is to do some
 *       VERY rudimentary timing of your methods. There are ways to make these
 *       tests better. For example, test 4 randomly calls some methods, but
 *       never randomly calls lookupCount.
 */
public class Controller {

	/*
	 * The only purpose of main() is to call processFile with increasingly
	 * larger and larger files. A larger file will give a more accurate sense of
	 * how fast your methods are, but at some point it may take so long to do
	 * the lookupPopularity, that we aren't willing to wait for it to finish.
	 */

	static Random rng;

	public static void main(String[] args) {

		/*
		 * These files are books from project Gutenberg. I have provided the
		 * inputs, as well as my outputs in the starter files
		 */
		rng = new Random(42);

		// Try changing 1 to 2, 3, 4, 5, 6, 7. Each is a larger set of strings.
		timeTests("8.txt");
	}
	
	public static ArrayList<String> readFile(String fileName) {
		ArrayList<String> wordList = new ArrayList<>();
		String line;
		try {
			FileReader fileReader = new FileReader(fileName);
			try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
				while ((line = bufferedReader.readLine()) != null) {
					line = line.replaceAll("[^A-Za-z0-9 ]", ""); // Remove all
																	// non-alphanumeric/whitespace
																	// characters
					for (String word : line.split(" "))
						if (!word.isEmpty())
							wordList.add(word);
				}
			}
		} catch (FileNotFoundException ex) {
			System.err.println("Unable to open file '" + fileName + "'");
			System.exit(1);
		} catch (IOException ex) {
			System.err.println("Error reading file '" + fileName + "'");
			System.exit(1);
		}
		return wordList;
	}

	/**
	 * Find the total time to perform the various methods.
	 * 
	 * @param wordList
	 * @return average time per insert
	 */
	public static void timeTests(String file) {
		ArrayList<String> wordList = readFile(file);
		System.out.println(wordList.size());
		

		// Average time for insert
		long startTime = System.nanoTime();
		LookupInterface tr = new StudentLookup();

		for (String w : wordList) {
			tr.addString(1, w);
		}
		
		System.out.println("Implementation: " + tr.numEntries());

		long endTime = System.nanoTime();
		double test1 = (double) ((endTime - startTime) / 1000000.0) / (double) wordList.size();
		System.out.println("Test 1: " + test1 + " milliseconds / addString");

		// Average time for lookupCount
		startTime = System.nanoTime();
		for (String w : wordList)
			tr.lookupCount(w);
		endTime = System.nanoTime();
		double test2 = (double) ((endTime - startTime) / 1000000.0) / (double) wordList.size();
		System.out.println("Test 2: " + test2 + " milliseconds / lookupCount");

		// Average time for lookupPopularity();
		int n = tr.numEntries();
		startTime = System.nanoTime();
		for (int i = 0; i < n; i++)
			tr.lookupPopularity(i);
		endTime = System.nanoTime();
		double test3 = (double) ((endTime - startTime) / 1000000.0) / (double) n;
		System.out.println("Test 3: " + test3 + " milliseconds / lookupPopularity");

		// Average time per operation when mixing operations.  There is room here for improvement.
		tr = new StudentLookup();
		startTime = System.nanoTime();
		n = 0;
		for (int i = 0; i < wordList.size(); i++) {
			tr.addString(1, wordList.get(i));
			if (rng.nextDouble() < 0.2)
				n = tr.numEntries();
			if (rng.nextDouble() < 0.2) {
				tr.lookupPopularity(rng.nextInt(Math.max(n, 1)));
			}
		}
		endTime = System.nanoTime();
		double test4 = (endTime - startTime) / 1000000000.0;
		System.out.println("Test 4: " + test4 + " seconds (total)");
		
		System.out.println(tr.lookupPopularity(0));
		System.out.println(tr.lookupCount("a"));

	}

}  
