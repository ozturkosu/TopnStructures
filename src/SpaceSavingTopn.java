
public class SpaceSavingTopn extends TopnStructure {

	final int MULTIPLIER = 1; //Multiplier for number of top items to be kept
	Topn topn;

	public SpaceSavingTopn(int n) {
		topn = new TopnOrdered(n*MULTIPLIER);
	}
	
	@Override
	public long add(int value) {
		
		boolean added = topn.put(value);
		
		if(!added)
		{
			long frequency = ((TopnOrdered)topn).getMinimumFrequency() + 1;
			Item newItem = new Item(value, frequency);
			topn.update(newItem);
		} 
		return 0;
	}

	@Override
	public TopnStructure union(TopnStructure topnStructure) {
		return null;
	}

	@Override
	public long frequency(int value) {
		return ((TopnOrdered)topn).getFrequencyOfItem(value);
	}

	@Override
	public String topn() {
		return "Top-n:\n" + topn.toString();
	}
	

}
