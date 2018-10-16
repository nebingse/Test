/**
 * Your implementation of the LookupInterface.  The only public methods
 * in this class should be the ones that implement the interface.  You
 * should write as many other private methods as needed.  Of course, you
 * should also have a public constructor.
 * 
 * @author Susan Nebinger
 */
  
 
public class StudentLookup implements LookupInterface{
	
	private HashMap map= new HashMap();
	private ReverseMap rMap= new ReverseMap();

	@Override
	public void addString(int amount, String s) {
		int oldCount = lookupCount(s);
		map.add(s, amount);
		int newCount = lookupCount(s);
		rMap.add(oldCount, newCount, s);
	}

	@Override
	public int lookupCount(String s) {
		return map.frequency(s);
	}

	@Override
	public String lookupPopularity(int n) {
		return rMap.pop(n);
	}

	@Override
	public int numEntries() {
		return map.getSize();
	}
    
}
