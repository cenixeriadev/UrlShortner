package com.example.backend.config;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient  // Habilita el descubrimiento de servicios en ZooKeeper
public class ZookeeperConfig {
    @Value("${zookeeper.host}")
    private String zookeeperHost;
    @Value("${zookeeper.port}")
    private String zookeeperPort;
    
    @Bean
    public CuratorFramework curatorFramework() {
        String zkConnectionString = zookeeperHost + ":" + zookeeperPort;
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkConnectionString)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        return curatorFramework;
    }
}