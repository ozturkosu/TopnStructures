public class PerformanceTests {
	
	public static void main(String[] args)
	{
		/*
		 * Top-n tests with different data distributions like Random, Zipfian, Normal..
		 */
		/*
		 * Normal10000
		 */
//		System.out.println("Real Topn:");
//		TestMethods.realTopN("Normal100000", 15);
//		
//		//SpaceSaving PerformanceTests
//		System.out.println("SpaceSaving for Normal100000:");
//		TopnStructure simpleSS = new SpaceSavingTopn(10);
//		TestMethods.testTopnStructure(simpleSS, "Normal100000");
//		
//		//CMSTopn PerformanceTests
//		System.out.println("CMSTopn for Normal100000:");
//		TopnStructure simpleCMS = new CMSTopn(10);
//		TestMethods.testTopnStructure(simpleCMS, "Normal100000");
//
//		//HybridTopn PerformanceTests
//		System.out.println("HybridTopn for Normal100000:");
//		TopnStructure simpleHybrid = new HybridTopn(10);
//		TestMethods.testTopnStructure(simpleHybrid, "Normal100000");
		
		long startTime = System.currentTimeMillis();
		System.out.println("Real Topn:");
		TestMethods.realTopN("checkins", 15);
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Real: " + elapsedTime/1000);
	    
		startTime = System.currentTimeMillis();
		//SpaceSaving PerformanceTests
		System.out.println("SpaceSaving for Normal100000:");
		TopnStructure simpleSS = new SpaceSavingTopn(10);
		TestMethods.testTopnStructure(simpleSS, "checkins");
	    stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    System.out.println("SpaceSaving: " + elapsedTime/1000);
	    startTime = System.currentTimeMillis();
	    
		//CMSTopn PerformanceTests
		System.out.println("CMSTopn for Normal100000:");
		TopnStructure simpleCMS = new CMSTopn(10);
		TestMethods.testTopnStructure(simpleCMS, "checkins");
		stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    System.out.println("CMSTopn: " + elapsedTime/1000);
	    startTime = System.currentTimeMillis();

		//HybridTopn PerformanceTests
		System.out.println("HybridTopn for Normal100000:");
		TopnStructure simpleHybrid = new HybridTopn(10);
		TestMethods.testTopnStructure(simpleHybrid, "checkins");
		stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    System.out.println("HybridTopn: " + elapsedTime/1000);
		
		
		/*
		 * Union tests with different data distributions like Random, Zipfian, Normal..
		 */
		//SpaceSaving PerformanceTests
		
		//CMSTopn PerformanceTests
		
		//HybridTopn PerformanceTests
	}
}
