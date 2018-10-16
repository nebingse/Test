import java.util.ArrayList;
/**
 * Susan Nebinger
 * CSE 274
 * Description: to implement the LookUpHasMap interface to be used
 *              in the StudentLookUp class
 */
public class HashMap implements LookUpHashMap{

	private static final int HASHTABLE_SIZE = 256;
	private ArrayList<StringNode> hashmap;
	private int wordCount = 0;
	
	public HashMap() {
		hashmap = new ArrayList<>(HASHTABLE_SIZE);
		
		for (int idx = 0; idx < HASHTABLE_SIZE;idx++) {
			hashmap.add(null);
		}
	}
	
	@Override
	public void add(String key, int val) {
		// get the hash value for the key
		int hash = calculateHornersHashValue(key);
		
		// hash is the index into the map		
        StringNode node = hashmap.get(hash);
		
		if (node == null) {
			// nothing has been added yet...
			node = new StringNode(key,val);

			wordCount++;
			
			hashmap.set(hash, node);
		} else {
			// there is at least one node in our linked list.
			// see if the node list matches the given value
			// on a match, increment the value
			StringNode before= null;
			
			int compare;
			while (node != null) {
				compare = node.compare(key);
				
				if (compare == 0) {
					node.increment(val);
					
					return;
				}
				
				if (compare > 0) {
					if (before == null) {
						
						before = new StringNode(key, val);
						
						
						before.nextNode = node;
						
						// inc the count
						wordCount++;
						
						hashmap.set(hash, node);
						
						return;
					}
					else {
						before.nextNode = new StringNode(key, val);
						before.nextNode.nextNode = node;
						
						// inc the count
						wordCount++;
						
						return;
					}
				}
				
				before = node;
				node = node.nextNode;

			}

			before.nextNode= new StringNode(key, val);
			// inc the count
			wordCount++;
			
		}
		
	}

	/**
	 * Calculates the hashvalue to be used for the Strings
	 * @param str the key to be hashed
	 * @return the hashvalue 
	 */
	private int calculateHornersHashValue(String str) {
		if (str == null) return 0;
		
		// use horners to calculate the hash
		int hashVal = 0;
	    for (int idx = 0; idx < str.length(); idx++) {
	        // For small letters.
	        int letter = str.charAt(idx); 
	        hashVal = (hashVal * 32 + letter) % HASHTABLE_SIZE; // mod
	    }
	    return hashVal;
	}
	
	@Override
	public boolean isEmpty() {
		for (int idx = 0; idx < HASHTABLE_SIZE; idx++) {
			if (hashmap.get(idx) != null) {
				// there is at least one node at this point
				return false;
			}
		}
		// no nodes found
		return true;
	}

	
	@Override
	public int getSize() {
		return wordCount;
	}
	
	/**
	 * Returns the number of times a word appears.
	 */
	public int frequency(String key) {
		int hash = calculateHornersHashValue(key);
		
		if ((hash >= this.HASHTABLE_SIZE) || (hash < 0)) {
			hash++;
		}
		StringNode node = hashmap.get(hash);
		
		if (node == null) {
			// not in hash table
			return 0;
		}
		
		while (node != null) {
			if (node.compare(key) == 0) {
				// match
				return node.frequency;
			}
			
			node = node.nextNode;
		}
		
		return 0;
	}
	
	private class StringNode {
		public String word;
		public int frequency;
		
		public StringNode nextNode;
		
		public StringNode(String k, int v) {
			word = k;
			frequency = v;
			nextNode = null;
		}
		
		public int compare(String key) {
			return word.compareTo(key);
		}
		
		public void increment(int value) {
			frequency += value;
		}
	}
}
