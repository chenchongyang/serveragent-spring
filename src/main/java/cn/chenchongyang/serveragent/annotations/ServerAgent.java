
package cn.chenchongyang.serveragent.annotations;

import cn.chenchongyang.serveragent.ServerClient;
import cn.chenchongyang.serveragent.ServerFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerAgent {

    /**
     * 客户端
     */
    Class<? extends ServerClient> client();

    /**
     * 过滤器
     */
    Class<? extends ServerFilter>[] serverFilter() default {};

    /**
     * host全局属性，如果在ServerMapper中未定义，使用此参数
     */
    String host() default "";

    /**
     * path全局属性，如果在ServerMapper中未定义，使用此参数
     */
    String path() default "";

    /**
     * 超时时间（单位秒）全局属性，如果在ServerMapper中未定义，使用此参数
     */
    int timeout() default -1;

}
