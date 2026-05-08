package com.glproject.facade;

import com.glproject.behavioral.ExportStrategy;
import com.glproject.behavioral.HTMLExportStrategy;
import com.glproject.behavioral.MarkdownExportStrategy;
import com.glproject.behavioral.PDFExportStrategy;
import com.glproject.domain.Document;
import com.glproject.util.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentExporterFacade {

    private static final Logger logger = Logger.getInstance();

    public void export(Document document, String filePath, ExportStrategy strategy) {
        logger.info("Exporting document '{}' to {} using {}", document.getTitle(), filePath,
                strategy.getClass().getSimpleName());
        byte[] content = strategy.render(document);
        try {
            Files.write(Path.of(filePath), content);
            logger.info("Successfully exported document '{}' to {}", document.getTitle(), filePath);
        } catch (IOException e) {
            logger.error("Failed to export document '{}' to {}", document.getTitle(), filePath);
            throw new RuntimeException("Failed to export to " + filePath, e);
        }
    }

    public void exportPDF(Document document, String filePath) {
        export(document, filePath, new PDFExportStrategy());
    }

    public void exportHTML(Document document, String filePath) {
        export(document, filePath, new HTMLExportStrategy());
    }

    public void exportMarkdown(Document document, String filePath) {
        export(document, filePath, new MarkdownExportStrategy());
    }
}
