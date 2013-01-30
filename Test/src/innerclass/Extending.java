package innerclass;

import innerclass.InnerClassTest.InnerClass.Inner;

public class Extending extends InnerClassTest{
	int aaaa;
	innerclass.Extending.InnerClassExt.InnerInnerClass in2;

	class InnerClassExt {

		InnerInnerClass in;
		Inner innnnnnnnn;

		public void set(int i){
			aaaa = i;
		}

		private class InnerInnerClass {

			public void aaaa(){
				System.out.println();
			}
		}

	}


}
