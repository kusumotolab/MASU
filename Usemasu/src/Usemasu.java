import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
//import path.Path;

import java.util.*;

public class Usemasu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		Usemasu u = new Usemasu();
		
		

		
//		String[] masuarg2 = {"-h", "-l", "Java"};
		
/*		String[] masuarg = {"-d", "/home/a-saitoh/investigate/testCase/tmpdep/src/Detect2", "-l", "Java",
				"-m", "nom,noa,nopubm,noprim,noprom,nopuba,nopria,noproa,nopubfm,noabsm,lcom1,rfc,dit,wmc,noc,cbo",
				"-C", "/home/a-saitoh/tmp/ret1.csv", "-v"};
*//*		for(int i = 0;i < masuarg.length;i++){
			System.out.print(masuarg[i] + " ");
		}
		*/
		
		for(String[] masuarg : u.initArgList()){
			for(int i = 0;i < masuarg.length;i++){
				System.out.print(masuarg[i] + " ");
			}
			System.out.println("");
			MetricsTool.main(masuarg);
			DataManager.clear();
		}
		System.out.println("End");
	}

	public List<String[]> initArgList(){
		List<String[]> list = new ArrayList<String[]>();

		/*String[] masuarg1 = {"-d", "/home/a-saitoh/investigate/testCase/masusrc/asserttest/", "-l", "java13",  
				"-m", "noc",
				"-C", "/home/a-saitoh/tmp/ret1.csv",
				 "-v"};
		
		list.add(masuarg1);
		*/
			/*
				String[] masuarg = {"-d", "/home/a-saitoh/investigate/testCase/masusrc/emptyfor", "-l", "Java", 
		"-m", "lcom1",
		"-C", "/home/a-saitoh/tmp/ret1.csv",
		 "-v"};
				*/
		String[] masuarg = {"-d", "/home/a-saitoh/workspace/metrics_workspace/masu/src", "-l", "Java", 
				"-m", "nom,noa,nopubm,noprim,noprom,nopuba,nopria,noproa,nopubfm,noabsm,rfc,dit,wmc,noc,cbo",
				"-C", "/home/a-saitoh/tmp/ret1.csv",
				 "-v"};
				
		
		

/*		
		String[] masuarg1 = {"-d", "/home/a-saitoh/investigate/forMASU/netbeans-javadoc/", "-l", "java",  
				"-m", "lcom1",
				"-C", "/home/a-saitoh/tmp/ret1.csv",
				 "-v"};
*/
/*		String[] masuarg2 = {"-d", "/home/a-saitoh/investigate/forMASU/j2sdk1.4.0-javax-swing/", "-l", "java13",  
				"-m", "noc",
				"-C", "/home/a-saitoh/tmp/ret1.csv",
				 "-v"};
		String[] masuarg3 = {"-d", "/home/a-saitoh/investigate/forMASU/eclipse-jdtcore/", "-l", "java13",  
				"-m", "noc",
				"-C", "/home/a-saitoh/tmp/ret1.csv",
				 "-v"};
		String[] masuarg4 = {"-d", "/home/a-saitoh/investigate/forMASU/eclipse-ant/", "-l", "java13",  
				"-m", "noc",
				"-C", "/home/a-saitoh/tmp/ret1.csv",
				 "-v"};
		
		list.add(masuarg1);
		list.add(masuarg2);
		list.add(masuarg3);
	*/	list.add(masuarg);
		
		return list;
	}
	
}

