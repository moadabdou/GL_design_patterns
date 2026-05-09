package com.glproject.facade;

import com.glproject.behavioral.DocumentEvent;
import com.glproject.behavioral.DocumentEventBus;
import com.glproject.behavioral.ExportStrategy;
import com.glproject.behavioral.HTMLExportStrategy;
import com.glproject.behavioral.MarkdownExportStrategy;
import com.glproject.behavioral.PDFExportStrategy;
import com.glproject.domain.Document;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentExporterFacade {

    private DocumentEventBus eventBus = new DocumentEventBus();

    public DocumentExporterFacade() {
    }

    public DocumentExporterFacade(DocumentEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setEventBus(DocumentEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public DocumentEventBus getEventBus() {
        return eventBus;
    }

    public void export(Document document, String filePath, ExportStrategy strategy) {
        eventBus.notifyObservers(DocumentEvent.EXPORT_STARTED, filePath);
        byte[] content = strategy.render(document);
        try {
            Files.write(Path.of(filePath), content);
            eventBus.notifyObservers(DocumentEvent.EXPORT_COMPLETED, filePath);
        } catch (IOException e) {
            eventBus.notifyObservers(DocumentEvent.EXPORT_FAILED, filePath);
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
