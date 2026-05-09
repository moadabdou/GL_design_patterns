package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyTest {

    @Test
    void pdfStrategy_rendersValidPdf() throws Exception {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("hello"));

        byte[] result = new PDFExportStrategy().render(doc);
        assertTrue(result.length > 0);

        try (PDDocument pdf = Loader.loadPDF(result)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("hello"));
            assertTrue(text.contains("Test"));
        }
    }

    @Test
    void pdfStrategy_respectsBoldDecorator() throws Exception {
        Document doc = new Document("Test");
        doc.addElement(new BoldDecorator(new TextElement("bold text")));

        byte[] result = new PDFExportStrategy().render(doc);
        try (PDDocument pdf = Loader.loadPDF(result)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("bold text"));
        }
    }

    @Test
    void pdfStrategy_rendersTable() throws Exception {
        Document doc = new Document("Test");
        doc.addElement(new TableElement(List.of(List.of("a", "b"), List.of("c", "d"))));

        byte[] result = new PDFExportStrategy().render(doc);
        try (PDDocument pdf = Loader.loadPDF(result)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("a"));
            assertTrue(text.contains("b"));
            assertTrue(text.contains("c"));
            assertTrue(text.contains("d"));
        }
    }

    @Test
    void pdfStrategy_respectsColorDecorator() throws Exception {
        Document doc = new Document("Test");
        doc.addElement(new ColorDecorator(new TextElement("red text"), "red"));

        byte[] result = new PDFExportStrategy().render(doc);
        try (PDDocument pdf = Loader.loadPDF(result)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("red text"));
        }
    }

    @Test
    void pdfStrategy_multipleElements() throws Exception {
        Document doc = new Document("Doc");
        doc.addElement(new TextElement("line one"));
        doc.addElement(new TextElement("line two"));

        byte[] result = new PDFExportStrategy().render(doc);
        try (PDDocument pdf = Loader.loadPDF(result)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("line one"));
            assertTrue(text.contains("line two"));
        }
    }

    @Test
    void htmlStrategy_rendersFullDocument() {
        Document doc = new Document("My Title");
        doc.addElement(new TextElement("A paragraph"));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("<!DOCTYPE html>"));
        assertTrue(result.contains("<h1>My Title</h1>"));
        assertTrue(result.contains("<p>A paragraph</p>"));
    }

    @Test
    void htmlStrategy_rendersImage() {
        Document doc = new Document("Doc");
        doc.addElement(new ImageElement("photo.jpg", 640, 480));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("<img"));
        assertTrue(result.contains("src=\"photo.jpg\""));
        assertTrue(result.contains("width=\"640\""));
        assertTrue(result.contains("height=\"480\""));
    }

    @Test
    void htmlStrategy_rendersTable() {
        Document doc = new Document("Doc");
        doc.addElement(new TableElement(List.of(List.of("a", "b"), List.of("c", "d"))));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("border-collapse"));
        assertTrue(result.contains("border: 1px solid black"));
        assertTrue(result.contains(">a<"));
        assertTrue(result.contains(">b<"));
    }

    @Test
    void htmlStrategy_rendersCode() {
        Document doc = new Document("Doc");
        doc.addElement(new CodeElement("java", "class A {}"));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("<pre><code>"));
        assertTrue(result.contains("class A {}"));
    }

    @Test
    void htmlStrategy_respectsBoldDecorator() {
        Document doc = new Document("Doc");
        doc.addElement(new BoldDecorator(new TextElement("bold")));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("<b><p>bold</p></b>"));
    }

    @Test
    void htmlStrategy_respectsItalicDecorator() {
        Document doc = new Document("Doc");
        doc.addElement(new ItalicDecorator(new TextElement("italic")));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("<i><p>italic</p></i>"));
    }

    @Test
    void htmlStrategy_respectsColorDecorator() {
        Document doc = new Document("Doc");
        doc.addElement(new ColorDecorator(new TextElement("colored"), "red"));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("color:red"));
    }

    @Test
    void htmlStrategy_decoratorChain() {
        Document doc = new Document("Doc");
        doc.addElement(new BoldDecorator(new ItalicDecorator(new TextElement("text"))));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("<b>"));
        assertTrue(result.contains("<i>"));
    }

    @Test
    void htmlStrategy_escapesHtmlEntities() {
        Document doc = new Document("Doc");
        doc.addElement(new TextElement("<tag> & \"quote\""));

        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("&lt;tag&gt;"));
        assertTrue(result.contains("&amp;"));
        assertTrue(result.contains("&quot;"));
    }

    @Test
    void markdownStrategy_rendersTitle() {
        Document doc = new Document("My Document");

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.startsWith("# My Document"));
    }

    @Test
    void markdownStrategy_rendersText() {
        Document doc = new Document("Doc");
        doc.addElement(new TextElement("plain text"));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("plain text"));
    }

    @Test
    void markdownStrategy_rendersImage() {
        Document doc = new Document("Doc");
        doc.addElement(new ImageElement("pic.jpg", 100, 200));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("![pic.jpg](pic.jpg)"));
    }

    @Test
    void markdownStrategy_rendersTable() {
        Document doc = new Document("Doc");
        doc.addElement(new TableElement(List.of(List.of("a", "b"), List.of("c", "d"))));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("| a | b |"));
        assertTrue(result.contains("| c | d |"));
        assertTrue(result.contains("---"));
    }

    @Test
    void markdownStrategy_rendersCode() {
        Document doc = new Document("Doc");
        doc.addElement(new CodeElement("java", "class A {}"));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("```java"));
        assertTrue(result.contains("class A {}"));
    }

    @Test
    void markdownStrategy_respectsBoldDecorator() {
        Document doc = new Document("Doc");
        doc.addElement(new BoldDecorator(new TextElement("bold")));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("**bold**"));
    }

    @Test
    void markdownStrategy_respectsItalicDecorator() {
        Document doc = new Document("Doc");
        doc.addElement(new ItalicDecorator(new TextElement("italic")));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("*italic*"));
    }

    @Test
    void markdownStrategy_decoratorChain() {
        Document doc = new Document("Doc");
        doc.addElement(new BoldDecorator(new ItalicDecorator(new TextElement("text"))));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("***text***") || result.contains("** *text* **"));
    }

    @Test
    void markdownStrategy_rendersColorDecorator() {
        Document doc = new Document("Doc");
        doc.addElement(new ColorDecorator(new TextElement("text"), "red"));

        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("style=\"color:red\""));
        assertTrue(result.contains("text"));
    }

    @Test
    void strategies_areInterchangeable() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("hello"));

        ExportStrategy pdf = new PDFExportStrategy();
        ExportStrategy html = new HTMLExportStrategy();
        ExportStrategy md = new MarkdownExportStrategy();

        assertInstanceOf(ExportStrategy.class, pdf);
        assertInstanceOf(ExportStrategy.class, html);
        assertInstanceOf(ExportStrategy.class, md);
    }

    @Test
    void emptyDocument_pdfStrategyProducesValidPdf() throws Exception {
        Document doc = new Document("Empty");
        byte[] result = new PDFExportStrategy().render(doc);
        assertTrue(result.length > 0);
        try (PDDocument pdf = Loader.loadPDF(result)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("Empty"));
        }
    }

    @Test
    void emptyDocument_htmlRendersTitleOnly() {
        Document doc = new Document("Empty");
        String result = new String(new HTMLExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertTrue(result.contains("<h1>Empty</h1>"));
    }

    @Test
    void emptyDocument_markdownRendersTitleOnly() {
        Document doc = new Document("Empty");
        String result = new String(new MarkdownExportStrategy().render(doc), StandardCharsets.UTF_8);
        assertEquals("# Empty", result);
    }
}
