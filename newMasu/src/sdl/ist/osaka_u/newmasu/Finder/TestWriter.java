package sdl.ist.osaka_u.newmasu.Finder;

import org.eclipse.jdt.core.dom.ASTNode;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TestWriter {
    final static private String path = "./sample/out.dot";
    static private File file = new File(path);



    public static void println(String str){


        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            pw.println(str);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void newFile(){
        if(file.exists())
            file.delete();

        try {
            File f = file.getParentFile();
            if(f!=null)
                f.mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void newFile(String path){
        file = new File(path);
        newFile();
    }

}
