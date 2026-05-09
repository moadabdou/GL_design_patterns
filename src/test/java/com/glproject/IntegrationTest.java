package com.glproject;

import com.glproject.behavioral.*;
import com.glproject.creational.DocumentBuilder;
import com.glproject.domain.*;
import com.glproject.facade.DocumentExporterFacade;
import com.glproject.structural.BoldDecorator;
import com.glproject.structural.ItalicDecorator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @Test
    void builderWithFactoryCreatesValidDocument() {
        Document doc = new DocumentBuilder()
                .setTitle("Integration Test")
                .addParagraph("Factory-created paragraph")
                .addImage("test.png", 100, 200)
                .build();

        assertEquals("Integration Test", doc.getTitle());
        assertEquals(2, doc.getElementCount());
        assertInstanceOf(TextElement.class, doc.getElement(0));
        assertInstanceOf(ImageElement.class, doc.getElement(1));
    }

    @Test
    void documentMutationsTriggerObserverEvents() {
        DocumentEventBus bus = new DocumentEventBus();
        Document doc = new Document("Test", bus);
        List<String> events = new ArrayList<>();
        bus.subscribe((event, data) -> events.add(event.name()));

        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("b"));
        doc.removeElement(0);

        assertEquals(3, events.size());
        assertEquals("ELEMENT_ADDED", events.get(0));
        assertEquals("ELEMENT_ADDED", events.get(1));
        assertEquals("ELEMENT_REMOVED", events.get(2));
    }

    @Test
    void strategyWithIteratorExportsAllElements(@TempDir Path tempDir) throws Exception {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("line1"));
        doc.addElement(new TextElement("line2"));

        MarkdownExportStrategy mdStrategy = new MarkdownExportStrategy();
        byte[] result = mdStrategy.render(doc);
        String content = new String(result);

        assertTrue(content.contains("line1"));
        assertTrue(content.contains("line2"));
        assertTrue(content.contains("# Test"));

        for (Element e : doc) {
            assertTrue(content.contains(((TextElement) e).getContent())
                    || content.contains(e.render()));
        }
    }

    @Test
    void facadeExportsMultipleFormats(@TempDir Path tempDir) throws Exception {
        Document doc = new Document("Multi Format");
        doc.addElement(new TextElement("Hello"));

        DocumentExporterFacade facade = new DocumentExporterFacade();

        Path html = tempDir.resolve("test.html");
        Path md = tempDir.resolve("test.md");

        facade.exportHTML(doc, html.toString());
        facade.export(doc, md.toString(), new MarkdownExportStrategy());

        assertTrue(Files.size(html) > 0);
        assertTrue(Files.size(md) > 0);

        String htmlContent = Files.readString(html);
        assertTrue(htmlContent.contains("<h1>Multi Format</h1>"));

        String mdContent = Files.readString(md);
        assertTrue(mdContent.contains("# Multi Format"));
    }

    @Test
    void commandUndoRedoWithObserverNotifications() {
        DocumentEventBus bus = new DocumentEventBus();
        Document doc = new Document("Test", bus);
        List<DocumentEvent> events = new ArrayList<>();
        bus.subscribe((event, data) -> events.add(event));

        CommandHistory history = new CommandHistory();
        TextElement element = new TextElement("undoable");

        history.executeCommand(new AddElementCommand(doc, element));
        assertEquals(1, doc.getElementCount());
        assertTrue(events.contains(DocumentEvent.ELEMENT_ADDED));

        events.clear();
        history.undo();
        assertEquals(0, doc.getElementCount());
        assertTrue(events.contains(DocumentEvent.ELEMENT_REMOVED));

        events.clear();
        history.redo();
        assertEquals(1, doc.getElementCount());
        assertTrue(events.contains(DocumentEvent.ELEMENT_ADDED));
    }

    @Test
    void decoratorWrappingWorksAcrossPatterns() {
        TextElement text = new TextElement("important");
        Element decorated = new BoldDecorator(new ItalicDecorator(text));

        Document doc = new Document("Test");
        doc.addElement(decorated);

        assertEquals(1, doc.getElementCount());
        Element retrieved = doc.getElement(0);
        assertInstanceOf(BoldDecorator.class, retrieved);
        assertTrue(retrieved.render().contains("<b>"));
        assertTrue(retrieved.render().contains("<i>"));
    }

    @Test
    void iteratorFilteredWithDecoratorDetection() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("plain"));
        doc.addElement(new BoldDecorator(new TextElement("bold")));
        doc.addElement(new TextElement("also plain"));

        Iterator<Element> all = doc.iterator();
        int count = 0;
        while (all.hasNext()) { all.next(); count++; }
        assertEquals(3, count);

        Iterator<Element> textOnly = doc.iterator(ElementTypeFilter.textOnly());
        assertEquals(2, countTextOnly(textOnly));

        Iterator<Element> decoratedOnly = doc.iterator(ElementTypeFilter.decoratedOnly());
        assertEquals(1, countTextOnly(decoratedOnly));
    }

    private int countTextOnly(Iterator<Element> it) {
        int count = 0;
        while (it.hasNext()) { it.next(); count++; }
        return count;
    }

    @Test
    void facadeExportFiresLifecycleEvents(@TempDir Path tempDir) {
        DocumentEventBus bus = new DocumentEventBus();
        Document doc = new Document("Test", bus);
        doc.addElement(new TextElement("content"));

        List<DocumentEvent> events = new ArrayList<>();
        bus.subscribe((event, data) -> events.add(event));

        DocumentExporterFacade facade = new DocumentExporterFacade(bus);
        facade.exportHTML(doc, tempDir.resolve("out.html").toString());

        assertTrue(events.contains(DocumentEvent.EXPORT_STARTED));
        assertTrue(events.contains(DocumentEvent.EXPORT_COMPLETED));
    }

    @Test
    void fullPipelineBuilderToExport(@TempDir Path tempDir) throws Exception {
        Document doc = new DocumentBuilder()
                .setTitle("Pipeline Test")
                .addParagraph("Built by builder")
                .build();

        assertEquals(1, doc.getElementCount());

        DocumentExporterFacade facade = new DocumentExporterFacade();
        Path html = tempDir.resolve("pipeline.html");
        facade.exportHTML(doc, html.toString());

        String content = Files.readString(html);
        assertTrue(content.contains("Pipeline Test"));
        assertTrue(content.contains("Built by builder"));
    }

    @Test
    void documentGetEventBusReturnsDefault() {
        Document doc = new Document("Test");
        assertNotNull(doc.getEventBus());
    }

    @Test
    void singletonInstancesAreConsistent() {
        assertSame(com.glproject.util.Logger.getInstance(),
                com.glproject.util.Logger.getInstance());
        assertSame(com.glproject.util.Configuration.getInstance(),
                com.glproject.util.Configuration.getInstance());
    }

    @Test
    void demoMainRunsWithoutError() throws Exception {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }
}
