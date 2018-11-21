package de.mehdidridi;


import Service.MethodsAnalyser;
import Service.ParseHandler;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        ParseHandler values = new ParseHandler(args);
        CmdLineParser parser = new CmdLineParser(values);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        MethodsAnalyser m = new MethodsAnalyser(values.getClassname(), values.getReport());

        //analyse the methods
        StringBuilder result = m.analyseMethods();

        //save the analyse result to a file
        m.saveResult(result);

    }
}
