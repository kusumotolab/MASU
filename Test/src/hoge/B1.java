package hoge;

public class B1 extends A3 implements I1, I2 {

    private int test = 100;

	public void aaa(){
		A1 ff = new A2();
		ff.test();

        test = 20;

		I1 i11 = new B1();
		i11.duplicate();
	}

	public void bbb(){
		int jjj = 0;
	}

	public int bbb3(int i){
		return i + 1;
	}

	@Override
	public void duplicate() {
		int ttt =0;

	}

}
