package Service;

import de.htw.ai.kbe.runmerunner.RunMe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

public class MethodsAnalyser {

    private Class clazz;
    private File fileName;

    public MethodsAnalyser(String className, File fileName) throws ClassNotFoundException {
        //init class object
        this.clazz = Class.forName(className);
        this.fileName = fileName;
    }


    /**
     * analyse the methods
     * @return
     */
    public StringBuilder analyseMethods() {


        StringBuilder success = new StringBuilder("Methodennamen mit @RunMe: \n");
        StringBuilder failed = new StringBuilder("„Nicht-invokierbare“ Methoden mit @RunMe: \n");
        StringBuilder withoutAnnotation = new StringBuilder("Methodennamen ohne @RunMe: \n");


        //go through the class methods
        for (Method method : clazz.getDeclaredMethods()) {

            //access the private methods
            method.setAccessible(true);

            // if method is annotated with @RunMe
            if (method.isAnnotationPresent(RunMe.class)) {

                //Annotation annotation = method.getAnnotation(RunMe.class);
                // RunMe test = (RunMe) annotation;

                try {
                    //save the successfully invoked methods
                    method.invoke(clazz.newInstance());

                    success.append(method.getName()).append("\n");

                } catch (Throwable ex) {
                    //save the failed invoked methods

                    failed.append(method.getName()).append(" ").append(ex.getCause()).append("\n");

                }

            } else {
                //save methods which have no @RunMe annotation
                withoutAnnotation.append(method.getName()).append("\n");

            }

        }

        return success.append("\n").append(failed).append("\n").append(withoutAnnotation);

    }


    /**
     * write the result to a file
     * @param result
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void saveResult(StringBuilder result) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(result);
        writer.close();
    }


}
