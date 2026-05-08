package com.glproject.structural;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFLibraryAdapter implements PDFBuilder {

    private final PDDocument document;
    private PDPageContentStream contentStream;
    private PDRectangle pageSize;

    public PDFLibraryAdapter() {
        this.document = new PDDocument();
    }

    @Override
    public void newPage() {
        closeContentStream();
        pageSize = PDRectangle.A4;
        PDPage page = new PDPage(pageSize);
        document.addPage(page);
        try {
            contentStream = new PDPageContentStream(document, page);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create PDF page", e);
        }
    }

    @Override
    public void setFont(PDFBuilder.Font font, float size) {
        try {
            contentStream.setFont(new PDType1Font(toStandard14(font)), size);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set font", e);
        }
    }

    @Override
    public void writeText(String text, float x, float y) {
        try {
            float pdfY = pageSize.getHeight() - y;
            contentStream.beginText();
            contentStream.newLineAtOffset(x, pdfY);
            contentStream.showText(text);
            contentStream.endText();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write text", e);
        }
    }

    @Override
    public void drawImage(String imagePath, float x, float y, float width, float height) {
        try {
            PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
            float pdfY = pageSize.getHeight() - y;
            contentStream.drawImage(image, x, pdfY - height, width, height);
        } catch (IOException e) {
            throw new RuntimeException("Failed to draw image: " + imagePath, e);
        }
    }

    @Override
    public float getPageHeight() {
        return PDRectangle.A4.getHeight();
    }

    @Override
    public byte[] toByteArray() {
        try {
            closeContentStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF bytes", e);
        }
    }

    private void closeContentStream() {
        try {
            if (contentStream != null) {
                contentStream.close();
                contentStream = null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to close content stream", e);
        }
    }

    private Standard14Fonts.FontName toStandard14(PDFBuilder.Font font) {
        return switch (font) {
            case HELVETICA -> Standard14Fonts.FontName.HELVETICA;
            case HELVETICA_BOLD -> Standard14Fonts.FontName.HELVETICA_BOLD;
            case HELVETICA_OBLIQUE -> Standard14Fonts.FontName.HELVETICA_OBLIQUE;
            case HELVETICA_BOLD_OBLIQUE -> Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE;
            case TIMES_ROMAN -> Standard14Fonts.FontName.TIMES_ROMAN;
            case TIMES_BOLD -> Standard14Fonts.FontName.TIMES_BOLD;
            case TIMES_ITALIC -> Standard14Fonts.FontName.TIMES_ITALIC;
            case TIMES_BOLD_ITALIC -> Standard14Fonts.FontName.TIMES_BOLD_ITALIC;
            case COURIER -> Standard14Fonts.FontName.COURIER;
            case COURIER_BOLD -> Standard14Fonts.FontName.COURIER_BOLD;
            case COURIER_OBLIQUE -> Standard14Fonts.FontName.COURIER_OBLIQUE;
            case COURIER_BOLD_OBLIQUE -> Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE;
        };
    }
}
