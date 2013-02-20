package sdl.ist.osaka_u.newmasu.util;

public class Output {
	
	public static void err(String str){
		System.err.println(str);
	}
	
	public static void cannotResolve(String str){
		System.err.println("Cannot resolve " + str);
	}

}
