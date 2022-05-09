
package cn.chenchongyang.serveragent.spring.common;

import cn.chenchongyang.serveragent.spring.RequestMethod;
import cn.chenchongyang.serveragent.spring.ServerAgentContext;
import cn.chenchongyang.serveragent.spring.ServerAgentException;
import cn.chenchongyang.serveragent.spring.ServerAgentResponse;
import cn.chenchongyang.serveragent.spring.ServerClient;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.lang.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
@Slf4j
public class DefaultNetworkClient implements ServerClient {

    private final RequestConfig DEFAULT_REQUEST_CONFIG =
        RequestConfig.custom().setSocketTimeout(-1).setConnectTimeout(-1).build();

    private CloseableHttpClient closeableHttpClient;

    @PostConstruct
    public void init() {
        closeableHttpClient = HttpClients.custom()
            .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
            .setConnectionManager(creatConnectionManager())
            .setRetryHandler(new DefaultHttpRequestRetryHandler())
            .evictExpiredConnections()
            .evictIdleConnections(30, TimeUnit.SECONDS)
            .build();
    }

    @NonNull
    @Override
    public Object doRequest(@NonNull ServerAgentContext serverAgentContext) throws Exception {
        ServerAgentResponse serverAgentResponse = new ServerAgentResponse();
        HttpRequestBase httpRequestBase = buildHttpRequest(serverAgentContext);
        try (CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpRequestBase)) {
            Map<String, String> header = Arrays.stream(closeableHttpResponse.getAllHeaders())
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
            serverAgentResponse.setHeader(header);

            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            serverAgentResponse.setHttpCode(statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                serverAgentResponse.setRawStr(EntityUtils.toString(closeableHttpResponse.getEntity()));
            }
        }
        return serverAgentResponse;
    }

    private String getUrl(ServerAgentContext serverAgentContext) {
        String host = serverAgentContext.getHost();
        String path = serverAgentContext.getPath();
        // 尾部拼接 /
        if (!host.endsWith("/")) {
            host = host + "/";
        }
        // 去掉头部 /
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        // 去掉尾部 /
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return host + path;
    }

    private HttpRequestBase buildHttpRequest(ServerAgentContext serverAgentContext)
        throws UnsupportedEncodingException {
        HttpRequestBase httpRequestBase;
        if (RequestMethod.POST == serverAgentContext.getRequestMethod()) {
            httpRequestBase = buildHttpPost(serverAgentContext);
        } else if (RequestMethod.GET == serverAgentContext.getRequestMethod()) {
            httpRequestBase = buildHttpGet(serverAgentContext);
        } else {
            throw new ServerAgentException("unsupported request method");
        }
        // 设置header
        serverAgentContext.getHeaders().forEach(httpRequestBase::addHeader);
        if (serverAgentContext.getTimeout() != -1) {
            // 设置超时时间
            httpRequestBase.setConfig(RequestConfig.copy(DEFAULT_REQUEST_CONFIG)
                .setConnectTimeout(getTimeout(serverAgentContext.getTimeout()))
                .setSocketTimeout(getTimeout(serverAgentContext.getTimeout()))
                .build());
        }
        return httpRequestBase;
    }

    private HttpPost buildHttpPost(ServerAgentContext serverAgentContext) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(getUrl(serverAgentContext));
        // todo contentType只考虑了json格式，其他格式还未兼容
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(serverAgentContext.getParams()));
        httpPost.setEntity(stringEntity);
        return httpPost;
    }

    private HttpGet buildHttpGet(ServerAgentContext serverAgentContext) throws UnsupportedEncodingException {
        Map<String, Object> params = serverAgentContext.getParams();
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            joiner.add(entry.getKey());
            joiner.add("=");
            joiner.add(entry.getValue().toString());
        }
        String url = getUrl(serverAgentContext) + "?" + params;
        return new HttpGet(URLEncoder.encode(url, serverAgentContext.getEncode()));
    }

    public HttpClientConnectionManager creatConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(10);
        return connectionManager;
    }

    /**
     * 秒转换为毫秒
     */
    private int getTimeout(int timeout) {
        return timeout * 1000;
    }
}
