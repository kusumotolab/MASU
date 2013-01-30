package innerclass;

import innerclass.InnerClassTest.InnerClass.Inner;

public class InnerClassTest {

	InnerClass2 ic2;
	Inner inner;

	public InnerClassTest(){
		int i = 1;
		ic2 = new InnerClass2();
	}

	class InnerClass {

		public void test(){
			int j = 1;

		}

		private int get(){
			return 1;
		}

		class Inner {

		}

	}

	private class InnerClass2 {
		int field;

		public InnerClass2(){
			field = 1;
		}

		public int getField(){
			return field;
		}
	}

}
