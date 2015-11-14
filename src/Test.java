import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

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
		System.out.println("Simple CMSSpaceSavingTopn:");
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
		System.out.println("Basic CMSSpaceSavingTopn:");
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
		System.out.println("Basic1 CMSSpaceSavingTopn:");
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
		System.out.println("Basic2 CMSSpaceSavingTopn:");
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
		System.out.println("Basic3 CMSSpaceSavingTopn:");
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
		
		//CMSTopn Test
		
		//HybridTopn Test
		
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

