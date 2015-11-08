
public abstract class TopnStructureWithSketch extends TopnStructure{
	
	final static int SKETCH_DEPTH = Constants.DEPTH;
	final static int SKETCH_WIDTH = Constants.WIDTH;
	final static int MURMUR_SEED = Constants.SEED;
	long[][] sketch;
	
	public long updateSketchAndGetFrequency(MurmurHash3.LongPair hashPair, long updateFrequency)
	{
		long frequency = Long.MAX_VALUE;

		//XXX Check overflow etc?
		for(int i = 0; i < SKETCH_DEPTH; i++)
		{		
			long hashValue = hashPair.val1 + i * hashPair.val2;
			int hashIndex = (int)(hashValue)% SKETCH_WIDTH;
			hashIndex = (hashIndex < 0) ? hashIndex + SKETCH_WIDTH : hashIndex;
			sketch[i][hashIndex] += updateFrequency;
			frequency = Math.min(frequency, sketch[i][hashIndex]);
		}
		
		return frequency;
	}
}
