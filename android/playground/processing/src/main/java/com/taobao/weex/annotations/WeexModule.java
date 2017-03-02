package com.taobao.weex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by moxun on 17/3/1.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WeexModule {
    String name();
    boolean canOverrideExistingModule() default true;
    boolean globalRegistration() default false;
}
