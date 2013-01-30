package test.test.test;

public class AAAA {

	public static final int var = getValue();
	static int var2;

	static {

		var2 = getValue();
		var2++;

	}

	public void methodA(){
		System.out.println(methodB() + methodC());
	}

	private int methodB(){

		return methodD();
	}

	private int methodC(){

		return methodD() + 1;
	}

	private int methodD(){

		return 100;
	}


	private static int getValue(){
		return 0;
	}
}
