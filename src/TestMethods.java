import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class TestMethods {
	public enum Structure {
		SS, //SpaceSaving
		CMS, //CMS
		H // Hybrid
	}

	public static void main(String[] args)
	{
		convertCsv("checkins");
		System.out.println("ok");
	}
	
	public static void convertCsv(String csvFile)
	{
		Scanner scanner;
		PrintWriter out;

		try {
			scanner = new Scanner(new File("test/" + csvFile + ".csv"));
			out = new PrintWriter(new File("test/" + csvFile));
			String line = scanner.nextLine();
			int i = 1;
			HashMap<String, Integer> venues = new HashMap<String, Integer>();

			while(scanner.hasNextLine())
			{
				line = scanner.nextLine();
				String[] words = line.split(",");
				String venueId = words[5];
				
				if(!venues.containsKey(venueId))
				{
					venues.put(venueId, i);
					out.print(i);
					i++;
				}
				else
				{
					int venue = venues.get(venueId);
					out.print(venue);
				}
				out.print(" ");
			}
			System.out.println(i);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	
	public static void randomGenerator(int count, int max)
	{
		PrintWriter out;
		Random random = new Random();

		try {
			out = new PrintWriter(new File("test/Random" + count));
			for( int i = 0; i < count; i++)
			{
				out.print(random.nextInt(max));
				out.print(" ");
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void normalGenerator(int count, int mean, int variance)
	{
		PrintWriter out;
		Random random = new Random();

		try {
			out = new PrintWriter(new File("test/Normal" + count));
			for( int i = 0; i < count; i++)
			{
				out.print((int)(random.nextGaussian()*variance + mean));
				out.print(" ");
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void realTopN(String inputFile, int n)
	{
		HashMap<Integer, Long> items = new HashMap<Integer, Long>();//item-frequency
		Scanner scanner;
		try {
			scanner = new Scanner(new File("test/" + inputFile));
			
			while(scanner.hasNextInt()){
				int item = scanner.nextInt();
				Long existingFrequency = items.get(item);
				
				if(existingFrequency == null)
				{
					items.put(item, (long) 1);
				}
				else
				{
					items.put(item, existingFrequency + 1);
				}
			}
			
			TopnOrdered topItems = new TopnOrdered(n);
			
			for (Map.Entry<Integer, Long> entry : items.entrySet()) {
			    int item = entry.getKey();
			    long frequency = entry.getValue();
			    
			    //Put if there is place, else try to update
			    boolean inTopn = topItems.put(item, frequency);
			    
			    if(!inTopn)
			    {
			    	topItems.update(new Item(item, frequency));
			    }
			}
			
			System.out.println(topItems.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
