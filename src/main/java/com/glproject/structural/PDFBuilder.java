package com.glproject.structural;

public interface PDFBuilder {

    enum Font {
        HELVETICA,
        HELVETICA_BOLD,
        HELVETICA_OBLIQUE,
        HELVETICA_BOLD_OBLIQUE,
        TIMES_ROMAN,
        TIMES_BOLD,
        TIMES_ITALIC,
        TIMES_BOLD_ITALIC,
        COURIER,
        COURIER_BOLD,
        COURIER_OBLIQUE,
        COURIER_BOLD_OBLIQUE
    }

    void newPage();
    void setFont(Font font, float size);
    void writeText(String text, float x, float y);
    void drawImage(String imagePath, float x, float y, float width, float height);
    void drawLine(float x1, float y1, float x2, float y2);
    void setLineWidth(float width);
    void setColor(float r, float g, float b);
    float getTextWidth(String text);
    float getFontAscent();
    float getFontDescent();
    float getPageHeight();
    byte[] toByteArray();
}
