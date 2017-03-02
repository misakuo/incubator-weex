package com.taobao.weex.annotations;

/**
 * Created by moxun on 17/3/2.
 */

public @interface WeexComponent {
    String[] names();
    boolean appendTree() default false;
    boolean usingHolder() default false;
    String creator() default "NULL";
}
