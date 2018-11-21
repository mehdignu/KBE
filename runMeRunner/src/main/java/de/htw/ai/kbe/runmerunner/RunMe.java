package de.htw.ai.kbe.runmerunner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
@Documented
public @interface RunMe {
    //should ignore this test?
    public boolean enabled() default true;
}
