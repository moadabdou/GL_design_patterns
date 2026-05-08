package com.glproject.domain;

import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ElementTest {

    @Test
    void textElement_rendersContent() {
        TextElement text = new TextElement("Hello, World!");
        assertEquals("Hello, World!", text.render());
    }

    @Test
    void textElement_clone_isIndependent() {
        TextElement original = new TextElement("Hello");
        TextElement cloned = original.clone();
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    @Test
    void textElement_equals_basedOnContent() {
        assertEquals(new TextElement("abc"), new TextElement("abc"));
        assertNotEquals(new TextElement("abc"), new TextElement("xyz"));
    }

    @Test
    void imageElement_rendersCorrectly() {
        ImageElement img = new ImageElement("photo.jpg", 800, 600);
        assertEquals("[Image: photo.jpg (800x600)]", img.render());
    }

    @Test
    void imageElement_clone_isIndependent() {
        ImageElement original = new ImageElement("img.png", 100, 200);
        ImageElement cloned = original.clone();
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    @Test
    void imageElement_equals_basedOnAllFields() {
        assertEquals(new ImageElement("a.jpg", 10, 20), new ImageElement("a.jpg", 10, 20));
        assertNotEquals(new ImageElement("a.jpg", 10, 20), new ImageElement("b.jpg", 10, 20));
        assertNotEquals(new ImageElement("a.jpg", 10, 20), new ImageElement("a.jpg", 99, 20));
    }

    @Test
    void tableElement_rendersCorrectly() {
        TableElement table = new TableElement(List.of(
                List.of("Name", "Age"),
                List.of("Alice", "30")
        ));
        String expected = "[Table]\n  | Name | Age |\n  | Alice | 30 |\n";
        assertEquals(expected, table.render());
    }

    @Test
    void tableElement_clone_isIndependent() {
        TableElement original = new TableElement(List.of(List.of("a")));
        TableElement cloned = original.clone();
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    @Test
    void tableElement_equals_basedOnContent() {
        List<List<String>> rows = List.of(List.of("x"));
        assertEquals(new TableElement(rows), new TableElement(rows));
        assertNotEquals(new TableElement(rows), new TableElement(List.of(List.of("y"))));
    }

    @Test
    void document_managesElements() {
        Document doc = new Document("Test Doc");
        assertEquals("Test Doc", doc.getTitle());

        TextElement text = new TextElement("hello");
        ImageElement img = new ImageElement("pic.png", 100, 100);

        doc.addElement(text);
        doc.addElement(img);
        assertEquals(2, doc.getElementCount());
        assertSame(text, doc.getElement(0));
        assertSame(img, doc.getElement(1));

        doc.removeElement(text);
        assertEquals(1, doc.getElementCount());
        assertSame(img, doc.getElement(0));
    }

    @SuppressWarnings("unused")
    @Test
    void document_isIterable() {
        Document doc = new Document("Doc");
        doc.addElement(new TextElement("a"));
        doc.addElement(new TextElement("b"));

        int count = 0;
        for (Element e : doc) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void document_iterator_isUnmodifiable() {
        Document doc = new Document("Doc");
        doc.addElement(new TextElement("a"));

        Iterator<Element> it = doc.iterator();
        it.next();
        assertThrows(java.lang.UnsupportedOperationException.class, it::remove);
    }

    @Test
    void document_setTitle() {
        Document doc = new Document("Initial");
        doc.setTitle("Updated");
        assertEquals("Updated", doc.getTitle());
    }
}
