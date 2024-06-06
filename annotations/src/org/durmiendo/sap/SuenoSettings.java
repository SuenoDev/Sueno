package org.durmiendo.sap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface SuenoSettings {
    float min() default 0f;
    float max() default 1f;
    float steep() default 0.1f;
    float def() default 0;
    int accuracy() default 4;
    int priority() default -1;
}