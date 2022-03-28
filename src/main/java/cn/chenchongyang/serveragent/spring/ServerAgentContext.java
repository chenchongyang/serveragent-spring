
package cn.chenchongyang.serveragent.spring;

import lombok.Getter;
import lombok.Setter;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
@Setter
@Getter
public class ServerAgentContext {

    private RequestMethod requestMethod;

    private String host;

    private String path;

    private String encode;

    private int timeout;

    private Map<String, String> headers = new HashMap<>();

    private Map<String, Object> params = new HashMap<>();

}
