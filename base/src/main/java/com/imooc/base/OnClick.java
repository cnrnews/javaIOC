package com.imooc.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target(ElementType.FIELD)           代表应用注解的位置
 *      ElementType.FIELD               代表应用到类属性
 *      ElementType.CLASS               代表应用到类上
 *      ElementType.CONSTRUCTOR         代表应用到构造函数
 * @Retention(RetentionPolicy.RUNTIME)  代表什么时候生效
 *      RetentionPolicy.RUNTIME         运行时
 *      RetentionPolicy.CLASS           编译时
 *      RetentionPolicy.SOURCE          源码资源
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick {
    // 可以绑定多个值
    int[] value();
}
