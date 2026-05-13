package com.glproject.domain;

import com.glproject.behavioral.DocumentEventBus;
import com.glproject.behavioral.DocumentEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void constructorSetsTitle() {
        Document doc = new Document("My Title");
        assertEquals("My Title", doc.getTitle());
    }

    @Test
    void setTitleUpdatesTitle() {
        Document doc = new Document("Old");
        doc.setTitle("New");
        assertEquals("New", doc.getTitle());
    }

    @Test
    void addElementIncreasesCount() {
        Document doc = new Document("Test");
        assertEquals(0, doc.getElementCount());
        doc.addElement(new TextElement("a"));
        assertEquals(1, doc.getElementCount());
    }

    @Test
    void addElementAtSpecificIndex() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("c"));
        doc.addElement(1, new TextElement("b"));

        assertEquals(3, doc.getElementCount());
        assertEquals("a", ((TextElement) doc.getElement(0)).getContent());
        assertEquals("b", ((TextElement) doc.getElement(1)).getContent());
        assertEquals("c", ((TextElement) doc.getElement(2)).getContent());
    }

    @Test
    void removeElementByObjectDecreasesCount() {
        Document doc = new Document("Test");
        TextElement el = new TextElement("a");
        doc.addElement(el);
        assertEquals(1, doc.getElementCount());

        boolean removed = doc.removeElement(el);
        assertTrue(removed);
        assertEquals(0, doc.getElementCount());
    }

    @Test
    void removeElementByObjectReturnsFalseForMissing() {
        Document doc = new Document("Test");
        boolean removed = doc.removeElement(new TextElement("nonexistent"));
        assertFalse(removed);
    }

    @Test
    void removeElementByIndexReturnsRemoved() {
        Document doc = new Document("Test");
        TextElement el = new TextElement("a");
        doc.addElement(el);
        Element removed = doc.removeElement(0);
        assertSame(el, removed);
    }

    @Test
    void setElementReplacesAndReturnsOld() {
        Document doc = new Document("Test");
        TextElement old = new TextElement("old");
        doc.addElement(old);
        Element returned = doc.setElement(0, new TextElement("new"));

        assertSame(old, returned);
        assertEquals("new", ((TextElement) doc.getElement(0)).getContent());
    }

    @Test
    void getElementByIndex() {
        Document doc = new Document("Test");
        TextElement el = new TextElement("hello");
        doc.addElement(el);
        assertSame(el, doc.getElement(0));
    }

    @Test
    void getElementCount() {
        Document doc = new Document("Test");
        assertEquals(0, doc.getElementCount());
        doc.addElement(new TextElement("a"));
        assertEquals(1, doc.getElementCount());
        doc.addElement(new TextElement("b"));
        assertEquals(2, doc.getElementCount());
    }

    @Test
    void getElementOutOfBoundsThrows() {
        Document doc = new Document("Test");
        assertThrows(IndexOutOfBoundsException.class, () -> doc.getElement(0));
    }

    @Test
    void removeElementOutOfBoundsThrows() {
        Document doc = new Document("Test");
        assertThrows(IndexOutOfBoundsException.class, () -> doc.removeElement(0));
    }

    @Test
    void iteratorReturnsDocumentIterator() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        Iterator<Element> it = doc.iterator();
        assertTrue(it.hasNext());
        assertEquals("a", ((TextElement) it.next()).getContent());
        assertFalse(it.hasNext());
    }

    @Test
    void modCountIncrementedOnAdd() {
        Document doc = new Document("Test");
        int before = doc.getModCount();
        doc.addElement(new TextElement("a"));
        assertEquals(before + 1, doc.getModCount());
    }

    @Test
    void modCountIncrementedOnRemove() {
        Document doc = new Document("Test");
        TextElement el = new TextElement("a");
        doc.addElement(el);
        int before = doc.getModCount();
        doc.removeElement(el);
        assertEquals(before + 1, doc.getModCount());
    }

    @Test
    void modCountNotIncrementedOnSet() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        int before = doc.getModCount();
        doc.setElement(0, new TextElement("b"));
        assertEquals(before, doc.getModCount());
    }

    @Test
    void modCountNotIncrementedOnSetTitle() {
        Document doc = new Document("Test");
        int before = doc.getModCount();
        doc.setTitle("New");
        assertEquals(before, doc.getModCount());
    }

    @Test
    void customEventBusInjectedViaConstructor() {
        DocumentEventBus bus = new DocumentEventBus();
        Document doc = new Document("Test", bus);
        assertSame(bus, doc.getEventBus());
    }

    @Test
    void setEventBusReplacesBus() {
        Document doc = new Document("Test");
        DocumentEventBus newBus = new DocumentEventBus();
        doc.setEventBus(newBus);
        assertSame(newBus, doc.getEventBus());
    }

    @Test
    void eventsFiredOnAddElement() {
        DocumentEventBus bus = new DocumentEventBus();
        Document doc = new Document("Test", bus);
        List<DocumentEvent> received = new ArrayList<>();
        bus.subscribe((event, data) -> received.add(event));

        doc.addElement(new TextElement("a"));
        assertEquals(1, received.size());
        assertEquals(DocumentEvent.ELEMENT_ADDED, received.get(0));
    }

    @Test
    void eventsFiredOnRemoveElement() {
        DocumentEventBus bus = new DocumentEventBus();
        Document doc = new Document("Test", bus);
        TextElement el = new TextElement("a");
        doc.addElement(el);

        List<DocumentEvent> received = new ArrayList<>();
        bus.subscribe((event, data) -> received.add(event));

        doc.removeElement(el);
        assertEquals(1, received.size());
        assertEquals(DocumentEvent.ELEMENT_REMOVED, received.get(0));
    }

    @Test
    void eventsFiredOnSetElement() {
        DocumentEventBus bus = new DocumentEventBus();
        Document doc = new Document("Test", bus);
        doc.addElement(new TextElement("a"));

        List<DocumentEvent> received = new ArrayList<>();
        bus.subscribe((event, data) -> received.add(event));

        doc.setElement(0, new TextElement("b"));
        assertEquals(1, received.size());
        assertEquals(DocumentEvent.ELEMENT_MODIFIED, received.get(0));
    }
}
