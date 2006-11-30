package jp.ac.osaka_u.ist.sel.metricstool.sample2;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;

import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


public class SamplePlugin2 extends AbstractPlugin {

    @Override
    protected LANGUAGE[] getMeasurableLanguages() {
        return this.languages;
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
        this.out.println("Start measuring!");

        this.reportProgress(0);
        this.reportProgress(5);
        this.reportProgress(20);

        try {
            this.reportProgress(5);
        } catch (final IllegalStateException e) {
            this.err.println(e);
        }

        this.err.println("Warning : measuring process is too buzy.");

        this.reportProgress(50);
        this.reportProgress(100);

        this.out.println("End mesuaring!");

        this.out.println("Try to create <PLUGIN_ROOT>\\test.txt.");
        final File file = new File(this.getPluginRootDir(), "test.txt");
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("I am Sample Plugin 2!");
            writer.close();
            this.out.println(file.getAbsolutePath() + " was created!");
        } catch (final IOException e) {
            this.err.println(e);
            this.err.println(file.getAbsolutePath() + " was not created...");
        }
        File file2 = null;
        try {
            this.out.println("Try to create <PLUGIN_ROOT>\\..\\test.txt");
            file2 = new File(this.getPluginRootDir(), "..\\test.txt");

            final BufferedWriter writer = new BufferedWriter(new FileWriter(file2));
            writer.write("I am Sample Plugin 2!");
            writer.close();
            this.out.println(file2.getAbsolutePath() + " was created!");
        } catch (final IOException e) {
            this.err.println(e);
            this.err.println(file2.getAbsolutePath() + " was not created...");
        } catch (final SecurityException e) {
            this.err.println(e);
            this.err.println(file2.getAbsolutePath() + " was not created...");
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

    private final LANGUAGE[] languages = new LANGUAGE[] { LANGUAGE.JAVA };

}
