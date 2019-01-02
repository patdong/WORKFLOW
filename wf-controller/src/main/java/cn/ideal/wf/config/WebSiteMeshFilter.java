package cn.ideal.wf.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;


public class WebSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {

        builder.addDecoratorPath("/wf/*", "/decorators/decorator.jsp")
        	   .addDecoratorPath("/tb/*", "/decorators/decorator.jsp")
        	   .addDecoratorPath("/em/*", "/decorators/decorator.jsp")
        	   .addDecoratorPath("/cf/*", "/decorators/decorator.jsp")
        	   .addDecoratorPath("/app/*", "/decorators/actualDecorator.jsp")
               .addExcludedPath("/login");        	   
        	   
    }

}
