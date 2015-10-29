import java.util.Random;

public class HybridTopn extends TopnStructure{

	final int SKETCH_DEPTH = 5;
	final int SKETCH_WIDTH = 1000;
	final int MULTIPLIER = 1; //Multiplier for number of top items to be kept
	final double RELIABILTY = 1; //Reliability factor for the sketch
	long[][] sketch;
	Topn topn;
	long[] hasha, hashb;
	
	public HybridTopn(int n) {
		sketch = new long[SKETCH_DEPTH][SKETCH_WIDTH];
		topn = new TopnOrdered(n*MULTIPLIER);
		hasha = new long[SKETCH_DEPTH];
		hashb = new long[SKETCH_DEPTH];
		Random random = new Random();
		
		//XXX These have to be fixed later on because of mergebility
		for( int i = 0; i < SKETCH_DEPTH; i++)
		{
			hasha[i] = random.nextLong();
			hashb[i] = random.nextLong();
		}
	}
	
	@Override
	public long add(int value) {
		boolean inCounters = topn.put(value);
		
		if(!inCounters)
		{
			long frequency = Long.MAX_VALUE;
			
			
			for(int i = 0; i < SKETCH_DEPTH; i++)
			{		
				long hashValue = hash31(hasha[i], hashb[i], value);
				int hashIndex = (int)(hashValue % SKETCH_WIDTH);
				frequency = Math.min(frequency, ++sketch[i][hashIndex]);
			}
			
			frequency *= RELIABILTY;
			Item newItem = new Item(value, frequency);
			Item oldItem = null;
			
			try {
				oldItem = topn.update(newItem);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(oldItem != null)
			{
				for(int i = 0; i < SKETCH_DEPTH; i++)
				{		
					long hashValue = hash31(hasha[i], hashb[i], value);
					int hashIndex = (int)(hashValue % SKETCH_WIDTH);
					sketch[i][hashIndex] -= frequency;
					
					long oldItemHash = hash31(hasha[i], hashb[i], oldItem.value);
					int oldItemIndex = (int)(oldItemHash % SKETCH_WIDTH);
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
