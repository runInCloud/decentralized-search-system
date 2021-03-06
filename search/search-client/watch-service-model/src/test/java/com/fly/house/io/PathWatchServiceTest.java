package com.fly.house.io;

import com.fly.house.io.operations.api.OperationHistory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by dimon on 1/29/14.
 */
@RunWith(MockitoJUnitRunner.class)
public class PathWatchServiceTest {

    @Mock
    private WatchService watchService;

    @Mock
    private OperationHistory operationHistory;

    private PathWatchService pathWatchService;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    @Before
    public void setUp() throws Exception {
        pathWatchService = new PathWatchService(Paths.get(""), watchService, operationHistory);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void startWatchShouldDoNothingWhenAnyEventsWereFired() throws InterruptedException {

        when(watchService.poll(5, TIME_UNIT)).thenReturn(null);

        pathWatchService.startWatch();

        verify(watchService).poll(5, TIME_UNIT);
        verifyNoMoreInteractions(watchService);
        verifyZeroInteractions(operationHistory);
    }

    @Test
    public void startWatchShouldStopWhenThrownClosedWatchServiceException() throws InterruptedException {
        when(watchService.poll(5, TIME_UNIT)).thenThrow(new ClosedWatchServiceException());

        pathWatchService.startWatch();

        verifyZeroInteractions(operationHistory);
        assertThat(pathWatchService.isStop(), is(true));
    }

    @Test
    public void startWatchShouldStopWhenThrownInterruptedException() throws InterruptedException {
        when(watchService.poll(5, TIME_UNIT)).thenThrow(new InterruptedException());

        pathWatchService.startWatch();

        verifyZeroInteractions(operationHistory);
        assertThat(pathWatchService.isStop(), is(true));
    }

    @Test
    public void startWatchShouldSaveCommandInOperationHistory() throws InterruptedException {
        WatchKey watchKey = mock(WatchKey.class);
        List<WatchEvent<?>> event = createEvent(ENTRY_CREATE);

        when(watchKey.pollEvents()).thenReturn(event);
        when(watchService.poll(5, TIME_UNIT)).thenReturn(watchKey);

        pathWatchService.startWatch();

        verify(operationHistory).putCommands(anyCollection());
        verifyNoMoreInteractions(operationHistory);
    }

    @SafeVarargs
    private final ArrayList<WatchEvent<?>> createEvent(WatchEvent.Kind<Path>... kindsOfEvent) {
        ArrayList<WatchEvent<?>> events = new ArrayList<>();
        for (WatchEvent.Kind<Path> kindOfEvent : kindsOfEvent) {
            WatchEvent event = mock(WatchEvent.class);
            when(event.count()).thenReturn(1);
            when(event.kind()).thenReturn(kindOfEvent);
            when(event.context()).thenReturn(Paths.get("/"));
            events.add(event);
        }
        return events;
    }


}
