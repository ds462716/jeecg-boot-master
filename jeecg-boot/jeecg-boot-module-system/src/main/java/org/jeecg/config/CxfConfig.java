package org.jeecg.config;


import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.jeecg.modules.external.cxf.WebServiceService;
import org.jeecg.modules.external.cxf.impl.WebServiceServiceImpl;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;


@Configuration
public class CxfConfig {

    /**
     * 注入servlet  bean name不能dispatcherServlet 否则会覆盖dispatcherServlet
     * @return
     */
    @Bean(name = "cxfServlet")
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean(new CXFServlet(),"/webservice/*");
    }


    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public WebServiceService demoJsonService(){
        return new WebServiceServiceImpl();
    }

    /**
     * 注册WebServiceService接口到webservice服务
     * @return
     */
    @Bean(name = "WebServiceEndpoint")
    public Endpoint sweptPayEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), demoJsonService());
        endpoint.publish("/webservice");
        return endpoint;
    }
}