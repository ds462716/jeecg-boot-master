package org.jeecg.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig_prod {
    /**
     * 	注入ServerEndpointExporter，
     * 	这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
/*    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }*/
    
}
