package com.qxh.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class OnSystemPropertyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        Map<String, Object> propertiesMap = annotatedTypeMetadata.getAnnotationAttributes("com.qxh.condition.ConditionalOnSystemProperty");

        String name = String.valueOf(propertiesMap.get("name"));

        String value = String.valueOf(propertiesMap.get("value"));

        String systemProperty = System.getProperty(name);

        return systemProperty.equals(value);
    }
}
