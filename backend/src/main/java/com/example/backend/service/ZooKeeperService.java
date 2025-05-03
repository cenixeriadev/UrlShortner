package com.example.backend.service;
import com.example.backend.utils.Base62Converter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Service class for managing distributed
 * sequence generation using Apache ZooKeeper.
 * <p>
 * This class uses ZooKeeper to generate unique
 * shortcodes in a distributed environment.
 * It ensures thread safety by using an
 * inter-process mutex lock and maintains a range of
 * sequence values in a ZooKeeper node.
 *
 * @see org.apache.curator.framework.CuratorFramework
 * @see org.apache.curator.framework.recipes.locks.InterProcessMutex
 */
@Slf4j
@Service
public final class ZooKeeperService {
    /**
     * The ZooKeeper path used for managing the URL shortener sequence.
     * <p>
     * This path is used as the base node in ZooKeeper
     * for storing the range start value
     * and managing distributed locks.
     */
    private static final String ZK_SHORTENER_PATH = "/shorten";
    /**
     * The initial value of the sequence range stored in ZooKeeper.
     * <p>
     * This value represents the starting point
     * for generating unique shortcodes.
     */
    private static final Long FIRST_RANGE_START_VALUE = 100000L;
    /**
     * The length of each range allocated for shortcode generation.
     * <p>
     * When the current range is exhausted,
     * a new range of this length is allocated.
     */
    private static final Long RANGE_LENGTH = 100000L;
    /**
     * The CuratorFramework client used to interact with ZooKeeper.
     * <p>
     * This client provides methods for creating nodes,
     * retrieving data, and managing locks
     * in a distributed environment.
     *
     * @see org.apache.curator.framework.CuratorFramework
     */
    private final CuratorFramework client;
    /**
     * A distributed lock used to ensure thread safety
     * during shortcode generation.
     * <p>
     * This lock prevents race conditions
     * when multiple instances attempt to generate
     * the next shortcode simultaneously.
     *
     * @see org.apache.curator.framework.recipes.locks.InterProcessMutex
     */
    private final InterProcessMutex lock;

    /**
     * Constructs a new {@code ZooKeeperService} instance.
     *
     * @param clientCurator the {@link CuratorFramework} client
     *               for interacting with ZooKeeper
     */
    public ZooKeeperService(final CuratorFramework clientCurator) {
        this.client = clientCurator;
        this.lock = new InterProcessMutex(client, ZK_SHORTENER_PATH + "/lock");
        initializeZNode();
    }
    /**
     * Initializes the ZooKeeper node if it does not exist.
     * <p>
     * This method creates the node at the
     * specified path and sets its initial value to the
     * first range start value.
     */
    private void initializeZNode() {
        try {
            if (client.checkExists().forPath(ZK_SHORTENER_PATH) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .forPath(ZK_SHORTENER_PATH,
                                FIRST_RANGE_START_VALUE.toString()
                                        .getBytes(StandardCharsets.UTF_8));
                log.info("ZNode initialized to: {}", ZK_SHORTENER_PATH);
            }
        } catch (Exception e) {
            log.error("Error initializing ZNode: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize ZooKeeper", e);
        }
    }

    /**
     * Generates the next shortcode in the sequence.
     * <p>
     * This method acquires a distributed lock,
     * retrieves the current sequence value from
     * ZooKeeper, increments it, and encodes
     * the new value into a Base62 shortcode.
     *
     * @return the generated shortcode
     */
    public String getNextShortCode() {
        try {
            if (!lock.acquire(2, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout when acquiring the lock");
            }
            log.info("Block acquired to generate sequence");

            byte[] data = client.getData().forPath(ZK_SHORTENER_PATH);
            Long rangeStartValue = Long.parseLong(new String(data));
            long nextRangeStartValue = rangeStartValue + RANGE_LENGTH;
            String currentValue = new String(Long.toString(nextRangeStartValue)
                    .getBytes(),
                    StandardCharsets.UTF_8);
            Long sequence = parseSequence(currentValue);

            sequence++;
            client.setData().forPath(ZK_SHORTENER_PATH,
                    String.valueOf(sequence).getBytes(StandardCharsets.UTF_8)
            );
            String shortCode = Base62Converter.encode(sequence);
            log.info("New generated sequence: {} → Code: {}",
                    sequence, shortCode);

            return shortCode;
        } catch (Exception e) {
            log.error("Error in getNextShortCode: {}", e.getMessage());
            throw new RuntimeException("Error generating shortcode", e);
        } finally {
            try {
                if (lock.isAcquiredInThisProcess()) {
                    lock.release();
                    log.info("Lock released");
                }
            } catch (Exception e) {
                log.error("Error releasing the lock: {}", e.getMessage());
            }
        }
    }

    /**
     * Parses a sequence value from the ZooKeeper node.
     * <p>
     * If the value is invalid, the method logs a
     * warning and returns the initial range start value.
     *
     * @param value the value to parse
     * @return the parsed sequence value
     */
    private Long parseSequence(final String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid value in ZNode. Using initial value.");
            return FIRST_RANGE_START_VALUE;
        }
    }
}
