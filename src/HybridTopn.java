
public class HybridTopn extends TopnStructure{

	final int SKETCH_DEPTH = 5;
	final int SKETCH_WIDTH = 1000;
	final int MULTIPLIER = 1; //Multiplier for number of top items to be kept
	final double RELIABILTY = 1; //Reliability factor for the sketch
	final int MURMUR_SEED = 304837963;
	long[][] sketch;
	Topn topn;
	
	public HybridTopn(int n) {
		sketch = new long[SKETCH_DEPTH][SKETCH_WIDTH];
		topn = new TopnOrdered(n*MULTIPLIER);
	}
	
	@Override
	public long add(int value) {
		boolean inCounters = topn.put(value);
		
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
				frequency = Math.min(frequency, ++sketch[i][hashIndex]);
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
	public long frequency(int item) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String topn() {
		return "Top-n:\n" + topn.toString();
	}

}
