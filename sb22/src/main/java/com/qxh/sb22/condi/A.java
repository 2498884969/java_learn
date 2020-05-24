package com.qxh.sb22.condi;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
//@ConditionalOnProperty({"com.qxh"})
@MyConditionAnnotation({"com.qxh1"})
public class A {
}
