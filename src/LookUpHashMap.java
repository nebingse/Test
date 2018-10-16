/**
 * 
 * @author snebing
 *
 */
public interface LookUpHashMap {

	/**
	 * Adds a entry to the hashmap
	 * @param key Search item
	 * @param val Data
	 * Return the previous entry associated with the key (null or a value)
	 */
	public void add(String key, int val);
	
	/**
	 * Determines if the map is empty
	 * @return true if the map is empty
	 */
	public boolean isEmpty();
	
	/**
	 * Gets the size of the hashmap
	 * @return The number of unique entries in the map.
	 */
	public int getSize();

	/**
	 * gets the number of times associated with the given key
	 * @param key
	 * @return
	 */
	public int frequency(String key);
}
