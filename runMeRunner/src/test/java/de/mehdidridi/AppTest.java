package de.mehdidridi;

import Service.MethodsAnalyser;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Unit test for simple App.
 */
public class AppTest {

    private MethodsAnalyser m;
    private int numOfLines;
    private String reportFile;


    @Before
    public void setUp() throws ClassNotFoundException, IOException {

        // -DclassName=de.htw.ai.kbe.runmerunner.RunMeTest -Dreport=reportTest2.txt
        System.setProperty("className", "de.htw.ai.kbe.runmerunner.RunMeTest");
        System.setProperty("report", "reportTest2.txt");


        //get the values from SystemProperties
        String className = System.getProperty("className");
        reportFile = System.getProperty("report");
        m = new MethodsAnalyser(className, new File(reportFile));
        String testFile = "reportTest.txt";
        this.numOfLines = countLines(testFile); //number of lines should exist in the test file

    }


    /**
     * test the result of extracting the methods
     */
    @Test
    public void extractingTheMethods() throws IOException {


        StringBuilder result = m.analyseMethods();


        int count = 0;
        String[] lines = result.toString().split("\\n");

        for (String s : lines) count++;
        count++;

        assertEquals(count, numOfLines);
    }

    /**
     * test the values of the file
     */
    @Test
    public void writeToFile() throws IOException {

        StringBuilder result = m.analyseMethods();

        m.saveResult(result);

        //compare the new generated files's number of lines with the test file's
        assertEquals(countLines(reportFile), numOfLines);
    }


    /**
     * counts the number of lines in a file
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
