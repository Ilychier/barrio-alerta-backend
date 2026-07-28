package com.alertabarrio.application;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Meta-annotation for use cases.
 * Combines @Service + @Transactional(rollbackFor = Exception.class)
 * to keep the application layer clean while ensuring transactional safety.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Transactional(rollbackFor = Exception.class)
public @interface UseCase {

    @AliasFor(annotation = Service.class)
    String value() default "";
}
