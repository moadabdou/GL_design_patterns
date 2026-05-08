package com.thirdparty.pdf;

/**
 * Mock 3rd-party PDF library.
 * Simulates an external PDF generation API to be adapted later.
 */
public class PDFGenerator {

    public boolean openDocument(String filePath) {
        System.out.println("[PDFGenerator] Opening document: " + filePath);
        return true;
    }

    public boolean addPage() {
        System.out.println("[PDFGenerator] Adding new page");
        return true;
    }

    public boolean setFont(String fontName, int size) {
        System.out.println("[PDFGenerator] Setting font: " + fontName + ", size: " + size);
        return true;
    }

    public boolean drawText(String text, float x, float y) {
        System.out.println("[PDFGenerator] Drawing text '" + text + "' at (" + x + ", " + y + ")");
        return true;
    }

    public boolean drawImage(String imagePath, float x, float y, float width, float height) {
        System.out.println("[PDFGenerator] Drawing image '" + imagePath + "' at (" + x + ", " + y +
                ") size (" + width + "x" + height + ")");
        return true;
    }

    public boolean save() {
        System.out.println("[PDFGenerator] Saving document");
        return true;
    }

    public boolean close() {
        System.out.println("[PDFGenerator] Closing document");
        return true;
    }
}
