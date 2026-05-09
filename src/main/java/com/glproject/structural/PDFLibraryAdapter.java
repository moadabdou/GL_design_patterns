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
    private PDType1Font currentFont;
    private float currentFontSize;

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
            currentFont = new PDType1Font(toStandard14(font));
            currentFontSize = size;
            contentStream.setFont(currentFont, size);
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
    public void drawLine(float x1, float y1, float x2, float y2) {
        try {
            float pdfY1 = pageSize.getHeight() - y1;
            float pdfY2 = pageSize.getHeight() - y2;
            contentStream.moveTo(x1, pdfY1);
            contentStream.lineTo(x2, pdfY2);
            contentStream.stroke();
        } catch (IOException e) {
            throw new RuntimeException("Failed to draw line", e);
        }
    }

    @Override
    public void setLineWidth(float width) {
        try {
            contentStream.setLineWidth(width);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set line width", e);
        }
    }

    @Override
    public void setColor(float r, float g, float b) {
        try {
            contentStream.setNonStrokingColor(r, g, b);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set color", e);
        }
    }

    @Override
    public float getTextWidth(String text) {
        if (currentFont == null) return 0;
        try {
            return currentFont.getStringWidth(text) * currentFontSize / 1000f;
        } catch (IOException e) {
            throw new RuntimeException("Failed to get text width", e);
        }
    }

    @Override
    public float getFontAscent() {
        if (currentFont == null) return 0;
        return currentFont.getFontDescriptor().getAscent() * currentFontSize / 1000f;
    }

    @Override
    public float getFontDescent() {
        if (currentFont == null) return 0;
        return Math.abs(currentFont.getFontDescriptor().getDescent()) * currentFontSize / 1000f;
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
