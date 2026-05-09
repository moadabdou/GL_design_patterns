package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;
import com.glproject.facade.DocumentExporterFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    private static class TestObserver implements DocumentObserver {
        final List<DocumentEvent> events = new ArrayList<>();
        final List<Object> dataList = new ArrayList<>();

        @Override
        public void onEvent(DocumentEvent event, Object data) {
            events.add(event);
            dataList.add(data);
        }

        DocumentEvent getLastEvent() {
            return events.isEmpty() ? null : events.getLast();
        }

        Object getLastData() {
            return dataList.isEmpty() ? null : dataList.getLast();
        }

        boolean receivedEvent(DocumentEvent event) {
            return events.contains(event);
        }

        int getEventCount() {
            return events.size();
        }

        void clear() {
            events.clear();
            dataList.clear();
        }
    }

    @Test
    void observerReceivesElementAdded() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        TextElement element = new TextElement("hello");
        doc.addElement(element);

        assertEquals(1, observer.getEventCount());
        assertEquals(DocumentEvent.ELEMENT_ADDED, observer.getLastEvent());
    }

    @Test
    void observerReceivesElementRemoved() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        TextElement element = new TextElement("hello");
        doc.addElement(element);
        observer.clear();

        doc.removeElement(element);

        assertEquals(DocumentEvent.ELEMENT_REMOVED, observer.getLastEvent());
    }

    @Test
    void observerReceivesElementModified() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        TextElement text = new TextElement("hello");
        doc.addElement(text);
        observer.clear();

        doc.setElement(0, new BoldDecorator(text));

        assertEquals(DocumentEvent.ELEMENT_MODIFIED, observer.getLastEvent());
    }

    @Test
    void unsubscribedObserverNoLongerReceivesEvents() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        doc.addElement(new TextElement("first"));
        assertEquals(1, observer.getEventCount());

        bus.unsubscribe(observer);
        observer.clear();

        doc.addElement(new TextElement("second"));
        assertEquals(0, observer.getEventCount());
    }

    @Test
    void multipleObserversReceiveSameEvent() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        bus.subscribe(observer1);
        bus.subscribe(observer2);
        Document doc = new Document("Test", bus);

        doc.addElement(new TextElement("hello"));

        assertEquals(1, observer1.getEventCount());
        assertEquals(1, observer2.getEventCount());
        assertEquals(DocumentEvent.ELEMENT_ADDED, observer1.getLastEvent());
        assertEquals(DocumentEvent.ELEMENT_ADDED, observer2.getLastEvent());
    }

    @Test
    void eventsCarryCorrectDataPayload() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        TextElement added = new TextElement("added");
        doc.addElement(added);
        assertSame(added, observer.getLastData());

        doc.removeElement(added);
        assertSame(added, observer.getLastData());
    }

    @Test
    void addElementAtIndexFiresEvent() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        doc.addElement(new TextElement("first"));
        observer.clear();

        TextElement second = new TextElement("second");
        doc.addElement(0, second);

        assertEquals(DocumentEvent.ELEMENT_ADDED, observer.getLastEvent());
        assertSame(second, observer.getLastData());
    }

    @Test
    void removeElementByIndexFiresEvent() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        TextElement element = new TextElement("hello");
        doc.addElement(element);
        observer.clear();

        doc.removeElement(0);

        assertEquals(DocumentEvent.ELEMENT_REMOVED, observer.getLastEvent());
        assertSame(element, observer.getLastData());
    }

    @Test
    void exportStartedAndCompletedEvents(@TempDir Path tempDir) {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);
        doc.addElement(new TextElement("hello"));

        DocumentExporterFacade facade = new DocumentExporterFacade(bus);
        Path file = tempDir.resolve("output.html");

        facade.exportHTML(doc, file.toString());

        assertTrue(observer.receivedEvent(DocumentEvent.EXPORT_STARTED));
        assertTrue(observer.receivedEvent(DocumentEvent.EXPORT_COMPLETED));
        assertEquals(file.toString(), observer.dataList.get(observer.dataList.size() - 1));
    }

    @Test
    void exportFailedEvent(@TempDir Path tempDir) {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);
        doc.addElement(new TextElement("hello"));

        DocumentExporterFacade facade = new DocumentExporterFacade(bus);
        Path badPath = tempDir.resolve("nonexistent_dir").resolve("output.html");

        try {
            facade.exportHTML(doc, badPath.toString());
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }

        assertTrue(observer.receivedEvent(DocumentEvent.EXPORT_STARTED));
        assertTrue(observer.receivedEvent(DocumentEvent.EXPORT_FAILED));
    }

    @Test
    void exportStatusObserverTracksExportState(@TempDir Path tempDir) {
        DocumentEventBus bus = new DocumentEventBus();
        ExportStatusObserver statusObserver = new ExportStatusObserver();
        bus.subscribe(statusObserver);
        Document doc = new Document("Test", bus);
        doc.addElement(new TextElement("hello"));

        DocumentExporterFacade facade = new DocumentExporterFacade(bus);
        Path file = tempDir.resolve("export.html");

        assertFalse(statusObserver.isExportInProgress());

        facade.exportHTML(doc, file.toString());

        assertFalse(statusObserver.isExportInProgress());
        assertTrue(statusObserver.isLastExportSucceeded());
        assertEquals(file.toString(), statusObserver.getLastExportPath());
    }

    @Test
    void commandOperationsTriggerEvents() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        Command addCmd = new AddElementCommand(doc, new TextElement("hello"));
        addCmd.execute();

        assertTrue(observer.receivedEvent(DocumentEvent.ELEMENT_ADDED));

        observer.clear();
        addCmd.undo();

        assertTrue(observer.receivedEvent(DocumentEvent.ELEMENT_REMOVED));
    }

    @Test
    void documentWithDefaultConstructorHasEventBus() {
        Document doc = new Document("Test");
        assertNotNull(doc.getEventBus());
    }

    @Test
    void facadeWithDefaultConstructorHasEventBus() {
        DocumentExporterFacade facade = new DocumentExporterFacade();
        assertNotNull(facade.getEventBus());
    }

    @Test
    void eventBusGetObserverCount() {
        DocumentEventBus bus = new DocumentEventBus();
        assertEquals(0, bus.getObserverCount());
        bus.subscribe(new TestObserver());
        assertEquals(1, bus.getObserverCount());
        bus.subscribe(new TestObserver());
        assertEquals(2, bus.getObserverCount());
        bus.subscribe(null);
        assertEquals(2, bus.getObserverCount());
    }

    @Test
    void eventBusSubscribeNullDoesNothing() {
        DocumentEventBus bus = new DocumentEventBus();
        bus.subscribe(null);
        assertEquals(0, bus.getObserverCount());
    }

    @Test
    void unsubscribedObserverNotInCount() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        assertEquals(1, bus.getObserverCount());
        bus.unsubscribe(observer);
        assertEquals(0, bus.getObserverCount());
    }

    @Test
    void consoleObserverPrintsToStdout() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try {
            ConsoleObserver console = new ConsoleObserver();
            console.onEvent(DocumentEvent.ELEMENT_ADDED, "test-data");

            String output = out.toString().trim();
            assertTrue(output.contains("ELEMENT_ADDED"));
            assertTrue(output.contains("test-data"));
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void loggingObserverDoesNotThrow() {
        LoggingObserver observer = new LoggingObserver();
        assertDoesNotThrow(() -> {
            observer.onEvent(DocumentEvent.ELEMENT_ADDED, new TextElement("test"));
            observer.onEvent(DocumentEvent.EXPORT_STARTED, "/path/to/file");
            observer.onEvent(DocumentEvent.EXPORT_COMPLETED, "/path/to/file");
        });
    }

    @Test
    void exportStatusObserverTracksFailedExport() {
        DocumentEventBus bus = new DocumentEventBus();
        ExportStatusObserver statusObserver = new ExportStatusObserver();
        bus.subscribe(statusObserver);

        bus.notifyObservers(DocumentEvent.EXPORT_STARTED, "/bad/path");
        assertTrue(statusObserver.isExportInProgress());
        assertFalse(statusObserver.isLastExportSucceeded());

        bus.notifyObservers(DocumentEvent.EXPORT_FAILED, "/bad/path");
        assertFalse(statusObserver.isExportInProgress());
        assertFalse(statusObserver.isLastExportSucceeded());
    }

    @Test
    void documentSetEventBusReplacesBus() {
        DocumentEventBus bus1 = new DocumentEventBus();
        DocumentEventBus bus2 = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus2.subscribe(observer);

        Document doc = new Document("Test", bus1);
        doc.addElement(new TextElement("a"));
        assertEquals(0, observer.getEventCount());

        doc.setEventBus(bus2);
        doc.addElement(new TextElement("b"));

        assertEquals(1, observer.getEventCount());
    }

    @Test
    void facadeSetEventBusReplacesBus(@TempDir Path tempDir) {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);

        DocumentExporterFacade facade = new DocumentExporterFacade();
        facade.setEventBus(bus);
        Document doc = new Document("Test");
        doc.addElement(new TextElement("hello"));

        facade.exportHTML(doc, tempDir.resolve("out.html").toString());

        assertTrue(observer.receivedEvent(DocumentEvent.EXPORT_STARTED));
    }

    @Test
    void removeElementByElementDoesNotFireOnFailedRemove() {
        DocumentEventBus bus = new DocumentEventBus();
        TestObserver observer = new TestObserver();
        bus.subscribe(observer);
        Document doc = new Document("Test", bus);

        boolean result = doc.removeElement(new TextElement("nonexistent"));

        assertFalse(result);
        assertEquals(0, observer.getEventCount());
    }
}
