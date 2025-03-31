package com.example.backend.service;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class ZooKeeperService {

    private static final String ZK_SHORTENER_PATH = "/shorten";
    private final CuratorFramework client;
    private final InterProcessMutex lock;

    public ZooKeeperService(CuratorFramework client) {
        this.client = client;
        this.lock = new InterProcessMutex(client, ZK_SHORTENER_PATH + "/lock");
    }

    public int getNextSequence() throws Exception {
        lock.acquire(2, TimeUnit.SECONDS);
        try {
            if (client.checkExists().forPath(ZK_SHORTENER_PATH) == null) {
                client.create().creatingParentsIfNeeded().forPath(ZK_SHORTENER_PATH, "1".getBytes(StandardCharsets.UTF_8));
            }
            byte[] data = client.getData().forPath(ZK_SHORTENER_PATH);
            int sequence = Integer.parseInt(new String(data, StandardCharsets.UTF_8));

            // Incrementamos y guardamos el nuevo valor
            sequence++;
            client.setData().forPath(ZK_SHORTENER_PATH, String.valueOf(sequence).getBytes(StandardCharsets.UTF_8));
            return sequence;
        } finally {
            lock.release();
        }
    }
}
