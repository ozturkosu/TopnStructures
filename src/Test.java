public class Test {
	public static void main(String[] args)
	{
		//XXX maybe I can test with PostgreSQL cms_topn
		TopnStructure test1 = new SpaceSavingTopn(4);
		TopnStructure test2 = new SpaceSavingTopn(4);
		
		for(int i = 0; i < 200; i++)
		{
			test1.add(0, 1);
			test2.add(4, 1);

		}
		System.out.println(test1.topn());
		System.out.println(test2.topn());


		for(int i = 0; i < 10; i++)
		{
			test1.add(1, 1);
			test2.add(0, 1);

		}
		System.out.println(test1.topn());
		System.out.println(test2.topn());


		for(int i = 0; i < 150; i++)
		{
			test1.add(2, 1);
			test2.add(5, 1);

		}
		System.out.println(test1.topn());
		System.out.println(test2.topn());

		
		for(int i = 0; i < 3; i++)
		{
			test1.add(3, 1);
			test2.add(1, 1);

		}
		System.out.println(test1.topn());
		System.out.println(test2.topn());

		for(int i = 0; i < 40000; i++)
		{
			test1.add(4, 1);
			test2.add(2, 1);

		}
		System.out.println(test1.topn());
		System.out.println(test2.topn());

		for(int i = 0; i < 6000; i++)
		{
			test1.add(5, 1);
			test2.add(3, 1);

		}
		System.out.println(test1.topn());
		System.out.println(test2.topn());
		
		TopnStructure[] structures = new TopnStructure[2];
		structures[0] = test1;
		structures[1] = test2;

		TopnStructure result = SpaceSavingTopn.unionAll(structures);
		System.out.println(result.topn());


	}
}

