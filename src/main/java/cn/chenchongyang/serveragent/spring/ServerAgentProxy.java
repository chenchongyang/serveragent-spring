
package cn.chenchongyang.serveragent.spring;

import cn.chenchongyang.serveragent.spring.annotations.Body;
import cn.chenchongyang.serveragent.spring.annotations.Header;
import cn.chenchongyang.serveragent.spring.annotations.Headers;
import cn.chenchongyang.serveragent.spring.annotations.Param;
import cn.chenchongyang.serveragent.spring.annotations.ServerAgent;
import cn.chenchongyang.serveragent.spring.annotations.ServerMapper;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
public class ServerAgentProxy<T> implements InvocationHandler {

    private final ServerClient client;

    private final List<ServerFilter> serverFilterList;

    private final ServerAgent serverAgent;

    private final ApplicationContext applicationContext;

    public ServerAgentProxy(Class<T> serverAgentInterface, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        serverAgent = serverAgentInterface.getAnnotation(ServerAgent.class);
        client = getClient(serverAgent.client());
        client.init();
        serverFilterList = getFilter(serverAgent.serverFilter());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServerAgentContext serverAgentContext = buildServerContext(method, args);
        try {
            serverFilterList.forEach(serverFilter -> serverFilter.beforeOnRequest(serverAgentContext));
            Object o = client.doRequest(serverAgentContext);
            serverFilterList.forEach(serverFilter -> serverFilter.afterOnRequest(serverAgentContext));
            return o;
        } finally {
            serverFilterList.forEach(ServerFilter::finallyOnRequest);
        }
    }

    public ServerClient getClient(Class<? extends ServerClient> clientCls) {
        return applicationContext.getBean(clientCls);
    }

    private List<ServerFilter> getFilter(Class<? extends ServerFilter>[] serverFilters) {
        return Arrays.stream(serverFilters)
            .map((Function<Class<? extends ServerFilter>, ServerFilter>) applicationContext::getBean)
            .sorted(Comparator.comparingInt(ServerFilter::order))
            .collect(Collectors.toList());
    }

    private ServerAgentContext buildServerContext(Method method, Object[] args) {
        ServerAgentContext serverAgentContext = new ServerAgentContext();
        // 全局配置
        serverAgentContext.setTimeout(serverAgent.timeout());
        serverAgentContext.setHost(serverAgent.host());
        serverAgentContext.setPath(serverAgent.path());
        parseMethod(method, serverAgentContext);
        parseParameter(method, serverAgentContext, args);
        return serverAgentContext;
    }

    private void parseMethod(Method method, ServerAgentContext serverAgentContext) {
        ServerMapper serverMapper = method.getAnnotation(ServerMapper.class);
        if (serverMapper == null) {
            throw new ServerAgentException("@ServerMapper is necessary!");
        }
        serverAgentContext.setRequestMethod(serverMapper.requestMethod());
        serverAgentContext.setEncode(serverMapper.encode());
        if (StringUtils.isNotBlank(serverMapper.host())) {
            serverAgentContext.setHost(serverMapper.host());
        }
        if (StringUtils.isNotBlank(serverMapper.path())) {
            serverAgentContext.setPath(serverMapper.path());
        }
        if (-1 != serverMapper.timeout()) {
            serverAgentContext.setTimeout(serverMapper.timeout());
        }
    }

    private void parseParameter(Method method, ServerAgentContext serverAgentContext, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            Object paramValue = args[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Headers) {
                    if (paramValue instanceof Map) {
                        serverAgentContext.getHeaders().putAll((Map<String, String>) paramValue);
                    }
                }
                if (annotation instanceof Header) {
                    if (paramValue instanceof String) {
                        serverAgentContext.getHeaders().put(((Header) annotation).value(), (String) paramValue);
                    }
                }
                if (annotation instanceof Body) {
                    Map<String, Object> params = JSONObject.parseObject(JSONObject.toJSONString(paramValue),
                        new TypeReference<Map<String, Object>>() {});
                    serverAgentContext.getParams().putAll(params);
                }
                if (annotation instanceof Param) {
                    serverAgentContext.getParams().put(((Param) annotation).value(), paramValue);
                }
            }
        }
    }
}
