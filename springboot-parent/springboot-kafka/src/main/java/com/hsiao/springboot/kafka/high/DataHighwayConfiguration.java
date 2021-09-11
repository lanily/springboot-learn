package com.hsiao.springboot.kafka.high;


import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName flink-common
 * @title: DataHighwayConfiguration
 * @description: TODO
 * @author xiao
 * @create 2021/3/10
 * @since 1.0.0
 */
@Data
public class DataHighwayConfiguration {
    private final String trustStorePath;
    private final String trustStoreKey;
    private  final String bootStrapServers;
    private final String schemaRegistryUrl;
    private final boolean trustStoreInClasspath;

    public DataHighwayConfiguration(String bootStrapServers, String schemaRegistryUrl) {
        this(null, null, bootStrapServers, schemaRegistryUrl);
    }

    public DataHighwayConfiguration(String trustStorePath, String trustStoreKey,
            String bootStrapServers, String schemaRegistryUrl) {
        this(trustStorePath, trustStoreKey, bootStrapServers, schemaRegistryUrl, true);
    }
    public DataHighwayConfiguration(String trustStorePath, String trustStoreKey,
            String bootStrapServers, String schemaRegistryUrl, boolean inClasspath) {
        this.trustStorePath = trustStorePath;
        this.trustStoreKey = trustStoreKey;
        this.bootStrapServers = bootStrapServers;
        this.schemaRegistryUrl = schemaRegistryUrl;
        this.trustStoreInClasspath = inClasspath;
    }
}

