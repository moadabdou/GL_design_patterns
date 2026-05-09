package com.glproject.facade;

import com.glproject.behavioral.*;
import com.glproject.domain.*;
import com.glproject.structural.*;
import com.glproject.util.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FacadeTest {

    private final PrintStream originalOutput = System.out;
    private ByteArrayOutputStream logCapture;
    private PrintStream logStream;

    @BeforeEach
    void setUp() {
        logCapture = new ByteArrayOutputStream();
        logStream = new PrintStream(logCapture);
        Logger.setOutput(logStream);
    }

    @AfterEach
    void tearDown() {
        Logger.setOutput(originalOutput);
    }

    @Test
    void facade_exportWithStrategy_writesToFile() throws IOException {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("hello"));

        Path tempFile = Files.createTempFile("export-test-", ".html");
        try {
            new DocumentExporterFacade().export(doc, tempFile.toString(), new HTMLExportStrategy());

            String content = Files.readString(tempFile);
            assertTrue(content.contains("<h1>Test</h1>"));
            assertTrue(content.contains("<p>hello</p>"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void facade_exportPDF_createsPdfFile() throws IOException {
        Document doc = new Document("PDF Test");
        doc.addElement(new TextElement("PDF content"));

        Path tempFile = Files.createTempFile("facade-pdf-test-", ".pdf");
        try {
            new DocumentExporterFacade().exportPDF(doc, tempFile.toString());

            assertTrue(Files.exists(tempFile));
            assertTrue(Files.size(tempFile) > 0);

            try (PDDocument pdf = Loader.loadPDF(tempFile.toFile())) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(pdf);
                assertTrue(text.contains("PDF Test"));
                assertTrue(text.contains("PDF content"));
            }
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void facade_exportHTML_createsHtmlFile() throws IOException {
        Document doc = new Document("HTML Test");
        doc.addElement(new TextElement("HTML content"));

        Path tempFile = Files.createTempFile("facade-html-test-", ".html");
        try {
            new DocumentExporterFacade().exportHTML(doc, tempFile.toString());

            String content = Files.readString(tempFile);
            assertTrue(content.contains("<!DOCTYPE html>"));
            assertTrue(content.contains("<h1>HTML Test</h1>"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void facade_exportMarkdown_createsMdFile() throws IOException {
        Document doc = new Document("MD Test");
        doc.addElement(new TextElement("MD content"));

        Path tempFile = Files.createTempFile("facade-md-test-", ".md");
        try {
            new DocumentExporterFacade().exportMarkdown(doc, tempFile.toString());

            String content = Files.readString(tempFile);
            assertTrue(content.contains("# MD Test"));
            assertTrue(content.contains("MD content"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void facade_logsExportOperation() throws IOException {
        DocumentEventBus bus = new DocumentEventBus();
        LoggingObserver loggingObserver = new LoggingObserver();
        bus.subscribe(loggingObserver);
        DocumentExporterFacade facade = new DocumentExporterFacade(bus);

        Document doc = new Document("Log Test");
        doc.addElement(new TextElement("content"));

        Path tempFile = Files.createTempFile("facade-log-test-", ".html");
        try {
            facade.exportHTML(doc, tempFile.toString());

            String logs = logCapture.toString();
            assertTrue(logs.contains("EXPORT_STARTED"));
            assertTrue(logs.contains(tempFile.toString()));
            assertTrue(logs.contains("EXPORT_COMPLETED"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void facade_exportWithInvalidPath_throws() {
        Document doc = new Document("Test");
        doc.addElement(new TextElement("content"));

        DocumentExporterFacade facade = new DocumentExporterFacade();
        assertThrows(RuntimeException.class, () ->
                facade.export(doc, "/nonexistent/dir/file.html", new HTMLExportStrategy())
        );
    }

    @Test
    void facade_styledDocumentExports() throws IOException {
        Document doc = new Document("Styled Doc");
        doc.addElement(new BoldDecorator(new TextElement("bold text")));
        doc.addElement(new ItalicDecorator(new TextElement("italic text")));
        doc.addElement(new ColorDecorator(new TextElement("colored text"), "blue"));

        Path tempFile = Files.createTempFile("facade-styled-", ".html");
        try {
            new DocumentExporterFacade().exportHTML(doc, tempFile.toString());

            String content = Files.readString(tempFile);
            assertTrue(content.contains("<b>"));
            assertTrue(content.contains("<i>"));
            assertTrue(content.contains("color:blue"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void facade_mixedContentExportsToMarkdown() throws IOException {
        Document doc = new Document("Mixed Doc");
        doc.addElement(new TextElement("A paragraph"));
        doc.addElement(new ImageElement("img.png", 300, 200));
        doc.addElement(new CodeElement("python", "print('hello')"));

        Path tempFile = Files.createTempFile("facade-mixed-", ".md");
        try {
            new DocumentExporterFacade().exportMarkdown(doc, tempFile.toString());

            String content = Files.readString(tempFile);
            assertTrue(content.contains("A paragraph"));
            assertTrue(content.contains("![img.png](img.png)"));
            assertTrue(content.contains("```python"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void facade_allExportMethodsUseSameSignature() throws IOException {
        Document doc = new Document("Unified");
        doc.addElement(new TextElement("content"));

        Path htmlFile = Files.createTempFile("unified-", ".html");
        Path mdFile = Files.createTempFile("unified-", ".md");
        Path pdfFile = Files.createTempFile("unified-", ".pdf");
        try {
            DocumentExporterFacade facade = new DocumentExporterFacade();
            facade.exportHTML(doc, htmlFile.toString());
            facade.exportMarkdown(doc, mdFile.toString());
            facade.exportPDF(doc, pdfFile.toString());

            assertTrue(Files.size(htmlFile) > 0);
            assertTrue(Files.size(mdFile) > 0);
            assertTrue(Files.size(pdfFile) > 0);
        } finally {
            Files.deleteIfExists(htmlFile);
            Files.deleteIfExists(mdFile);
            Files.deleteIfExists(pdfFile);
        }
    }
}
