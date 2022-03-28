
package cn.chenchongyang.serveragent;

import org.springframework.lang.NonNull;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
public interface ServerFilter {

    /**
     * 多个过滤器的执行顺序，从小到到的顺序执行
     * 
     * @return 执行顺序
     */
    default int order() {
        return 0;
    }

    /**
     * 请求触发前的钩子
     * 
     * @param serverAgentContext context参数
     */
    default void beforeOnRequest(@NonNull ServerAgentContext serverAgentContext) {
    }

    /**
     * 请求后触发的钩子
     *
     * @param serverAgentContext context参数
     */
    default void afterOnRequest(@NonNull ServerAgentContext serverAgentContext) {
    }

    /**
     * finally方法触发的钩子
     */
    default void finallyOnRequest() {
    }
}
