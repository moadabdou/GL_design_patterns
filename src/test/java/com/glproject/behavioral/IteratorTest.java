package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;

class IteratorTest {

    @Test
    void iteratorTraversesAllElementsInOrder() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("b"));
        doc.addElement(new TextElement("c"));

        List<String> result = new ArrayList<>();
        for (Element e : doc) {
            result.add(((TextElement) e).getContent());
        }

        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    void filteredIteratorTextOnly() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("hello"));
        doc.addElement(new ImageElement("pic.jpg", 100, 200));
        doc.addElement(new TextElement("world"));

        DocumentIterator it = doc.iterator(ElementTypeFilter.textOnly());
        List<String> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(((TextElement) it.next()).getContent());
        }

        assertEquals(List.of("hello", "world"), result);
    }

    @Test
    void filteredIteratorImageOnly() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("text"));
        doc.addElement(new ImageElement("a.jpg", 100, 200));
        doc.addElement(new TextElement("more"));
        doc.addElement(new ImageElement("b.jpg", 300, 400));

        DocumentIterator it = doc.iterator(ElementTypeFilter.imageOnly());
        List<String> paths = new ArrayList<>();
        while (it.hasNext()) {
            paths.add(((ImageElement) it.next()).getImagePath());
        }

        assertEquals(List.of("a.jpg", "b.jpg"), paths);
    }

    @Test
    void filteredIteratorDecoratedOnly() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("plain"));
        doc.addElement(new BoldDecorator(new TextElement("bold")));
        doc.addElement(new TextElement("also plain"));
        doc.addElement(new ItalicDecorator(new TextElement("italic")));

        DocumentIterator it = doc.iterator(ElementTypeFilter.decoratedOnly());
        List<Element> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(it.next());
        }

        assertEquals(2, result.size());
        assertInstanceOf(BoldDecorator.class, result.get(0));
        assertInstanceOf(ItalicDecorator.class, result.get(1));
    }

    @Test
    void filteredIteratorAllReturnsAll() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new ImageElement("b.jpg", 1, 1));
        doc.addElement(new BoldDecorator(new TextElement("c")));

        DocumentIterator it = doc.iterator(ElementTypeFilter.all());
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    void iteratorFailFastOnAddDuringIteration() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("b"));

        Iterator<Element> it = doc.iterator();
        assertTrue(it.hasNext());
        it.next();

        doc.addElement(new TextElement("c"));

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void iteratorFailFastOnRemoveDuringIteration() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("b"));
        doc.addElement(new TextElement("c"));

        Iterator<Element> it = doc.iterator();
        it.next();

        doc.removeElement(1);

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void iteratorFailFastOnRemoveByElementDuringIteration() {
        Document doc = new Document("Test");
        TextElement target = new TextElement("target");
        doc.addElement(new TextElement("first"));
        doc.addElement(target);
        doc.addElement(new TextElement("last"));

        Iterator<Element> it = doc.iterator();
        it.next();

        doc.removeElement(target);

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void setElementDuringIterationDoesNotTriggerFailFast() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("b"));

        Iterator<Element> it = doc.iterator();
        it.next();

        doc.setElement(0, new TextElement("modified"));

        assertDoesNotThrow(it::next);
    }

    @Test
    void multipleIteratorsCoexist() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("b"));
        doc.addElement(new TextElement("c"));

        DocumentIterator it1 = (DocumentIterator) doc.iterator();
        DocumentIterator it2 = (DocumentIterator) doc.iterator();

        assertEquals("a", ((TextElement) it1.next()).getContent());
        assertEquals("a", ((TextElement) it2.next()).getContent());
        assertEquals("b", ((TextElement) it1.next()).getContent());
        assertEquals("b", ((TextElement) it2.next()).getContent());
        assertEquals("c", ((TextElement) it1.next()).getContent());
        assertEquals("c", ((TextElement) it2.next()).getContent());

        assertFalse(it1.hasNext());
        assertFalse(it2.hasNext());
    }

    @Test
    void iteratorRemoveThrowsUnsupported() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));

        Iterator<Element> it = doc.iterator();
        it.next();

        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void emptyDocumentIterator() {
        Document doc = new Document("Empty");

        Iterator<Element> it = doc.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void emptyDocumentFilteredIterator() {
        Document doc = new Document("Empty");

        DocumentIterator it = doc.iterator(ElementTypeFilter.textOnly());
        assertFalse(it.hasNext());
    }

    @Test
    void noSuchElementExceptionWhenPastEnd() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));

        Iterator<Element> it = doc.iterator();
        it.next();

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void textOnlyFilterDoesNotMatchDecoratedText() {
        Document doc = new Document("Test");
        doc.addElement(new BoldDecorator(new TextElement("bold")));
        doc.addElement(new TextElement("plain"));

        DocumentIterator it = doc.iterator(ElementTypeFilter.textOnly());
        assertTrue(it.hasNext());
        assertInstanceOf(TextElement.class, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void decoratedOnlyMatchesDecoratedElements() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("plain"));
        doc.addElement(new UnderlineDecorator(new TextElement("underlined")));
        doc.addElement(new ColorDecorator(new TextElement("colored"), "red"));

        DocumentIterator it = doc.iterator(ElementTypeFilter.decoratedOnly());
        assertTrue(it.hasNext());
        assertInstanceOf(UnderlineDecorator.class, it.next());
        assertTrue(it.hasNext());
        assertInstanceOf(ColorDecorator.class, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void enhancedForEachWorksWithDocument() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("x"));
        doc.addElement(new TextElement("y"));

        int count = 0;
        for (Element e : doc) {
            assertNotNull(e);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void iteratorRespectsFilterWithNoMatches() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("text"));

        DocumentIterator it = doc.iterator(ElementTypeFilter.imageOnly());
        assertFalse(it.hasNext());
    }

    @Test
    void twoFilteredIteratorsIndependently() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("t1"));
        doc.addElement(new ImageElement("i1.jpg", 1, 1));
        doc.addElement(new TextElement("t2"));
        doc.addElement(new ImageElement("i2.jpg", 1, 1));

        DocumentIterator textIt = doc.iterator(ElementTypeFilter.textOnly());
        DocumentIterator imgIt = doc.iterator(ElementTypeFilter.imageOnly());

        assertEquals("t1", ((TextElement) textIt.next()).getContent());
        assertEquals("i1.jpg", ((ImageElement) imgIt.next()).getImagePath());
        assertEquals("t2", ((TextElement) textIt.next()).getContent());
        assertEquals("i2.jpg", ((ImageElement) imgIt.next()).getImagePath());
        assertFalse(textIt.hasNext());
        assertFalse(imgIt.hasNext());
    }

    @Test
    void modCountIncrementedOnAdd() {
        Document doc = new Document("Test");
        int initial = doc.getModCount();
        doc.addElement(new TextElement("a"));
        assertEquals(initial + 1, doc.getModCount());
    }

    @Test
    void modCountIncrementedOnRemove() {
        Document doc = new Document("Test");
        TextElement el = new TextElement("a");
        doc.addElement(el);
        int beforeRemove = doc.getModCount();
        doc.removeElement(el);
        assertEquals(beforeRemove + 1, doc.getModCount());
    }

    @Test
    void modCountNotIncrementedOnSet() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("a"));
        int beforeSet = doc.getModCount();
        doc.setElement(0, new TextElement("b"));
        assertEquals(beforeSet, doc.getModCount());
    }
}
