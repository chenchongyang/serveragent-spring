
package cn.chenchongyang.serveragent.spring;

import org.springframework.lang.NonNull;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
public interface ServerClient {

    /**
     * 初始化
     */
    void init();

    /**
     * 具体的请求实现
     * 
     * @param serverAgentContext context参数
     * @return 结果
     * @throws Exception 内部应该抛出所有异常
     */
    @NonNull
    Object doRequest(@NonNull ServerAgentContext serverAgentContext) throws Exception;
}
