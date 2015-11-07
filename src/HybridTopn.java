import java.util.HashMap;
import java.util.Map;

public class HybridTopn extends TopnStructure{

	final static int SKETCH_DEPTH = Constants.DEPTH;
	final static int SKETCH_WIDTH = Constants.WIDTH;
	final static int MULTIPLIER = Constants.MULTIPLIER; //Multiplier for number of top items to be kept
	final static double RELIABILTY = Constants.RELIABILITY; //Reliability factor for the sketch--can be dynamically set
	final static int MURMUR_SEED = Constants.SEED;
	long[][] sketch;
	
	public HybridTopn(int n) {
		this.n = n;
		sketch = new long[SKETCH_DEPTH][SKETCH_WIDTH];
		topn = new TopnOrdered(n*MULTIPLIER);
	}
	
	@Override
	public long add(int value, long count) {
		boolean inCounters = topn.put(value, count);
		
		if(!inCounters)
		{
			long frequency = Long.MAX_VALUE;
			byte[] key = MurmurHash3.intToByteArray(value);
			MurmurHash3.LongPair out = new MurmurHash3.LongPair();
			MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);

			//XXX Check overflow etc?
			for(int i = 0; i < SKETCH_DEPTH; i++)
			{		
				long hashValue = out.val1 + i * out.val2;
				int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
				hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
				sketch[i][hashIndex] += count;
				frequency = Math.min(frequency, sketch[i][hashIndex]);
			}
			
			frequency *= RELIABILTY;
			Item newItem = new Item(value, frequency);
			Item oldItem = null;
			oldItem = topn.update(newItem);
			
			if(oldItem != null)
			{
				byte[] oldKey = MurmurHash3.intToByteArray(oldItem.value);
				MurmurHash3.LongPair oldOut = new MurmurHash3.LongPair();
				MurmurHash3.murmurhash3_x64_128(oldKey, 0, 4, MURMUR_SEED, oldOut);

				for(int i = 0; i < SKETCH_DEPTH; i++)
				{		
					long hashValue = out.val1 + i * out.val2;
					int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
					hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
					sketch[i][hashIndex] -= frequency;
					
					long oldItemHash = oldOut.val1 + i * oldOut.val2;
					int oldItemIndex = (int)(oldItemHash % SKETCH_WIDTH);
					oldItemIndex = (oldItemIndex < 0) ? oldItemIndex + SKETCH_WIDTH : oldItemIndex;
					sketch[i][oldItemIndex] += oldItem.frequency; //XXX do I need multiplier for this?

				}
			}
		}
		
		return 0;
	}

	@Override
	public TopnStructure union(TopnStructure topnStructure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long frequency(int value) {
		long topnFrequency = ((TopnOrdered)topn).getFrequencyOfItem(value);
		if( topnFrequency != -1)
		{
			return topnFrequency;
		}
		else
		{
			long frequency = Long.MAX_VALUE;
			byte[] key = MurmurHash3.intToByteArray(value);
			MurmurHash3.LongPair out = new MurmurHash3.LongPair();
			MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);

			//XXX Check overflow etc?
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

	@Override
	public String topn() {
		return "Top-n:\n" + topn.toString();
	}

	//XXX Do not forget exceptions if structures is empty or paramaters of structures do not match
	public static TopnStructure unionAll(TopnStructure[] structures) {
		HybridTopn result = new HybridTopn(structures[0].n);
		long[][] currentSketch = result.sketch;
		HashMap<Integer, Long> candidateItems = new HashMap<Integer, Long>();
		
		for( int i = 0; i < structures.length; i++)
		{
			HybridTopn structure = (HybridTopn) structures[i];
			TopnOrdered topn = (TopnOrdered) structure.topn;
			
			//XXX Correct!!! I need to use currentlength in other methods also
			for( Item item : topn.topItems)
			{
				Long frequency = candidateItems.get(item.value);
				if(frequency != null)
				{
					candidateItems.put(item.value, item.frequency + frequency);
				}
				else
				{
					long currentFrequencyFromSketch = Long.MAX_VALUE;
					byte[] key = MurmurHash3.intToByteArray(item.value);
					MurmurHash3.LongPair out = new MurmurHash3.LongPair();
					MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);
					
					//XXX Check overflow etc?
					for(int row = 0; row < SKETCH_DEPTH; row++)
					{		
						long hashValue = out.val1 + row * out.val2;
						int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
						hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
						currentFrequencyFromSketch = Math.min(currentFrequencyFromSketch, currentSketch[row][hashIndex]);
					}
					
					currentFrequencyFromSketch *= RELIABILTY;
					candidateItems.put(item.value, currentFrequencyFromSketch + item.frequency);
					
					for(int row = 0; row < SKETCH_DEPTH; row++)
					{		
						long hashValue = out.val1 + row * out.val2;
						int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
						hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
						currentSketch[row][hashIndex] -= currentFrequencyFromSketch;
					}
				}
			}
			
			for( int row = 0; row < SKETCH_DEPTH; row++)
			{
				for( int col = 0; col < SKETCH_WIDTH; col++)
				{
					currentSketch[row][col] += structure.sketch[row][col];
				}
			}
			
			for (Map.Entry<Integer, Long> entry : candidateItems.entrySet()) {
			    int item = entry.getKey();
			    long frequency = entry.getValue();
			    
			    if(!topn.contains(item))
			    {
			    	long sketchFrequency = Long.MAX_VALUE;
					byte[] key = MurmurHash3.intToByteArray(item);
					MurmurHash3.LongPair out = new MurmurHash3.LongPair();
					MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);
					
					//XXX Check overflow etc?
					for(int row = 0; row < SKETCH_DEPTH; row++)
					{		
						long hashValue = out.val1 + row * out.val2;
						int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
						hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
						sketchFrequency = Math.min(sketchFrequency, structure.sketch[row][hashIndex]);
					}
					
					sketchFrequency *= RELIABILTY;
					candidateItems.put(item, frequency + sketchFrequency);
					for(int row = 0; row < SKETCH_DEPTH; row++)
					{		
						long hashValue = out.val1 + row * out.val2;
						int hashIndex = (int)(hashValue) % SKETCH_WIDTH;
						hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
						currentSketch[row][hashIndex] -= sketchFrequency;
					}
			    }   
			}
		}
		
		for (Map.Entry<Integer, Long> entry : candidateItems.entrySet()) {
		    int item = entry.getKey();
		    long frequency = entry.getValue();
		    
		    result.add(item, frequency);
		}
		
		return result;
	}

}
