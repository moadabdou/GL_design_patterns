package com.glproject.structural;

import com.glproject.domain.Document;
import com.glproject.domain.Element;
import com.thirdparty.pdf.PDFGenerator;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PDFLibraryAdapter implements Exporter {

    @Override
    public void export(Document document, OutputStream output) {
        if (document == null) {
            throw new IllegalArgumentException("Document must not be null");
        }
        if (output == null) {
            throw new IllegalArgumentException("OutputStream must not be null");
        }

        try {
            Path tempFile = Files.createTempFile("pdf-export-", ".pdf");
            tempFile.toFile().deleteOnExit();

            PDFGenerator pdfGenerator = new PDFGenerator(tempFile.toString());
            pdfGenerator.setHeader(document.getTitle());
            String content = convertDocumentToText(document);
            pdfGenerator.generatePDF(content);

            Files.copy(tempFile, output);
            Files.delete(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export PDF", e);
        }
    }

    private String convertDocumentToText(Document document) {
        StringBuilder sb = new StringBuilder();
        for (Element element : document) {
            if (!sb.isEmpty()) {
                sb.append("\n\n");
            }
            sb.append(element.render());
        }
        return sb.toString();
    }
}
