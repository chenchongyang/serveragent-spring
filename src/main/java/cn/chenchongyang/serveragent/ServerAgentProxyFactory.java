
package cn.chenchongyang.serveragent;

import org.springframework.context.ApplicationContext;

import java.lang.reflect.Proxy;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
public class ServerAgentProxyFactory<T> {

    private final Class<T> serverAgentInterface;

    private final ApplicationContext applicationContext;

    public ServerAgentProxyFactory(Class<T> serverAgentInterface, ApplicationContext applicationContext) {
        this.serverAgentInterface = serverAgentInterface;
        this.applicationContext = applicationContext;
    }

    /**
     * 创造代理类
     *
     * @return 代理类
     */
    public T newInstance() {
        ServerAgentProxy<T> proxy = new ServerAgentProxy<T>(serverAgentInterface, applicationContext);
        return (T) Proxy.newProxyInstance(serverAgentInterface.getClassLoader(), new Class[] {serverAgentInterface},
            proxy);
    }
}
