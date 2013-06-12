package sdl.ist.osaka_u.newmasu.Plugins.CFG;

import java.io.*;

public class TestWriter {
    final static private String outdir = "./sample/";
    static private File file = null;

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
        newFile("hoge");
    }

    public static void newFile(String name){
        file = new File(outdir + name + ".dot");

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

}
