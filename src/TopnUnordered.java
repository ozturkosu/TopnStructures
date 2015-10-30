import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TopnUnordered implements Topn {

	Set<Integer> items;
	int n;
	long currentMin;
	long[][] sketch;//Reference to the related sketch
	
	public TopnUnordered(int n, long[][] sketch) {
		items = new HashSet<Integer>(n);
		this.n = n;
		currentMin = Long.MAX_VALUE;
		this.sketch = sketch;
	}
	
	@Override
	public boolean put(int value) {
		return false;
	}

	@Override
	public Item update(Item item){
		
		if(n > items.size())
		{
			items.add(item.value);
			currentMin = Math.min(item.frequency, currentMin);
		}
		else if( !items.contains(item.value) && currentMin < item.frequency) //Try to add new item by checking updated frequencies
		{
			//Requires updated minimum of the current items
			Item updatedMinItem = minFrequencyOfSet();
			
			//if item.frequency is bigger than updated minimum remove old minimum, put the new item and 
			//update current minimum to new minimum
			if(updatedMinItem.frequency < item.frequency)
			{
				items.remove(updatedMinItem.value);
				items.add(item.value);
				currentMin = updatedMinItem.frequency;
			}
		}		
		return null;
	}
	
	public Item minFrequencyOfSet()
	{
		Iterator<Integer> itemsIterator = items.iterator();
		long minFrequency = Long.MAX_VALUE;
		int minItem = -1;
		
		while(itemsIterator.hasNext())
		{
			int item = itemsIterator.next();
			long itemFrequency = CMSTopn.frequencyFromSketch(item, this.sketch);
			if( itemFrequency < minFrequency)
			{
				minFrequency = itemFrequency;
				minItem = item;
			}
			
		}
		return new Item(minItem, minFrequency);
	}

}
