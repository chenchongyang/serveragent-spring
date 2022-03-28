
package cn.chenchongyang.serveragent;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2021-12-30
 */
public class ServerAgentException extends RuntimeException {
    private static final long serialVersionUID = -648614455298039996L;

    public ServerAgentException(String message) {
        super(message);
    }

    public ServerAgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
