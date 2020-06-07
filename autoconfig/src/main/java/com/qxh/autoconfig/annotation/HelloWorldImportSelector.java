package com.qxh.autoconfig.annotation;

import com.qxh.autoconfig.config.HelloWorldConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class HelloWorldImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[] {HelloWorldConfiguration.class.getName()};
    }
}
