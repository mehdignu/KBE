package Service;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

import java.io.File;

/**
 * This class handles the programs arguments.
 */
public class ParseHandler {

    @Option(name="-c", required = true, usage="Classname")
    public String classname;

    @Option(name="-o",usage="Report")
    private File report;


    private boolean errorFree = false;

    public ParseHandler(String... args) {

        ParserProperties prop = ParserProperties.defaults()
                .withUsageWidth(80)
                .withOptionSorter(null);

        CmdLineParser parser = new CmdLineParser(this, prop);


        try {
            parser.parseArgument(args);

            errorFree = true;
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }

    /**
     * Returns whether the parameters could be parsed without an
     * error.
     *
     * @return true if no error occurred.
     */
    public boolean isErrorFree() {
        return errorFree;
    }

    /**
     * @return The report file.
     */
    public File getReport() {
        return report;
    }

    /**
     * @return The Class name
     */
    public String getClassname(){
        return classname;
    }


}
