package com.chen.blog.common.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    //缓存默认存在时间 1分钟 1毫秒*1000*60
    long expire() default 1*60 *1000;
    //缓存标识 key
    String name() default "";
}
