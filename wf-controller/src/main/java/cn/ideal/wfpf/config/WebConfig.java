package cn.ideal.wfpf.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{


	@Bean 
	public FilterRegistrationBean siteMeshFilter(){ 

		FilterRegistrationBean fitler = new FilterRegistrationBean(); 
	
		WebSiteMeshFilter siteMeshFilter = new WebSiteMeshFilter(); 
	
		fitler.setFilter(siteMeshFilter); 
	
		return fitler; 

	}
	
}




