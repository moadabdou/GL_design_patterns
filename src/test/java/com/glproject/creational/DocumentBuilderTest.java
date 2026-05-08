package com.glproject.creational;

import com.glproject.domain.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DocumentBuilderTest {

    @Test
    void fluentApi_buildsDocumentWithAllElements() {
        Document doc = new DocumentBuilder()
                .setTitle("Report")
                .addParagraph("Introduction")
                .addImage("chart.png", 800, 600)
                .addTable(List.of(List.of("Name", "Value"), List.of("X", "1")))
                .build();

        assertEquals("Report", doc.getTitle());
        assertEquals(3, doc.getElementCount());
        assertInstanceOf(TextElement.class, doc.getElement(0));
        assertInstanceOf(ImageElement.class, doc.getElement(1));
        assertInstanceOf(TableElement.class, doc.getElement(2));
    }

    @Test
    void elementsAreInOrder() {
        Document doc = new DocumentBuilder()
                .addParagraph("first")
                .addParagraph("second")
                .addParagraph("third")
                .build();

        assertEquals("first", doc.getElement(0).render());
        assertEquals("second", doc.getElement(1).render());
        assertEquals("third", doc.getElement(2).render());
    }

    @Test
    void builderCanBeReused() {
        DocumentBuilder builder = new DocumentBuilder();

        Document doc1 = builder
                .setTitle("First")
                .addParagraph("A")
                .build();

        Document doc2 = builder
                .setTitle("Second")
                .addParagraph("B")
                .build();

        assertEquals("First", doc1.getTitle());
        assertEquals(1, doc1.getElementCount());
        assertEquals("Second", doc2.getTitle());
        assertEquals(1, doc2.getElementCount());
    }

    @Test
    void addImage_createsImageElement() {
        Document doc = new DocumentBuilder()
                .addImage("photo.jpg", 100, 200)
                .build();

        ImageElement img = (ImageElement) doc.getElement(0);
        assertEquals("photo.jpg", img.getImagePath());
        assertEquals(100, img.getWidth());
        assertEquals(200, img.getHeight());
    }

    @Test
    void addTable_createsTableElement() {
        Document doc = new DocumentBuilder()
                .addTable(List.of(List.of("a", "b"), List.of("1", "2")))
                .build();

        TableElement table = (TableElement) doc.getElement(0);
        assertEquals(2, table.getRowCount());
    }

    @Test
    void setTitle_returnsBuilder() {
        DocumentBuilder builder = new DocumentBuilder();
        assertSame(builder, builder.setTitle("T"));
    }

    @Test
    void addParagraph_returnsBuilder() {
        DocumentBuilder builder = new DocumentBuilder();
        assertSame(builder, builder.addParagraph("p"));
    }
}
