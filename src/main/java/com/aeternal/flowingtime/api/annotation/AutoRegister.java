package com.aeternal.flowingtime.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be processed at runtime via AnnotationScanner (using Forge's
 * ModFileScanData)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface AutoRegister {
    /** The registry name for this element. */
    String value() default "";
}
