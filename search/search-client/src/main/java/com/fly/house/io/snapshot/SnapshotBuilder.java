package com.fly.house.io.snapshot;

import com.fly.house.io.exceptions.SnapshotIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.util.Arrays.asList;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Created by dimon on 1/30/14.
 */
//todo exception
public class SnapshotBuilder {

    private Path snapshotFolder = Paths.get("search-client/src/test/resources/snapshots/");
    private String hash;
    private Path path;
    private static Logger logger = LoggerFactory.getLogger(SnapshotBuilder.class);
    private static final Snapshot EMPTY_SNAPSHOT = new Snapshot(Collections.<File>emptyList());

    public SnapshotBuilder(Path path) {
        this.path = path;
        this.hash = hash();
    }

    public Snapshot getFreshSnapshot() {
        File file = path.toFile();
        File[] files = file.listFiles();
        if (files == null) {
            logger.debug("Directory {} does not exist", path.toString());
            logger.debug("Returning empty snapshot");
            return EMPTY_SNAPSHOT;
        }
        return new Snapshot(asList(files));
    }

    public Snapshot getSteelSnapshot() {
        Path pathToSnapshot = snapshotFolder.resolve(hash);
        if (!pathToSnapshot.toFile().exists()) {
            logger.debug("File {} does not exist", pathToSnapshot.toString());
            logger.debug("Returning empty snapshot");
            return EMPTY_SNAPSHOT;
        }
        try (ObjectInputStream ios = new ObjectInputStream(newInputStream(pathToSnapshot))) {
            return (Snapshot) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("Problems with reading snapshot", e);
            throw new SnapshotIOException("Problems with reading snapshot", e);
        }
    }


    public void save(Snapshot snapshot) {
        Path name = snapshotFolder.resolve(hash);
        logger.debug("Saving snapshot to {}", name.toString());
        try (ObjectOutputStream oos = new ObjectOutputStream(newOutputStream(name))) {
            oos.writeObject(snapshot);
        } catch (IOException e) {
            logger.warn("Problems with saving snapshot", e);
            throw new SnapshotIOException("Problems with saving snapshot", e);
        }
    }

    String hash() {
        logger.debug("Generating hash");
        String pathString = path.toAbsolutePath().toString();
        return md5Hex(pathString);
    }

}