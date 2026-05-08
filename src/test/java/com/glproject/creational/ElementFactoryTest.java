package com.glproject.creational;

import com.glproject.domain.*;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ElementFactoryTest {

    @Test
    void createElement_byClass_createsTextElement() {
        TextElement e = ElementFactory.createElement(TextElement.class, Map.of("content", "Hello"));
        assertEquals("Hello", e.render());
    }

    @Test
    void createElement_byClass_createsImageElement() {
        ImageElement e = ElementFactory.createElement(ImageElement.class,
                Map.of("path", "pic.png", "width", "800", "height", "600"));
        assertEquals("[Image: pic.png (800x600)]", e.render());
    }

    @Test
    void createElement_byClass_createsTableElement() {
        TableElement e = ElementFactory.createElement(TableElement.class, Map.of("rows", "a,b;1,2"));
        String expected = "[Table]\n  | a | b |\n  | 1 | 2 |\n";
        assertEquals(expected, e.render());
    }

    @Test
    void unknownClass_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ElementFactory.createElement(Element.class, Map.of()));
    }

    @Test
    void textFactory_missingContent_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ElementFactory.createElement(TextElement.class, Map.of()));
    }

    @Test
    void imageFactory_missingPath_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ElementFactory.createElement(ImageElement.class, Map.of("width", "1", "height", "1")));
    }

    @Test
    void imageFactory_invalidDimension_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ElementFactory.createElement(ImageElement.class,
                        Map.of("path", "x", "width", "abc", "height", "1")));
    }

    @Test
    void tableFactory_missingRows_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ElementFactory.createElement(TableElement.class, Map.of()));
    }

    @Test
    void textElementFactorySubclass_createsTextElement() {
        TextElementFactory factory = new TextElementFactory();
        Element e = factory.createElement(Map.of("content", "Hello"));
        assertInstanceOf(TextElement.class, e);
        assertEquals("Hello", e.render());
    }

    @Test
    void imageElementFactorySubclass_createsImageElement() {
        ImageElementFactory factory = new ImageElementFactory();
        Element e = factory.createElement(Map.of("path", "img.jpg", "width", "100", "height", "200"));
        assertInstanceOf(ImageElement.class, e);
    }

    @Test
    void tableElementFactorySubclass_createsTableElement() {
        TableElementFactory factory = new TableElementFactory();
        Element e = factory.createElement(Map.of("rows", "x,y;1,2"));
        assertInstanceOf(TableElement.class, e);
    }

    @Test
    void serviceLoader_discoversAllFactories() {
        assertDoesNotThrow(() -> ElementFactory.createElement(TextElement.class, Map.of("content", "a")));
        assertDoesNotThrow(() -> ElementFactory.createElement(ImageElement.class,
                Map.of("path", "a", "width", "1", "height", "1")));
        assertDoesNotThrow(() -> ElementFactory.createElement(TableElement.class, Map.of("rows", "a")));
    }
}
