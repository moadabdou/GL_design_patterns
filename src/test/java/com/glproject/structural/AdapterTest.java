package com.glproject.structural;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AdapterTest {

    @Test
    void adapter_createsPdfWithText() throws IOException {
        PDFBuilder pdf = new PDFLibraryAdapter();
        pdf.newPage();
        pdf.setFont(PDFBuilder.Font.HELVETICA, 12);
        pdf.writeText("Hello World", 50, 50);

        byte[] bytes = pdf.toByteArray();
        assertTrue(bytes.length > 0);

        try (PDDocument doc = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            assertTrue(text.contains("Hello World"));
        }
    }

    @Test
    void adapter_multiplePages() throws IOException {
        PDFBuilder pdf = new PDFLibraryAdapter();
        pdf.newPage();
        pdf.setFont(PDFBuilder.Font.HELVETICA, 12);
        pdf.writeText("Page 1", 50, 50);

        pdf.newPage();
        pdf.setFont(PDFBuilder.Font.HELVETICA, 12);
        pdf.writeText("Page 2", 50, 50);

        byte[] bytes = pdf.toByteArray();
        try (PDDocument doc = Loader.loadPDF(bytes)) {
            assertEquals(2, doc.getNumberOfPages());
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(1);
            assertTrue(stripper.getText(doc).contains("Page 1"));

            stripper.setStartPage(2);
            stripper.setEndPage(2);
            assertTrue(stripper.getText(doc).contains("Page 2"));
        }
    }

    @Test
    void adapter_differentFonts() throws IOException {
        PDFBuilder pdf = new PDFLibraryAdapter();
        pdf.newPage();
        pdf.setFont(PDFBuilder.Font.HELVETICA_BOLD, 16);
        pdf.writeText("Bold Title", 50, 50);

        pdf.setFont(PDFBuilder.Font.HELVETICA_OBLIQUE, 10);
        pdf.writeText("Italic note", 50, 80);

        byte[] bytes = pdf.toByteArray();
        try (PDDocument doc = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            assertTrue(text.contains("Bold Title"));
            assertTrue(text.contains("Italic note"));
        }
    }

    @Test
    void adapter_toByteArrayTwice_throws() {
        PDFBuilder pdf = new PDFLibraryAdapter();
        pdf.newPage();
        pdf.setFont(PDFBuilder.Font.HELVETICA, 12);
        pdf.writeText("content", 50, 50);
        pdf.toByteArray();

        assertThrows(RuntimeException.class, pdf::toByteArray);
    }

    @Test
    void adapter_implementsPDFBuilder() {
        assertInstanceOf(PDFBuilder.class, new PDFLibraryAdapter());
    }

    @Test
    void adapter_getPageHeight() {
        PDFBuilder pdf = new PDFLibraryAdapter();
        assertTrue(pdf.getPageHeight() > 800);
    }
}
