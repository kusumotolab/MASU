package jp.ac.osaka_u.ist.sel.metricstool.sample2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.AlreadyConnectedException;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultProgressReporter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressReporter;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;

import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;

public class SamplePlugin2 extends AbstractPlugin {

    @Override
    protected LANGUAGE[] getMeasurableLanguages() {
        return languages;
    }

    @Override
    protected boolean useClassInfo() {
       return true;
    }

    @Override
    protected boolean useFileInfo() {
        return true;
    }

    @Override
    protected boolean useMethodInfo() {
        return true;
    }

    @Override
    protected boolean useMethodLocalInfo() {
        return true;
    }

    @Override
    protected void execute() {
        ProgressReporter reporter = null;
        try {
            reporter = new DefaultProgressReporter(this);
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
            return;
        }
        
        MessagePrinter out = new DefaultMessagePrinter(this,MessagePrinter.MESSAGE_TYPE.OUT);
        MessagePrinter err = new DefaultMessagePrinter(this,MessagePrinter.MESSAGE_TYPE.ERROR);
        
        
        out.println("Start measuring!");
        
        reporter.reportProgress(0);
        reporter.reportProgress(5);
        reporter.reportProgress(20);
        
        err.println("Warning : mesuaring process is too buzy.");
        
        reporter.reportProgress(50);
        reporter.reportProgress(100);
        
        out.println("End mesuaring!");
        
        
        out.println("Try to create <PLUGIN_ROOT>\\test.txt.");
        File file = new File(this.getPluginRootDir(),"test.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("test");
            writer.close();
            out.println(file.getAbsolutePath() + " was created!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String getMetricName() {
        return "sample2";
    }

    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.CLASS_METRIC;
    }
    
    private final LANGUAGE[] languages = new LANGUAGE[]{LANGUAGE.JAVA};

}
