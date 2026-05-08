package com.thirdparty.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.IOException;

public class PDFGenerator {

    private final String filePath;
    private String header;
    private String footer;

    public PDFGenerator(String filePath) {
        this.filePath = filePath;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void generatePDF(String content) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float leading = 14.5f;

                if (header != null) {
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                    cs.beginText();
                    cs.newLineAtOffset(margin, yStart);
                    cs.showText(header);
                    cs.endText();
                    yStart -= 30;
                }

                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                cs.setLeading(leading);
                cs.beginText();
                cs.newLineAtOffset(margin, yStart);

                String[] lines = content.split("\n");
                for (String line : lines) {
                    cs.showText(line);
                    cs.newLine();
                }
                cs.endText();

                if (footer != null) {
                    float footerY = margin;
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 9);
                    cs.beginText();
                    cs.newLineAtOffset(margin, footerY);
                    cs.showText(footer);
                    cs.endText();
                }
            }

            document.save(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
