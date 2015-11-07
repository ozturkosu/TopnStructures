import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CMSTopn extends TopnStructure{

	final static int SKETCH_DEPTH = Constants.DEPTH;
	final static int SKETCH_WIDTH = Constants.WIDTH;
	final static int MULTIPLIER = Constants.MULTIPLIER; //Multiplier for number of top items to be kept
	final static int MURMUR_SEED = Constants.SEED;
	long[][] sketch;
	Topn topn;
	
	public CMSTopn(int n) {
		this.n = n;
		sketch = new long[SKETCH_DEPTH][SKETCH_WIDTH];
		topn = new TopnUnordered(n*MULTIPLIER, sketch);
	}
	
	@Override
	public long add(int value, long count) {
		long frequency = Long.MAX_VALUE;
		byte[] key = MurmurHash3.intToByteArray(value);
		MurmurHash3.LongPair out = new MurmurHash3.LongPair();
		MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);

		//XXX Check overflows etc?
		for(int i = 0; i < SKETCH_DEPTH; i++)
		{		
			long hashValue = out.val1 + i * out.val2;
			int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
			hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
			sketch[i][hashIndex] += count;
			frequency = Math.min(frequency, sketch[i][hashIndex]);
		}
		
		Item newItem = new Item(value, frequency);
		topn.update(newItem);
		
		return 0;
	}

	@Override
	public TopnStructure union(TopnStructure topnStructure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long frequency(int value) {
		return frequencyFromSketch(value, this.sketch);
	}

	@Override
	public String topn() {
		Set<Integer> topItems = ((TopnUnordered)topn).items;
		Iterator<Integer> topItemsIterator = topItems.iterator();
		ArrayList<Item> sortedTopItems = new ArrayList<Item>();
		
		while(topItemsIterator.hasNext())
		{
			int value = topItemsIterator.next();
			long frequency = frequency(value);
			sortedTopItems.add(new Item(value, frequency));
		}
		
		Collections.sort(sortedTopItems);
		
		String result = "Topn:\n";
		for( int i = 0; i < sortedTopItems.size(); i++)
		{
			result += (i+1) + ") " + sortedTopItems.get(i).toString() + "\n";
		}
		
		return result;
	}
	
	public static long frequencyFromSketch(int value, long[][] sketch)
	{
		long frequency = Long.MAX_VALUE;
		byte[] key = MurmurHash3.intToByteArray(value);
		MurmurHash3.LongPair out = new MurmurHash3.LongPair();
		MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);

		//XXX Check overflows etc?
		for(int i = 0; i < SKETCH_DEPTH; i++)
		{		
			long hashValue = out.val1 + i * out.val2;
			int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
			hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
			frequency = Math.min(frequency, sketch[i][hashIndex]);
		}
		
		return frequency;
	}

	public static TopnStructure unionAll(TopnStructure[] structures) {
		Set<Integer> candidateItems = new HashSet<Integer>();
		CMSTopn result = new CMSTopn(structures[0].n);
		long[][] resultSketch = result.sketch;
		
		for( int i = 0; i < structures.length; i++)
		{
			CMSTopn structure = (CMSTopn) structures[i];
			TopnUnordered topn = (TopnUnordered) structure.topn;
			
			for( int item : topn.items)
			{
				candidateItems.add(item);
			}
			
			for( int row = 0; row < SKETCH_DEPTH; row++)
			{
				for( int col = 0; col < SKETCH_WIDTH; col++)
				{
					resultSketch[row][col] += structure.sketch[row][col]; 
				}
			}
		}
		
		for( int item : candidateItems)
		{
			result.add(item, 0);
		}
		
		return result;
	}

}
