package com.glproject.structural;

import com.glproject.domain.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AdapterTest {

    @Test
    void exporterAdapter_exportWithContent() throws IOException {
        Document doc = new Document("Test Document");
        doc.addElement(new TextElement("Hello, World!"));
        doc.addElement(new ImageElement("photo.jpg", 100, 200));

        Exporter exporter = new PDFLibraryAdapter();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        exporter.export(doc, output);

        try (PDDocument pdf = Loader.loadPDF(output.toByteArray())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("Hello, World!"));
            assertTrue(text.contains("Test Document"));
        }
    }

    @Test
    void exporterAdapter_emptyDocument() throws IOException {
        Document doc = new Document("Empty Doc");

        Exporter exporter = new PDFLibraryAdapter();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        exporter.export(doc, output);

        try (PDDocument pdf = Loader.loadPDF(output.toByteArray())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("Empty Doc"));
        }
    }

    @Test
    void exporterAdapter_multipleElements() throws IOException {
        Document doc = new Document("Multi-element Doc");
        doc.addElement(new TextElement("Paragraph one"));
        doc.addElement(new TextElement("Paragraph two"));
        doc.addElement(new CodeElement("java", "System.out.println(\"hi\");"));

        Exporter exporter = new PDFLibraryAdapter();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        exporter.export(doc, output);

        try (PDDocument pdf = Loader.loadPDF(output.toByteArray())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            assertTrue(text.contains("Paragraph one"));
            assertTrue(text.contains("Paragraph two"));
            assertTrue(text.contains("java"));
        }
    }

    @Test
    void exporterAdapter_nullDocument_throws() {
        Exporter exporter = new PDFLibraryAdapter();
        assertThrows(IllegalArgumentException.class, () ->
                exporter.export(null, new ByteArrayOutputStream())
        );
    }

    @Test
    void exporterAdapter_nullOutputStream_throws() {
        Document doc = new Document("Test");
        Exporter exporter = new PDFLibraryAdapter();
        assertThrows(IllegalArgumentException.class, () ->
                exporter.export(doc, null)
        );
    }

    @Test
    void exporterAdapter_implementsExporterInterface() {
        PDFLibraryAdapter adapter = new PDFLibraryAdapter();
        assertInstanceOf(Exporter.class, adapter);
    }
}
