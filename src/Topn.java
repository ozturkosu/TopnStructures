//To keep the most frequent items
public interface Topn {
	//Checks if it is already in the top-n and updates the frequency if it is, otherwise
	//checks whether there is available place, and creates new item in the top-n if there is
	//place. Returns false in other cases.
	public boolean put(int value, long count);
	
	//Updates top-n according to new Item. Basically check the minimum and update if necessary.
	public Item update(Item item);

}
