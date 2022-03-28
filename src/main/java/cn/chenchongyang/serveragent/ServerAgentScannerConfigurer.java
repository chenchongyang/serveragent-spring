//
//package cn.chenchongyang.serveragent;
//
//import static org.springframework.util.Assert.notNull;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.util.StringUtils;
//
//import java.lang.annotation.Annotation;
//
///**
// * 类简要描述
// *
// * @author chenchongyang
// * @since 2021-12-30
// */
//public class ServerAgentScannerConfigurer
//    implements InitializingBean, BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
//
//    private String basePackage;
//
//    private Class<? extends Annotation> annotationClass;
//
//    private ApplicationContext applicationContext;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        notNull(this.basePackage, "Property 'basePackage' is required");
//    }
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        cn.chenchongyang.common.serveragent.ClassPathServerAgentScanner scanner = new cn.chenchongyang.common.serveragent.ClassPathServerAgentScanner(registry);
//        scanner.setAnnotationClass(annotationClass);
//        scanner.setApplicationContext(applicationContext);
//        scanner.registerFilters();
//        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage,
//            ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        // nothing to do
//    }
//
//    public void setBasePackage(String basePackage) {
//        this.basePackage = basePackage;
//    }
//
//    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
//        this.annotationClass = annotationClass;
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//}
