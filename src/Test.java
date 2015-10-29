
public class Test {
	public static void main(String[] args)
	{
		System.out.println("hell world");
		HybridTopn test1 = new HybridTopn(4);
		test1.add(3);
		test1.add(3);
		test1.add(3);
		test1.add(2);
		test1.add(2);
		test1.add(2);
		test1.add(2);
		test1.add(1);
		test1.add(1);
		test1.add(1);
		test1.add(1);
		test1.add(5);
		test1.add(5);
		test1.add(5);
		test1.add(5);
		test1.add(5);
		test1.add(8);
		test1.add(8);
		test1.add(8);
		test1.add(8);
		test1.add(8);
		test1.add(8);
		test1.add(3);
		test1.add(3);
		test1.add(3);
		test1.add(1);
		test1.add(1);
		test1.add(2);
		test1.add(2);
		test1.add(5);
		test1.add(5);
		test1.add(5);
		test1.add(5);
		System.out.println(test1.topn());
	}
}

