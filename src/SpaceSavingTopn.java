import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SpaceSavingTopn extends TopnStructure {

	final int MULTIPLIER = Constants.MULTIPLIER; //Multiplier for number of top items to be kept

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
	public TopnStructure union(TopnStructure topnStructure) {
		TopnOrdered topn1 = (TopnOrdered)topn;
		TopnOrdered topn2 = (TopnOrdered)topnStructure.topn;
		Item[] topItems1 = topn1.topItems;
		Item[] topItems2 = topn2.topItems;
		Hashtable<Integer, Item> candidateItems = new Hashtable<Integer, Item>();
		
		//XXX be sure to not modify topItems and not exceed currentLength limit by using topItems.length
		for( int i = 0; i < topn1.currentLength; i++)
		{
			Item item = new Item(topItems1[i]); //XXX don't modify items of the individual structures
			candidateItems.put(item.value, item);
		}
		
		for( int i = 0; i < topn2.currentLength; i++)
		{
			Item item = topItems2[i];
			if(candidateItems.containsKey(item.value))
			{
				Item existingItem = candidateItems.get(item.value);
				existingItem.frequency += item.frequency;
			}
			else
			{
				candidateItems.put(item.value, new Item(item));
			}
		}
		
		SpaceSavingTopn newSpaceSavingTopn = new SpaceSavingTopn(topItems1.length/MULTIPLIER);
		ArrayList<Item> candidateList = new ArrayList<Item>(candidateItems.values());
		Collections.sort(candidateList);
		
		for( int i = 0; i < topItems1.length && i < candidateList.size(); i++)
		{
			Item item = candidateList.get(i);
			newSpaceSavingTopn.add(item.value, item.frequency);
		}
		
		return newSpaceSavingTopn;
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
					// TODO Auto-generated catch block
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
		    
		    resultTopn.insertForUnion(new Item(item, frequency));
		}
		
		return resultStructure;
	}
	

}
