import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;//TODO Don't forget to put warning about this in the Readme

public class Test {
	
	public enum Structure {
		SS, //SpaceSaving
		CMS, //CMS
		H // Hybrid
	}
	
	public static void main(String[] args)
	{
		
		try {
			FileUtils.deleteDirectory(new File("result"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Simple Top-n Test
		 */
		//SpaceSaving Test
		System.out.println("Simple SpaceSavingTopn:");
		TopnStructure simpleSS = new SpaceSavingTopn(3);
		testTopnStructure(simpleSS, "SimpleTest");
		
		//CMSTopn Test
		System.out.println("Simple CMSTopn:");
		TopnStructure simpleCMS = new CMSTopn(3);
		testTopnStructure(simpleCMS, "SimpleTest");

		//HybridTopn Test
		System.out.println("Simple HybridTopn:");
		TopnStructure simpleHybrid = new HybridTopn(3);
		testTopnStructure(simpleHybrid, "SimpleTest");

		/*
		 * Simple union test
		 */
		TopnStructure simpleArray[] = new TopnStructure[2];
		
		//SpaceSaving Test
		System.out.println("SimpleTest1 SpaceSavingTopn:");
		TopnStructure simpleSS1 = new SpaceSavingTopn(3);
		testTopnStructure(simpleSS1, "SimpleTest1");
		
		System.out.println("Simple SpaceSaving Union:");
		simpleArray[0] = simpleSS;
		simpleArray[1] = simpleSS1;
		testUnion(Structure.SS, simpleArray, "Simple");
		
		//CMSTopn Test
		System.out.println("SimpleTest1 CMSTopn:");
		TopnStructure simpleCMS1 = new CMSTopn(3);
		testTopnStructure(simpleCMS1, "SimpleTest1");
		
		System.out.println("Simple CMS Union:");
		simpleArray[0] = simpleCMS;
		simpleArray[1] = simpleCMS1;
		testUnion(Structure.CMS, simpleArray, "Simple");
		
		//HybridTopn Test
		System.out.println("SimpleTest1 HybridTopn:");
		TopnStructure simpleHybrid1 = new HybridTopn(3);
		testTopnStructure(simpleHybrid1, "SimpleTest1");
		
		System.out.println("Simple Hybrid Union:");
		simpleArray[0] = simpleHybrid;
		simpleArray[1] = simpleHybrid1;
		testUnion(Structure.H, simpleArray, "Simple");

		/*
		 * Basic Top-n Tests to check edge cases with smaller and bigger files
		 */
		//SpaceSaving Test
		System.out.println("Basic SpaceSavingTopn:");
		TopnStructure basicSS = new SpaceSavingTopn(20);
		testTopnStructure(basicSS, "BasicTest");
		
		//CMSTopn Test
		System.out.println("Basic CMSTopn:");
		TopnStructure basicCMS = new CMSTopn(20);
		testTopnStructure(basicCMS, "BasicTest");

		//HybridTopn Test
		System.out.println("Basic HybridTopn:");
		TopnStructure basicHybrid = new HybridTopn(20);
		testTopnStructure(basicHybrid, "BasicTest");
		
		//SpaceSaving Test
		System.out.println("Basic1 SpaceSavingTopn:");
		TopnStructure basicSS1 = new SpaceSavingTopn(14);
		testTopnStructure(basicSS1, "BasicTest1");
		
		//CMSTopn Test
		System.out.println("Basic1 CMSTopn:");
		TopnStructure basicCMS1 = new CMSTopn(14);
		testTopnStructure(basicCMS1, "BasicTest1");

		//HybridTopn Test
		System.out.println("Basic1 HybridTopn:");
		TopnStructure basicHybrid1 = new HybridTopn(14);
		testTopnStructure(basicHybrid1, "BasicTest1");
		
		//SpaceSaving Test
		System.out.println("Basic2 SpaceSavingTopn:");
		TopnStructure basicSS2 = new SpaceSavingTopn(7);
		testTopnStructure(basicSS2, "BasicTest2");
		
		//CMSTopn Test
		System.out.println("Basic2 CMSTopn:");
		TopnStructure basicCMS2 = new CMSTopn(7);
		testTopnStructure(basicCMS2, "BasicTest2");

		//HybridTopn Test
		System.out.println("Basic2 HybridTopn:");
		TopnStructure basicHybrid2 = new HybridTopn(7);
		testTopnStructure(basicHybrid2, "BasicTest2");
		
		//SpaceSaving Test
		System.out.println("Basic3 SpaceSavingTopn:");
		TopnStructure basicSS3 = new SpaceSavingTopn(1);
		testTopnStructure(basicSS3, "BasicTest3");
		
		//CMSTopn Test
		System.out.println("Basic3 CMSTopn:");
		TopnStructure basicCMS3 = new CMSTopn(1);
		testTopnStructure(basicCMS3, "BasicTest3");

		//HybridTopn Test
		System.out.println("Basic3 HybridTopn:");
		TopnStructure basicHybrid3 = new HybridTopn(1);
		testTopnStructure(basicHybrid3, "BasicTest3");
		
		/*
		 * Basic Union Tests to check edge cases with smaller and bigger files
		 */
		//SpaceSaving Test
		System.out.println("Basic4 SpaceSavingTopn:");
		TopnStructure basicSS4 = new SpaceSavingTopn(20);
		testTopnStructure(basicSS4, "BasicTest4");
		
		System.out.println("Basic SpaceSaving Union:");
		simpleArray[0] = basicSS;
		simpleArray[1] = basicSS4;
		testUnion(Structure.SS, simpleArray, "Basic");
		
		//CMSTopn Test
		System.out.println("Basic4 CMSTopn:");
		TopnStructure basicCMS4 = new CMSTopn(20);
		testTopnStructure(basicCMS4, "BasicTest4");
		
		System.out.println("Basic CMS Union:");
		simpleArray[0] = basicCMS;
		simpleArray[1] = basicCMS4;
		testUnion(Structure.CMS, simpleArray, "Basic");

		//HybridTopn Test
		System.out.println("Basic HybridTopn:");
		TopnStructure basicHybrid4 = new HybridTopn(20);
		testTopnStructure(basicHybrid4, "BasicTest4");
		
		System.out.println("Basic Hybrid Union:");
		simpleArray[0] = basicHybrid;
		simpleArray[1] = basicHybrid4;
		testUnion(Structure.H, simpleArray, "Basic");
		
		//SpaceSaving Test
		System.out.println("Basic5 SpaceSavingTopn:");
		TopnStructure basicSS5 = new SpaceSavingTopn(14);
		testTopnStructure(basicSS5, "BasicTest5");
		
		System.out.println("Basic1 SpaceSaving Union:");
		simpleArray[0] = basicSS1;
		simpleArray[1] = basicSS5;
		testUnion(Structure.SS, simpleArray, "Basic1");
		
		//CMSTopn Test
		System.out.println("Basic5 CMSTopn:");
		TopnStructure basicCMS5 = new CMSTopn(14);
		testTopnStructure(basicCMS5, "BasicTest5");
		
		System.out.println("Basic1 CMS Union:");
		simpleArray[0] = basicCMS1;
		simpleArray[1] = basicCMS5;
		testUnion(Structure.CMS, simpleArray, "Basic1");

		//HybridTopn Test
		System.out.println("Basic5 HybridTopn:");
		TopnStructure basicHybrid5 = new HybridTopn(14);
		testTopnStructure(basicHybrid5, "BasicTest5");
		
		System.out.println("Basic1 Hybrid Union:");
		simpleArray[0] = basicHybrid1;
		simpleArray[1] = basicHybrid5;
		testUnion(Structure.H, simpleArray, "Basic1");
		
		//SpaceSaving Test
		System.out.println("Basic6 SpaceSavingTopn:");
		TopnStructure basicSS6 = new SpaceSavingTopn(1);
		testTopnStructure(basicSS6, "BasicTest6");
		
		System.out.println("Basic2 SpaceSaving Union:");
		simpleArray[0] = basicSS3;
		simpleArray[1] = basicSS6;
		testUnion(Structure.SS, simpleArray, "Basic2");
		
		//CMSTopn Test
		System.out.println("Basic6 CMSTopn:");
		TopnStructure basicCMS6 = new CMSTopn(1);
		testTopnStructure(basicCMS6, "BasicTest6");
		
		System.out.println("Basic2 CMS Union:");
		simpleArray[0] = basicCMS3;
		simpleArray[1] = basicCMS6;
		testUnion(Structure.CMS, simpleArray, "Basic2");

		//HybridTopn Test
		System.out.println("Basic6 HybridTopn:");
		TopnStructure basicHybrid6 = new HybridTopn(1);
		testTopnStructure(basicHybrid6, "BasicTest6");
		
		System.out.println("Basic2 Hybrid Union:");
		simpleArray[0] = basicHybrid3;
		simpleArray[1] = basicHybrid6;
		testUnion(Structure.H, simpleArray, "Basic2");
		
		/*
		 * Top-n tests with different data distributions like Zipfian, Normal, Random..
		 */
		//SpaceSaving Test
		
		//CMSTopn Test
		
		//HybridTopn Test
		
		
		/*
		 * Union tests with different data distributions like Zipfian, Normal, Random..
		 */
		//SpaceSaving Test
		
		//CMSTopn Test
		
		//HybridTopn Test
	}
	
	public static void testTopnStructure(TopnStructure ts, String inputFile)
	{		
		Scanner scanner;
		try {
			scanner = new Scanner(new File("test/" + inputFile));
			
			while(scanner.hasNextInt()){
				ts.add(scanner.nextInt(), 1);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
			
		new File("result").mkdir();
		String resultFile = inputFile + "Out";
		FileWriter fw;
		
		try {
			fw = new FileWriter("result/" + resultFile, true);
			fw.write(ts.topn());
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(ts.topn());
	}
	
	public static void testUnion(Structure type, TopnStructure[] structures, String testName)
	{
		TopnStructure result = null;
		
		if( type == Structure.SS)
		{
			result = SpaceSavingTopn.unionAll(structures);
		}
		else if( type == Structure.CMS)
		{
			result = CMSTopn.unionAll(structures);
		}
		else if( type == Structure.H)
		{
			result = HybridTopn.unionAll(structures);
		}
		
		new File("result").mkdir();
		String resultFile = testName + "Union";
		FileWriter fw;
		
		try {
			fw = new FileWriter("result/" + resultFile, true);
			fw.write(result.topn());
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(result.topn());


	}
	
}

