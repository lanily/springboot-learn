package com.hsiao.springboot.test.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ServerConfig
 * @description: TODO
 * @author xiao
 * @create 2021/4/22
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "server")
public class ServerConfig {

    private Address address;
    private Map<String, String> resourcesPath;

    public static class Address {

        private String ip;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<String, String> getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(Map<String, String> resourcesPath) {
        this.resourcesPath = resourcesPath;
    }
}


