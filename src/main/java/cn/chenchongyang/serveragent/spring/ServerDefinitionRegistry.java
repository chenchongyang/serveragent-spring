package cn.chenchongyang.serveragent.spring;//
//package cn.chenchongyang.common.serveragent;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 类简要描述
// *
// * @author chenchongyang
// * @since 2021-12-30
// */
//public class ServerDefinitionRegistry {
//
//    private final Map<Class<?>, ServerAgentProxyFactory<?>> knowServer = new HashMap<>();
//
//    public <T> void addServer(Class<T> type, String definition) {
//        if (type.isInterface()) {
//            if (!knowServer.containsKey(type)) {
//                knowServer.put(type, definition);
//            }
//        }
//    }
//
//    public <T>T getServer(Class<T> type) {
//        ServerAgentProxyFactory<?> factory = knowServer.get(type);
//        return factory.newInstance(type);
//    }
//}
