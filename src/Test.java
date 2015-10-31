
public class Test {
	public static void main(String[] args)
	{
		//XXX maybe I can test with PostgreSQL cms_topn
		TopnStructure test1 = new SpaceSavingTopn(4);
		
		for(int i = 0; i < 200; i++)
		{
			test1.add(0);
		}
		System.out.println(test1.topn());

		for(int i = 0; i < 10; i++)
		{
			test1.add(1);
		}
		System.out.println(test1.topn());

		for(int i = 0; i < 150; i++)
		{
			test1.add(2);
		}
		System.out.println(test1.topn());

		
		for(int i = 0; i < 3; i++)
		{
			test1.add(3);
		}
		System.out.println(test1.topn());

		for(int i = 0; i < 40000; i++)
		{
			test1.add(4);
		}
		System.out.println(test1.topn());

		for(int i = 0; i < 6000; i++)
		{
			test1.add(5);
		}
		System.out.println(test1.topn());
	}
}

