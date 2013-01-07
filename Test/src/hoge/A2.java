package hoge;

public class A2 extends A1 {

	int a22 = calc2(10);

	public int calc(int a, int b) {
		int c = a * b;
		return c;
	}

	public int calc2(int a2) {
		int c = a2 * a2;
		int d = calc(c, c);
		calc(1, 0);
		return c;
	}

	public int hoe() {
		calc(10, 5);
		calc2(10);

		return 10;
	}

	@Override
	public void test() {
		int bb = 0;
		calc(1, 1);
	}
}
