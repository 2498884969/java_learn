package com.qxh.sb22.condi;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class MyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        // 1. 获取values 属性
        String[] properties = (String[]) metadata.getAnnotationAttributes(
                "com.qxh.sb22.condi.MyConditionAnnotation").get("value");
        // 2. 判断属性是否存在
        for (String property: properties) {
            if (!StringUtils.isEmpty(context.getEnvironment().getProperty(property))) {
                return true;
            }
        }
        return false;
    }
}
