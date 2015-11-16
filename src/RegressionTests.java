import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;//TODO Don't forget to put warning about this in the Readme

public class RegressionTests {	
	public static void main(String[] args)
	{
		
		try {
			FileUtils.deleteDirectory(new File("result"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Simple Top-n RegressionTests
		 */
		//SpaceSaving RegressionTests
		System.out.println("Simple SpaceSavingTopn:");
		TopnStructure simpleSS = new SpaceSavingTopn(3);
		TestMethods.testTopnStructure(simpleSS, "SimpleTest");
		
		//CMSTopn RegressionTests
		System.out.println("Simple CMSTopn:");
		TopnStructure simpleCMS = new CMSTopn(3);
		TestMethods.testTopnStructure(simpleCMS, "SimpleTest");

		//HybridTopn RegressionTests
		System.out.println("Simple HybridTopn:");
		TopnStructure simpleHybrid = new HybridTopn(3);
		TestMethods.testTopnStructure(simpleHybrid, "SimpleTest");

		/*
		 * Simple union test
		 */
		TopnStructure simpleArray[] = new TopnStructure[2];
		
		//SpaceSaving RegressionTests
		System.out.println("SimpleTest1 SpaceSavingTopn:");
		TopnStructure simpleSS1 = new SpaceSavingTopn(3);
		TestMethods.testTopnStructure(simpleSS1, "SimpleTest1");
		
		System.out.println("Simple SpaceSaving Union:");
		simpleArray[0] = simpleSS;
		simpleArray[1] = simpleSS1;
		TestMethods.testUnion(TestMethods.Structure.SS, simpleArray, "Simple");
		
		//CMSTopn RegressionTests
		System.out.println("SimpleTest1 CMSTopn:");
		TopnStructure simpleCMS1 = new CMSTopn(3);
		TestMethods.testTopnStructure(simpleCMS1, "SimpleTest1");
		
		System.out.println("Simple CMS Union:");
		simpleArray[0] = simpleCMS;
		simpleArray[1] = simpleCMS1;
		TestMethods.testUnion(TestMethods.Structure.CMS, simpleArray, "Simple");
		
		//HybridTopn RegressionTests
		System.out.println("SimpleTest1 HybridTopn:");
		TopnStructure simpleHybrid1 = new HybridTopn(3);
		TestMethods.testTopnStructure(simpleHybrid1, "SimpleTest1");
		
		System.out.println("Simple Hybrid Union:");
		simpleArray[0] = simpleHybrid;
		simpleArray[1] = simpleHybrid1;
		TestMethods.testUnion(TestMethods.Structure.H, simpleArray, "Simple");

		/*
		 * Basic Top-n Tests to check edge cases with smaller and bigger files
		 */
		//SpaceSaving RegressionTests
		System.out.println("Basic SpaceSavingTopn:");
		TopnStructure basicSS = new SpaceSavingTopn(20);
		TestMethods.testTopnStructure(basicSS, "BasicTest");
		
		//CMSTopn RegressionTests
		System.out.println("Basic CMSTopn:");
		TopnStructure basicCMS = new CMSTopn(20);
		TestMethods.testTopnStructure(basicCMS, "BasicTest");

		//HybridTopn RegressionTests
		System.out.println("Basic HybridTopn:");
		TopnStructure basicHybrid = new HybridTopn(20);
		TestMethods.testTopnStructure(basicHybrid, "BasicTest");
		
		//SpaceSaving RegressionTests
		System.out.println("Basic1 SpaceSavingTopn:");
		TopnStructure basicSS1 = new SpaceSavingTopn(14);
		TestMethods.testTopnStructure(basicSS1, "BasicTest1");
		
		//CMSTopn RegressionTests
		System.out.println("Basic1 CMSTopn:");
		TopnStructure basicCMS1 = new CMSTopn(14);
		TestMethods.testTopnStructure(basicCMS1, "BasicTest1");

		//HybridTopn RegressionTests
		System.out.println("Basic1 HybridTopn:");
		TopnStructure basicHybrid1 = new HybridTopn(14);
		TestMethods.testTopnStructure(basicHybrid1, "BasicTest1");
		
		//SpaceSaving RegressionTests
		System.out.println("Basic2 SpaceSavingTopn:");
		TopnStructure basicSS2 = new SpaceSavingTopn(7);
		TestMethods.testTopnStructure(basicSS2, "BasicTest2");
		
		//CMSTopn RegressionTests
		System.out.println("Basic2 CMSTopn:");
		TopnStructure basicCMS2 = new CMSTopn(7);
		TestMethods.testTopnStructure(basicCMS2, "BasicTest2");

		//HybridTopn RegressionTests
		System.out.println("Basic2 HybridTopn:");
		TopnStructure basicHybrid2 = new HybridTopn(7);
		TestMethods.testTopnStructure(basicHybrid2, "BasicTest2");
		
		//SpaceSaving RegressionTests
		System.out.println("Basic3 SpaceSavingTopn:");
		TopnStructure basicSS3 = new SpaceSavingTopn(1);
		TestMethods.testTopnStructure(basicSS3, "BasicTest3");
		
		//CMSTopn RegressionTests
		System.out.println("Basic3 CMSTopn:");
		TopnStructure basicCMS3 = new CMSTopn(1);
		TestMethods.testTopnStructure(basicCMS3, "BasicTest3");

		//HybridTopn RegressionTests
		System.out.println("Basic3 HybridTopn:");
		TopnStructure basicHybrid3 = new HybridTopn(1);
		TestMethods.testTopnStructure(basicHybrid3, "BasicTest3");
		
		/*
		 * Basic Union Tests to check edge cases with smaller and bigger files
		 */
		//SpaceSaving RegressionTests
		System.out.println("Basic4 SpaceSavingTopn:");
		TopnStructure basicSS4 = new SpaceSavingTopn(20);
		TestMethods.testTopnStructure(basicSS4, "BasicTest4");
		
		System.out.println("Basic SpaceSaving Union:");
		simpleArray[0] = basicSS;
		simpleArray[1] = basicSS4;
		TestMethods.testUnion(TestMethods.Structure.SS, simpleArray, "Basic");
		
		//CMSTopn RegressionTests
		System.out.println("Basic4 CMSTopn:");
		TopnStructure basicCMS4 = new CMSTopn(20);
		TestMethods.testTopnStructure(basicCMS4, "BasicTest4");
		
		System.out.println("Basic CMS Union:");
		simpleArray[0] = basicCMS;
		simpleArray[1] = basicCMS4;
		TestMethods.testUnion(TestMethods.Structure.CMS, simpleArray, "Basic");

		//HybridTopn RegressionTests
		System.out.println("Basic HybridTopn:");
		TopnStructure basicHybrid4 = new HybridTopn(20);
		TestMethods.testTopnStructure(basicHybrid4, "BasicTest4");
		
		System.out.println("Basic Hybrid Union:");
		simpleArray[0] = basicHybrid;
		simpleArray[1] = basicHybrid4;
		TestMethods.testUnion(TestMethods.Structure.H, simpleArray, "Basic");
		
		//SpaceSaving RegressionTests
		System.out.println("Basic5 SpaceSavingTopn:");
		TopnStructure basicSS5 = new SpaceSavingTopn(14);
		TestMethods.testTopnStructure(basicSS5, "BasicTest5");
		
		System.out.println("Basic1 SpaceSaving Union:");
		simpleArray[0] = basicSS1;
		simpleArray[1] = basicSS5;
		TestMethods.testUnion(TestMethods.Structure.SS, simpleArray, "Basic1");
		
		//CMSTopn RegressionTests
		System.out.println("Basic5 CMSTopn:");
		TopnStructure basicCMS5 = new CMSTopn(14);
		TestMethods.testTopnStructure(basicCMS5, "BasicTest5");
		
		System.out.println("Basic1 CMS Union:");
		simpleArray[0] = basicCMS1;
		simpleArray[1] = basicCMS5;
		TestMethods.testUnion(TestMethods.Structure.CMS, simpleArray, "Basic1");

		//HybridTopn RegressionTests
		System.out.println("Basic5 HybridTopn:");
		TopnStructure basicHybrid5 = new HybridTopn(14);
		TestMethods.testTopnStructure(basicHybrid5, "BasicTest5");
		
		System.out.println("Basic1 Hybrid Union:");
		simpleArray[0] = basicHybrid1;
		simpleArray[1] = basicHybrid5;
		TestMethods.testUnion(TestMethods.Structure.H, simpleArray, "Basic1");
		
		//SpaceSaving RegressionTests
		System.out.println("Basic6 SpaceSavingTopn:");
		TopnStructure basicSS6 = new SpaceSavingTopn(1);
		TestMethods.testTopnStructure(basicSS6, "BasicTest6");
		
		System.out.println("Basic2 SpaceSaving Union:");
		simpleArray[0] = basicSS3;
		simpleArray[1] = basicSS6;
		TestMethods.testUnion(TestMethods.Structure.SS, simpleArray, "Basic2");
		
		//CMSTopn RegressionTests
		System.out.println("Basic6 CMSTopn:");
		TopnStructure basicCMS6 = new CMSTopn(1);
		TestMethods.testTopnStructure(basicCMS6, "BasicTest6");
		
		System.out.println("Basic2 CMS Union:");
		simpleArray[0] = basicCMS3;
		simpleArray[1] = basicCMS6;
		TestMethods.testUnion(TestMethods.Structure.CMS, simpleArray, "Basic2");

		//HybridTopn RegressionTests
		System.out.println("Basic6 HybridTopn:");
		TopnStructure basicHybrid6 = new HybridTopn(1);
		TestMethods.testTopnStructure(basicHybrid6, "BasicTest6");
		
		System.out.println("Basic2 Hybrid Union:");
		simpleArray[0] = basicHybrid3;
		simpleArray[1] = basicHybrid6;
		TestMethods.testUnion(TestMethods.Structure.H, simpleArray, "Basic2");
	
	}	
}

