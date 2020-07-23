package org.jeecg.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.jeecg.modules.external.cxf.ConsumablesService;
import org.jeecg.modules.external.cxf.WebServiceService;
import org.jeecg.modules.external.cxf.impl.ConsumablesServiceImpl;
import org.jeecg.modules.external.cxf.impl.WebServiceServiceImpl;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;


@Configuration
public class CxfConfig {

    /**
     * 注入servlet  bean name不能dispatcherServlet 否则会覆盖dispatcherServlet
     * http://localhost:3000/jeecg-boot/webservice/webservice?wsdl
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


    @Bean
    public ConsumablesService demoJsonService1(){
        return new ConsumablesServiceImpl();
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

    /**
     * 注册WebServiceService接口到webservice服务
     * @return
     */
    @Bean(name = "WebServiceEndpoint1")
    public Endpoint sweptPayEndpoint1() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), demoJsonService1());
        endpoint.publish("/sendConsumables");
        return endpoint;
    }
}