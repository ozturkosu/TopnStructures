
public class Item {
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
	
	public String toString()
	{
		return "Value: " + value + ",  frequency: " + frequency;
	}
}
