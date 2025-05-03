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

/**
 * Configuration class for integrating with Apache ZooKeeper
 * for service discovery.
 * <p>
 * This class configures a connection to ZooKeeper using
 * the {@link CuratorFramework} client.
 * It also registers the current application as
 * a service in ZooKeeper, enabling service discovery.
 *
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.cloud.client.discovery.EnableDiscoveryClient
 * @see org.apache.curator.framework.CuratorFramework
 */
@Configuration
@Slf4j
@EnableDiscoveryClient
public class ZookeeperConfig {
    /**
     * The interval of one retry by milliseconds.
     */
    private static final int RETRY_INTERVAL_MS = 1000;

    /**
     * Max retries that retry police can do.
     */
    private static final int MAX_RETRIES = 3;

    /**
     * The hostname of the ZooKeeper server.
     */
    @Value("${zookeeper.host}")
    private String zookeeperHost;
    /**
     * The port of the ZooKeeper server.
     */
    @Value("${zookeeper.port}")
    private String zookeeperPort;

    /**
     * The name of the current application, used for service registration.
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * The port on which the current application is running.
     */
    @Value("${server.port}")
    private int serverPort;
    /**
     * Creates and configures a {@link CuratorFramework}
     * instance for interacting with ZooKeeper.
     * <p>
     * This method establishes a connection to ZooKeeper
     * using the provided host and port,
     * applies an exponential backoff retry policy,
     * and starts the client. It also attempts
     * to register the current service in ZooKeeper.
     *
     * @return a fully configured and started {@link CuratorFramework} instance
     */
    @Bean
    public CuratorFramework curatorFramework() {
        String zkConnectionString = zookeeperHost + ":" + zookeeperPort;
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkConnectionString)
                .retryPolicy(new ExponentialBackoffRetry(RETRY_INTERVAL_MS,
                        MAX_RETRIES))
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

    /**
     * Registers the current application as a service in ZooKeeper.
     * <p>
     * This method creates a service node under
     * "/services/{applicationName}" if it does not exist,
     * and registers the current instance as
     * an ephemeral node under the service path.
     *
     * @param client the {@link CuratorFramework} client used
     *               to interact with ZooKeeper
     * @throws Exception if an error occurs while registering the service
     */
    private void registerService(final CuratorFramework client)
            throws Exception {
        String servicePath = "/services/" + applicationName;
        String instancePath = servicePath + "/instance-" + serverPort;

        if (client.checkExists().forPath(servicePath) == null) {
            client.create().creatingParentsIfNeeded().forPath(servicePath);
        }
        String instanceData = "http://localhost:" + serverPort;
        client.create().creatingParentsIfNeeded().
                withMode(CreateMode.EPHEMERAL).
                forPath(instancePath, instanceData.getBytes());
    }
}
