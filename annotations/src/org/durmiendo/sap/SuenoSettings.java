package org.durmiendo.sap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.SOURCE;


@Retention(SOURCE)
@Target(ElementType.FIELD)
public @interface SuenoSettings {
    float min() default 0f;
    float max() default 1f;
    float steep() default 0.1f;
}