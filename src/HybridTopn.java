import java.util.HashMap;
import java.util.Map;

public class HybridTopn extends TopnStructureWithSketch{

	final static double RELIABILTY = Constants.RELIABILITY; //Reliability factor for the sketch--may be dynamically set
	
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
			
			frequency = updateSketchAndGetFrequency(out, count);
			frequency *= RELIABILTY;
			Item newItem = new Item(value, frequency);
			Item oldItem = topn.update(newItem);
			
			if(oldItem != null)
			{
				updateSketchAndGetFrequency(out, -frequency);

				byte[] oldKey = MurmurHash3.intToByteArray(oldItem.value);
				MurmurHash3.LongPair oldOut = new MurmurHash3.LongPair();
				MurmurHash3.murmurhash3_x64_128(oldKey, 0, 4, MURMUR_SEED, oldOut);
				
				updateSketchAndGetFrequency(oldOut, oldItem.frequency);
			}
		}
		
		return 0;
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
			byte[] key = MurmurHash3.intToByteArray(value);
			MurmurHash3.LongPair out = new MurmurHash3.LongPair();
			MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);
			long frequency = updateSketchAndGetFrequency(out, 0);
			
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
		long[][] aggregateSketch = result.sketch;
		HashMap<Integer, Long> candidateItems = new HashMap<Integer, Long>();
		
		for( int i = 0; i < structures.length; i++)
		{
			HybridTopn structure = (HybridTopn) structures[i];
			TopnOrdered topn = (TopnOrdered) structure.topn;
			
			//Iterate through top items of current structure
			for( int index = 0; index < topn.currentLength; index++)
			{
				Item item = topn.topItems[index];
				Long frequency = candidateItems.get(item.value);
				
				//If the item is already in the candidate list, add the frequency from the current structure
				//else extract frequency from the aggregate sketch, add the new candidate with the new frequency
				//and the aggregate frequency
				if(frequency != null)
				{
					candidateItems.put(item.value, item.frequency + frequency);
				}
				else
				{
					byte[] key = MurmurHash3.intToByteArray(item.value);
					MurmurHash3.LongPair out = new MurmurHash3.LongPair();
					MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);
					long currentFrequencyFromSketch = result.updateSketchAndGetFrequency(out, 0);
					
					currentFrequencyFromSketch *= RELIABILTY;
					candidateItems.put(item.value, currentFrequencyFromSketch + item.frequency);
					result.updateSketchAndGetFrequency(out, -currentFrequencyFromSketch);
				}
			}
			
			//Merge the current sketch with aggregate sketch
			for( int row = 0; row < SKETCH_DEPTH; row++)
			{
				for( int col = 0; col < SKETCH_WIDTH; col++)
				{
					aggregateSketch[row][col] += structure.sketch[row][col];
				}
			}
			
			//Iterate through candidate item list, if an item is not in the current structure's top items,
			//extract the frequency information from its sketch
			for (Map.Entry<Integer, Long> entry : candidateItems.entrySet()) {
			    int item = entry.getKey();
			    long frequency = entry.getValue();
			    
			    if(!topn.contains(item))
			    {
					byte[] key = MurmurHash3.intToByteArray(item);
					MurmurHash3.LongPair out = new MurmurHash3.LongPair();
					MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);
			    	long sketchFrequency = structure.updateSketchAndGetFrequency(out, 0);
		
					sketchFrequency *= RELIABILTY;
					candidateItems.put(item, frequency + sketchFrequency);
					result.updateSketchAndGetFrequency(out, -sketchFrequency);
			    }   
			}
		}
		
		//Iterate through candidate list, try to put if there is place, else add the frequency
		//of disqualified item to the sketch
		for (Map.Entry<Integer, Long> entry : candidateItems.entrySet()) {
		    int item = entry.getKey();
		    long frequency = entry.getValue();
		    
		    boolean inTopn = result.topn.put(item, frequency);
		    
		    if(!inTopn)
		    {
		    	Item newItem = new Item(item, frequency);
		    	Item minItem = result.topn.update(newItem);
		    	if(minItem == null)
		    	{
		    		minItem = newItem;
		    	}
		    	
		    	byte[] key = MurmurHash3.intToByteArray(minItem.value);
				MurmurHash3.LongPair out = new MurmurHash3.LongPair();
				MurmurHash3.murmurhash3_x64_128(key, 0, 4, MURMUR_SEED, out);
				result.updateSketchAndGetFrequency(out, minItem.frequency);
		    }
		}
		
		return result;
	}

}
