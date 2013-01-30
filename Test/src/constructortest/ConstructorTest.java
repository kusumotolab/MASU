package constructortest;

public class ConstructorTest {

	int field;
	String str;

	public ConstructorTest(int field){
		this.field = field;
		testMethod();
	}

	private void testMethod(){
		str = "abc";
	}


	public String getString(){
		return str;
	}

}
