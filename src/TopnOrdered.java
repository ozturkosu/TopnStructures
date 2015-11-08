import java.util.HashMap;

//Keeps the top items ordered according to their frequencies
public class TopnOrdered implements Topn{
	Item[] topItems;
	HashMap<Integer, Integer> items;//value-index
	int currentLength;
	
	public TopnOrdered(int n)
	{
		topItems = new Item[n];
		currentLength = 0;
		items = new HashMap<Integer, Integer>();
	}
	
	@Override
	public boolean put(int value, long count) {
		Integer index = items.get(value);
	
		//If it is in the top-n update its frequency and place
		if(index != null)
		{
			Item item = topItems[index];
			item.frequency += count;
			int newIndex = index;
			
			for(int i = index - 1; i > -1; i--)
			{
				Item nextItem = topItems[i];
				
				//Swap if new frequency is bigger than next
				if(nextItem.frequency < item.frequency)
				{
					topItems[i+1] = nextItem;
					items.put(nextItem.value, i+1);
					newIndex = i;
				}
				else
				{
					break;
				}
			}
			
			if(newIndex != index)
			{
				topItems[newIndex] = item;
				items.put(item.value, newIndex);
			}
			return true;
		}
		//if it is not in the top-n check whether there is available place
		else if(currentLength < topItems.length)
		{
			Item newItem = new Item(value, count);
			int newIndex = currentLength;

			for(int i = newIndex - 1; i > -1; i--)
			{
				Item nextItem = topItems[i];
				
				//Swap if new frequency is bigger than next
				if(nextItem.frequency < newItem.frequency)
				{
					topItems[i+1] = nextItem;
					items.put(nextItem.value, i+1);
					newIndex = i;
				}
				else
				{
					break;
				}
				
			}
			
			topItems[newIndex] = newItem;
			items.put(value, newIndex);
			currentLength++;
			return true;
		}
		
		return false;
	}

	
	// Assumes counters are full and the item is not in the counters
	@Override
	public Item update(Item item){
		
		Item minItem = topItems[currentLength-1];
		
		if(minItem.frequency >= item.frequency)
		{
			return null;
		}
		else
		{
			topItems[topItems.length-1] = item;
			items.remove(minItem.value);
			int newIndex = topItems.length-1;
			
			for(int i = newIndex-1; i > -1; i--)
			{
				Item nextItem = topItems[i];
				
				if(item.frequency > nextItem.frequency)
				{
					topItems[i+1] = nextItem;
					items.put(nextItem.value, i+1);
					newIndex = i;
				}
				else
				{
					break;
				}
			}
			
			topItems[newIndex] = item;
			items.put(item.value, newIndex);
			
			return minItem;
		}
	}

	//Info about top-n items
	public String toString()
	{
		String topn = "";
		
		for( int i = 0; i < currentLength; i++)
		{
			topn += (i+1) + ") " + topItems[i] + "\n";
		}
		
		return topn;
	}
	
	public long getFrequencyOfItem(int value)
	{
		Integer itemIndex = items.get(value);
		
		if(itemIndex == null)
		{
			return -1;
		}
		
		Item item = topItems[itemIndex];
		
		return item.frequency;
	}
	
	public long getMinimumFrequency()
	{
		Item minItem = topItems[currentLength-1];
		return minItem.frequency;
	}
	
	public boolean contains(int item)
	{
		return items.containsKey(items);
	}
}
