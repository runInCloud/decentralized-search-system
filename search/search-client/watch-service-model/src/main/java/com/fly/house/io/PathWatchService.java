package com.fly.house.io;

import com.fly.house.io.event.Event;
import com.fly.house.io.event.EventManager;
import com.fly.house.io.event.EventType;
import com.fly.house.io.operations.api.OperationHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dimon on 1/26/14.
 */
public class PathWatchService implements Runnable {

    private volatile boolean stop = false;
    private OperationHistory operationFactory;
    private WatchService watchService;
    private EventManager eventManager;
    private static Logger logger = LoggerFactory.getLogger(PathWatchService.class);

    public PathWatchService(Path rootPath, WatchService watchService, OperationHistory operationHistory) {
        this.watchService = watchService;
        this.operationFactory = operationHistory;
        this.eventManager = new EventManager(rootPath);
    }

    @Override
    public void run() {
        while (!isStop()) {
            startWatch();
        }
    }

    void startWatch() {
        try {
            logger.debug("Block on obtaining WatchKey");
            WatchKey watchKey = watchService.poll(5, TimeUnit.SECONDS);
            logger.debug("WatchKey is taken: {}", watchKey);
            if (watchKey != null) {
                List<WatchEvent<?>> events = watchKey.pollEvents();
                List<WatchEvent<Path>> contexts = eventManager.filterEvents(events);
                List<Event> event = eventManager.encapsulateEvents(contexts);
                if (!isFileParsable(event)) {
                    logger.debug("file with this type is not allowed");
                    return;
                }
                operationFactory.putCommands(event);
                watchKey.reset();
                checkWatchKey(watchKey);
                logger.debug("WatchKey has been rested: {}", watchKey);
            }
        } catch (InterruptedException | ClosedWatchServiceException e) {
            stop();
            logger.warn("WatchService has been interrupted:", e);
        }
    }

    private boolean isFileParsable(List<Event> events) {
        return events.stream()
                .filter(event -> event.getType() == EventType.CREATE)
                .map(Event::getPath)
                .allMatch(FileFilter::isAcceptedType);
    }

    public void stop() {
        stop = true;
    }

    public boolean isStop() {
        return stop;
    }


    private void checkWatchKey(WatchKey key) {
        if (!key.isValid()) {
            logger.debug("WatchKey is invalid: {}", key);
            stop();
        }
    }


}
