package com.livingworld.core.registry.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает класс блока для авто-регистрации.
 * Класс должен иметь конструктор без аргументов или (Properties).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterBlock {
    String value(); // "thermal_pipe"
    String creativeTab() default "main";
}