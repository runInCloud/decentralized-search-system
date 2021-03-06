package com.fly.house.io.operations.api;

import com.fly.house.io.event.Event;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

/**
 * Created by dimon on 2/26/14.
 */
public interface OperationHistory {

    void putCommand(Event event);

    void putCommands(Collection<? extends Event> events);

    void addChangesToHistory(Path path);

    Map<Event, Command> getHistory();
}
