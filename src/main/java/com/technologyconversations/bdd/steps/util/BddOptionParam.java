package com.technologyconversations.bdd.steps.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BddOptionParam {

    String text();
    String value();
    boolean isSelected() default false;

}
