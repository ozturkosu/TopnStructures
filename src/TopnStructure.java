/*
 * Interface for the structures which is used for top-n queries
 */
public abstract class TopnStructure {
	int n; //Number of the most frequent items to be kept
	Topn topn; //Structure for top items
	final int MULTIPLIER = Constants.MULTIPLIER; //Multiplier for number of top items to be kept
	
	//To insert new item to the structure
	public abstract long add(int value, long count);
		
	//To get frequency of an item
	public abstract long frequency(int value);
	
	//To get the most n frequent items
	public abstract String topn();
	
	//XXX Hash function and random constants must be checked in terms of independence and randomness
	//XXX old hash function?? Which to use?
	public static long hash31(long a, long b, long x)
	{

	  long result;
	  long lresult;  

	  result=(a * x) + b;
	  result = ((result >> 31) + result) & 2147483647;
	  lresult=(long) result; 
	  
	  return(lresult);
	}
}
