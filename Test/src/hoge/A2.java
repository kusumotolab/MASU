package hoge;

public class A2 {
	
	public int calc(int a, int b){
		int c = a*b;
		return c;
	}
	
	public int calc2(int a){
		int c = a*a;
		calc(c,c);
		return c;
	}
	
	public int hoe(){
		calc(10,5);
		calc2(10);
		
		return 10;
	}

}
