package com.example.backend.config;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EnableDiscoveryClient  // Habilita el descubrimiento de servicios en ZooKeeper
public class ZookeeperConfig {
    @Value("${zookeeper.host}")
    private String zookeeperHost;
    @Value("${zookeeper.port}")
    private String zookeeperPort;
    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private int serverPort;
    
    @Bean
    public CuratorFramework curatorFramework() {
        String zkConnectionString = zookeeperHost + ":" + zookeeperPort;
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkConnectionString)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        try {
            registerService(curatorFramework);
        } catch (Exception e) {
            // Log error
            log.error("Dont register service in zookeeper");
        }

        return curatorFramework;
    }
    private void registerService(CuratorFramework client) throws Exception {
        String servicePath = "/services/" + applicationName;
        String instancePath = servicePath + "/instance-" + serverPort;

        // Crear nodo de servicio si no existe
        if (client.checkExists().forPath(servicePath) == null) {
            client.create().creatingParentsIfNeeded().forPath(servicePath);
        }

        // Registrar esta instancia
        String instanceData = "http://localhost:" + serverPort;
        client.create().creatingParentsIfNeeded().
                withMode(CreateMode.EPHEMERAL).
                forPath(instancePath, instanceData.getBytes());
    }
}