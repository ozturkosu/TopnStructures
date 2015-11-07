
public class Item implements Comparable<Item>{
	int value;
	long frequency;
	
	public Item(int value)
	{
		this.value = value;
		frequency = 1;
	}
	
	public Item(int value, long frequency)
	{
		this.value = value;
		this.frequency = frequency;
	}
	
	public Item(Item item)
	{
		value = item.value;
		frequency = item.frequency;
	}
	
	public String toString()
	{
		return "Value: " + value + ",  frequency: " + frequency;
	}

	@Override
	public int compareTo(Item o) {
		if(frequency > o.frequency)
		{
			return -1;
		}
		else if(frequency == o.frequency)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
}
