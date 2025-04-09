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
    private static final Long FIRST_RANGE_START_VALUE = 100000L;
    private static final Long RANGE_LENGTH = 100000L;
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
                        .forPath(ZK_SHORTENER_PATH, FIRST_RANGE_START_VALUE.toString().getBytes(StandardCharsets.UTF_8));
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
            Long rangeStartValue = Long.parseLong(new String(data));
            long nextRangeStartValue = rangeStartValue + RANGE_LENGTH;
            
            String currentValue = new String(Long.toString(nextRangeStartValue).getBytes(), StandardCharsets.UTF_8);
            Long sequence = parseSequence(currentValue);

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

    private Long parseSequence(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("Valor inválido en ZNode. Usando valor inicial.");
            return FIRST_RANGE_START_VALUE;
        }
    }
}
