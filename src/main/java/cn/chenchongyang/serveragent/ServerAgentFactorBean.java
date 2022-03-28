
package cn.chenchongyang.serveragent;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
public class ServerAgentFactorBean<T> implements FactoryBean<T> {

    private final ServerAgentProxyFactory<T> factory;

    private final Class<T> serverAgentInterface;

    public ServerAgentFactorBean(Class<T> serverAgentInterface, ApplicationContext applicationContext) {
        this.serverAgentInterface = serverAgentInterface;
        this.factory = new ServerAgentProxyFactory<>(serverAgentInterface, applicationContext);
    }

    @Override
    public T getObject() throws Exception {
        return factory.newInstance();
    }

    @Override
    public Class<T> getObjectType() {
        return serverAgentInterface;
    }
}
