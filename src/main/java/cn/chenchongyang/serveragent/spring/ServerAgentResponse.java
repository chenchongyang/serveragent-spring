
package cn.chenchongyang.serveragent.spring;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
@Setter
@Getter
public class ServerAgentResponse {

    private Map<String, String> header;

    private String rawStr;

    private Integer httpCode;
}
