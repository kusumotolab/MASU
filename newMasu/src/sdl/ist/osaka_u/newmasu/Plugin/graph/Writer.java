package sdl.ist.osaka_u.newmasu.Plugin.graph;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Writer {
    private static Path outdir = Paths.get("./sample/");
    public static void setOutDir(Path p){
        outdir = p;
    }

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
        newFile("./sample/hoge");
    }

    public static void newFile(String name){
        file = new File(outdir + java.nio.file.FileSystems.getDefault().getSeparator() + name + ".dot");

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
