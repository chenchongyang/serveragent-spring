
package cn.chenchongyang.serveragent.spring.annotations;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServerMapper {

    RequestMethod requestMethod() default RequestMethod.POST;

    /**
     * 如果在定义则覆盖ServerAgent中的值
     */
    String host() default "";

    /**
     * 如果在定义则覆盖ServerAgent中的值
     */
    String path() default "";

    /**
     * 超时时间（单位秒）如果在定义则覆盖ServerAgent中的值
     */
    int timeout() default -1;

    String encode() default "UTF-8";
}
