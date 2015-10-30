import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class CMSTopn extends TopnStructure{

	final static int SKETCH_DEPTH = 5;
	final static int SKETCH_WIDTH = 1000;
	final static int MULTIPLIER = 1; //Multiplier for number of top items to be kept
	final static int MURMUR_SEED = 304837963;
	long[][] sketch;
	Topn topn;
	
	public CMSTopn(int n) {
		sketch = new long[SKETCH_DEPTH][SKETCH_WIDTH];
		topn = new TopnUnordered(n*MULTIPLIER, sketch);
	}
	
	@Override
	public long add(int value) {
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
			frequency = Math.min(frequency, ++sketch[i][hashIndex]);
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
		
		Collections.sort(sortedTopItems, new Comparator<Item>() {

			@Override
			public int compare(Item o1, Item o2) {
				if(o1.frequency > o2.frequency)
				{
					return -1;
				}
				else if(o1.frequency == o2.frequency)
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
			
		});
		
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

}
