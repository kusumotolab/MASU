package hoge;

public class B1 extends A3 implements I1, I2 {

	public void block(){
		System.out.println("hoge");
		{
			System.out.println("hoge2");
			{
				System.out.println("hoge3");
			}
		}
	}
}
