import java.util.ArrayList;

/**
 * Susan Nebinger
 * CSE 274
 * Description: to have the nth popular term time efficiency as high
 *              as possible. 
 */
public interface ReverseLookUp {
	
	/**
	 * Adds an entry based on the number of times a word appears.
	 * @param key number of times a word appears
	 * @param s Arraylist of words with the same key
	 */
	public void add(int oldCount, int newCount, String s);
	
	/**
	 * Is the map empty?
	 * @return true if the map is empty, false otherwise
	 */
	public boolean isEmpty();
	
	
	/**
	 * Gets the word that is nth popular
	 * @param term degree of popularity
	 * @return The first string in the alphabetically sorted
	 *         ArrayList from the term position (tie is broken
	 *         alphabetically)
	 */
	public String pop(int term);

}
