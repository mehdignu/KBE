package de.htw.ai.kbe.runmerunner;

public class RunMeTest {

    @RunMe()
    public void bugFix() {
        System.out.println("Fixes Y2K-Bug");
    }

    @RunMe()
    void testB() {
        throw new RuntimeException("This test always passed");
    }

    void testA() {
        throw new RuntimeException("This test always failed");
    }

    void testK() {
        throw new RuntimeException("This test always failed");
    }

    void testsB() {
        throw new RuntimeException("This test always failed");
    }

    @RunMe()
    void testBx() {
        throw new RuntimeException("This test always passed");
    }


}
