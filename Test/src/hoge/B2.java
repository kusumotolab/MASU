package hoge;

public class B2 extends A3 implements I1, I2 {

	public void block(){
		System.out.println("hoge");
        int a = 0;
		{
		if(a==100){
		  a == 200;
		}
		
			System.out.println("hoge2");
            int b = 100;
			{
				System.out.println("hoge3");
                int c = 200;
                if( b==100 ){
                    int d = 20;
                }
			}
			
			int hoge=0;
		}
		int e = 100;
	}

    @Override
    public void bbb() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int bbb3(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void duplicate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
