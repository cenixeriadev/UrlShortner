package com.example.backend.service;
import com.example.backend.utils.Base62Converter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ZooKeeperService {

    private static final String ZK_SHORTENER_PATH = "/shorten";
    private static final String INITIAL_SEQUENCE_VALUE = "0";
    private final CuratorFramework client;
    private final InterProcessMutex lock;
    
    public ZooKeeperService(CuratorFramework client) {
        this.client = client;
        this.lock = new InterProcessMutex(client, ZK_SHORTENER_PATH + "/lock");
        initializeZNode();
    }

    private void initializeZNode() {
        try {
            if (client.checkExists().forPath(ZK_SHORTENER_PATH) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .forPath(ZK_SHORTENER_PATH, INITIAL_SEQUENCE_VALUE.getBytes(StandardCharsets.UTF_8));
                log.info("ZNode inicializado en: {}", ZK_SHORTENER_PATH);
            }
        } catch (Exception e) {
            log.error("Error inicializando ZNode: {}", e.getMessage());
            throw new RuntimeException("No se pudo inicializar ZooKeeper", e);
        }
    }

    public String getNextShortCode() {
        try {
            if (!lock.acquire(2, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout al adquirir el bloqueo");
            }
            log.info("Bloqueo adquirido para generar secuencia");

            byte[] data = client.getData().forPath(ZK_SHORTENER_PATH);
            String currentValue = data != null ? new String(data, StandardCharsets.UTF_8) : INITIAL_SEQUENCE_VALUE;
            int sequence = parseSequence(currentValue);

            sequence++;
            client.setData().forPath(ZK_SHORTENER_PATH, String.valueOf(sequence).getBytes(StandardCharsets.UTF_8));

            String shortCode = Base62Converter.encode(sequence);
            log.info("Nueva secuencia generada: {} → Código: {}", sequence, shortCode);

            return shortCode;
        } catch (Exception e) {
            log.error("Error en getNextShortCode: {}", e.getMessage());
            throw new RuntimeException("Error al generar código corto", e);
        } finally {
            try {
                if (lock.isAcquiredInThisProcess()) {
                    lock.release();
                    log.info("Bloqueo liberado");
                }
            } catch (Exception e) {
                log.error("Error liberando el bloqueo: {}", e.getMessage());
            }
        }
    }

    private int parseSequence(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Valor inválido en ZNode. Usando valor inicial.");
            return Integer.parseInt(INITIAL_SEQUENCE_VALUE);
        }
    }
}
