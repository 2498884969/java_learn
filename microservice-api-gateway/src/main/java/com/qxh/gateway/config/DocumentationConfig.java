package com.qxh.gateway.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置Swagger
 */
@Component
@Primary
class DocumentationConfig implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resource=new ArrayList<>();
        //name可以随便写，location前缀要与zuul配置的path一致。zuul开了token验证,要加上token,否则不用加?token=1
//        resource.add(swaggerResource("myapp-item","/item-service/v2/api-docs?token=1","2.0"));
        resource.add(swaggerResource("myapp-order","/order-service/v2/api-docs?token=1","2.0"));
        return resource;
    }

    //name可以随便写，location前缀要与zuul配置的path一致
    private SwaggerResource swaggerResource(String name,String location,String version){
        SwaggerResource swaggerResource=new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
