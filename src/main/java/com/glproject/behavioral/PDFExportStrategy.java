package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;

public class PDFExportStrategy implements ExportStrategy {

    @Override
    public byte[] render(Document document) {
        PDFBuilder pdf = new PDFLibraryAdapter();
        pdf.newPage();

        float margin = 50;
        float y = margin;
        float pageHeight = pdf.getPageHeight();
        float bottomMargin = 50;
        float leading = 16;

        pdf.setFont(PDFBuilder.Font.HELVETICA_BOLD, 18);
        pdf.writeText(document.getTitle(), margin, y);
        y += 30;

        for (Element element : document) {
            if (y > pageHeight - bottomMargin - leading) {
                pdf.newPage();
                y = margin;
            }

            FontChoice fc = resolveFont(element);
            pdf.setFont(fc.font, 12);
            pdf.writeText(fc.text, margin, y);
            y += leading;
        }

        return pdf.toByteArray();
    }

    private FontChoice resolveFont(Element element) {
        boolean bold = false;
        boolean italic = false;
        Element current = element;

        while (current instanceof ElementDecorator dec) {
            if (dec instanceof BoldDecorator) bold = true;
            if (dec instanceof ItalicDecorator) italic = true;
            current = dec.getWrapped();
        }

        PDFBuilder.Font f;
        if (bold && italic) f = PDFBuilder.Font.HELVETICA_BOLD_OBLIQUE;
        else if (bold) f = PDFBuilder.Font.HELVETICA_BOLD;
        else if (italic) f = PDFBuilder.Font.HELVETICA_OBLIQUE;
        else f = PDFBuilder.Font.HELVETICA;

        return new FontChoice(f, current.render());
    }

    private record FontChoice(PDFBuilder.Font font, String text) {}
}
