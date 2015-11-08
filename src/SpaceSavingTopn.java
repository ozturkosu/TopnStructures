import java.util.HashMap;
import java.util.Map;

public class SpaceSavingTopn extends TopnStructure {

	public SpaceSavingTopn(int n) {
		this.n = n;
		topn = new TopnOrdered(n*MULTIPLIER);
	}
	
	@Override
	public long add(int value, long count) {
		
		boolean added = topn.put(value, count);
		
		if(!added)
		{
			long frequency = ((TopnOrdered)topn).getMinimumFrequency() + count;
			Item newItem = new Item(value, frequency);
			topn.update(newItem);
		}
		
		return 0;
	}

	@Override
	public long frequency(int value) {
		return ((TopnOrdered)topn).getFrequencyOfItem(value);
	}

	@Override
	public String topn() {
		return "Top-n:\n" + topn.toString();
	}

	public static TopnStructure unionAll(TopnStructure[] structures) {
		HashMap<Integer, Long> candidateItems = new HashMap<Integer, Long>();
		int n = structures[0].n;
		
		for( int i  = 0; i < structures.length; i++)
		{
			if(structures[i].n != n)
			{
				try {
					throw new Exception("Only merging structures which have same paramater is allowed");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			TopnOrdered topn = (TopnOrdered) structures[i].topn;
			Item[] topnItems = topn.topItems;
			
			for ( int j = 0; j < topn.currentLength; j++)
			{
				Item candidateItem = topnItems[j];
				Long currentFrequency = candidateItems.get(candidateItem.value);
				if( currentFrequency == null)
				{
					candidateItems.put(candidateItem.value, candidateItem.frequency);
				}
				else
				{
					candidateItems.put(candidateItem.value, currentFrequency + candidateItem.frequency);
				}
			}
		}

		SpaceSavingTopn resultStructure = new SpaceSavingTopn(n);
		TopnOrdered resultTopn = (TopnOrdered) resultStructure.topn;
		
		for (Map.Entry<Integer, Long> entry : candidateItems.entrySet()) {
		    int item = entry.getKey();
		    long frequency = entry.getValue();
		    
		    //Put if there is place, else try to update
		    boolean inTopn = resultTopn.put(item, frequency);
		    
		    if(!inTopn)
		    {
		    	resultTopn.update(new Item(item, frequency));
		    }
		}
		
		return resultStructure;
	}
}
