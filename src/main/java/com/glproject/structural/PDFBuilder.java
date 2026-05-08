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
    float getPageHeight();
    byte[] toByteArray();
}
