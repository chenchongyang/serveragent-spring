
package cn.chenchongyang.serveragent.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类简要描述
 *
 * @author chenchongyang
 * @since 2022-03-30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ServerAgentScannerRegistrar.class)
public @interface ServerAgentScan {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<? extends Annotation> annotationClass() default Annotation.class;
}
