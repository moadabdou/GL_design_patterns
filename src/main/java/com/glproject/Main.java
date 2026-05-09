package com.glproject;

import com.glproject.behavioral.*;
import com.glproject.creational.*;
import com.glproject.domain.*;
import com.glproject.facade.DocumentExporterFacade;
import com.glproject.structural.*;
import com.glproject.util.Configuration;
import com.glproject.util.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Logger logger = Logger.getInstance();
    private static final String SEPARATOR = "============================================================";

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Path.of("output"));

        printBanner();

        // =========================================================
        // Pattern: Singleton — ensures a class has only one instance
        // =========================================================
        section("Singleton");
        Configuration config = Configuration.getInstance();
        String appName = config.get("app.name");
        logger.info("Application name: {}", appName != null ? appName : "Design Patterns Demo");

        // =========================================================
        // Pattern: Factory Method — creates objects without specifying
        // the exact class to instantiate
        // =========================================================
        section("Factory Method");
        TextElement textEl = ElementFactory.createElement(TextElement.class,
                Map.of("content", "Factory-created paragraph"));
        ImageElement imgEl = ElementFactory.createElement(ImageElement.class,
                Map.of("path", "photo.jpg", "width", "800", "height", "600"));
        System.out.println("  Created: " + textEl.render());
        System.out.println("  Created: " + imgEl.render());

        // =========================================================
        // Pattern: Builder — constructs a complex object step by step
        // =========================================================
        section("Builder");
        Document doc = new DocumentBuilder()
                .setTitle("Design Patterns Demo Document")
                .addParagraph("This document demonstrates all 10 design patterns.")
                .addImage("assets/image.png", 800, 200)
                .addTable(List.of(
                        List.of("Pattern", "Category", "Status"),
                        List.of("Singleton", "Creational", "Implemented"),
                        List.of("Factory Method", "Creational", "Implemented"),
                        List.of("Builder", "Creational", "Implemented"),
                        List.of("Decorator", "Structural", "Implemented"),
                        List.of("Adapter", "Structural", "Implemented"),
                        List.of("Facade", "Structural", "Implemented"),
                        List.of("Strategy", "Behavioral", "Implemented"),
                        List.of("Observer", "Behavioral", "Implemented"),
                        List.of("Command", "Behavioral", "Implemented"),
                        List.of("Iterator", "Behavioral", "Implemented")
                ))
                .build();
        System.out.println("  Built document: \"" + doc.getTitle() + "\" with "
                + doc.getElementCount() + " elements");

        // =========================================================
        // Pattern: Iterator — provides sequential access to elements
        // without exposing internal structure
        // =========================================================
        section("Iterator");
        System.out.println("  All elements (for-each loop):");
        for (Element e : doc) {
            System.out.println("    - " + e.render());
        }

        System.out.println("  Text-only elements (filtered iterator):");
        DocumentIterator textIt = doc.iterator(ElementTypeFilter.textOnly());
        while (textIt.hasNext()) {
            System.out.println("    - " + textIt.next().render());
        }

        // =========================================================
        // Pattern: Decorator — dynamically adds behavior to objects
        // =========================================================
        section("Decorator");
        Element plain = new TextElement("Important message");
        Element bold = new BoldDecorator(plain);
        Element boldItalic = new ItalicDecorator(bold);
        Element styled = new ColorDecorator(boldItalic, "red");
        doc.addElement(styled);
        System.out.println("  Plain:  " + plain.render());
        System.out.println("  Bold:   " + bold.render());
        System.out.println("  Bold+Italic: " + boldItalic.render());
        System.out.println("  Styled (Bold+Italic+Red): " + styled.render());

        // =========================================================
        // Pattern: Adapter — allows incompatible interfaces to work
        // together
        // =========================================================
        section("Adapter");
        PDFBuilder pdfBuilder = new PDFLibraryAdapter();
        pdfBuilder.newPage();
        pdfBuilder.setFont(PDFBuilder.Font.HELVETICA_BOLD, 16);
        pdfBuilder.writeText("Adapter Pattern Demo", 50, 50);
        byte[] pdfBytes = pdfBuilder.toByteArray();
        System.out.println("  Generated PDF via PDFLibraryAdapter: "
                + pdfBytes.length + " bytes");

        // =========================================================
        // Pattern: Facade + Strategy — simplified interface +
        // interchangeable algorithms
        // =========================================================
        section("Facade + Strategy");
        DocumentExporterFacade facade = new DocumentExporterFacade();
        facade.exportHTML(doc, "output/demo.html");
        System.out.println("  Exported: output/demo.html (HTMLExportStrategy)");
        facade.export(doc, "output/demo.md", new MarkdownExportStrategy());
        System.out.println("  Exported: output/demo.md (MarkdownExportStrategy)");
        try {
            facade.exportPDF(doc, "output/demo.pdf");
            System.out.println("  Exported: output/demo.pdf (PDFExportStrategy)");
        } catch (Exception e) {
            System.out.println("  PDF export skipped: " + e.getMessage());
        }

        // =========================================================
        // Pattern: Observer — notifies dependents of state changes
        // =========================================================
        section("Observer");
        ConsoleObserver consoleObs = new ConsoleObserver();
        LoggingObserver loggingObs = new LoggingObserver();
        doc.getEventBus().subscribe(consoleObs);
        doc.getEventBus().subscribe(loggingObs);
        System.out.println("  Subscribed ConsoleObserver and LoggingObserver");
        System.out.println("  Adding element (triggers events)...");
        doc.addElement(new TextElement("Observer notification test"));
        System.out.println("  Removing element (triggers events)...");
        doc.removeElement(doc.getElementCount() - 1);
        System.out.println("  Observers received notifications above ^");
        doc.getEventBus().unsubscribe(consoleObs);
        doc.getEventBus().unsubscribe(loggingObs);

        // =========================================================
        // Pattern: Command — encapsulates operations as objects,
        // enabling undo/redo
        // =========================================================
        section("Command");
        CommandHistory history = new CommandHistory();
        System.out.println("  Initial element count: " + doc.getElementCount());
        Command addCmd = new AddElementCommand(doc,
                new TextElement("Command pattern test"));
        history.executeCommand(addCmd);
        System.out.println("  After execute(): " + doc.getElementCount()
                + " elements (added one)");
        history.undo();
        System.out.println("  After undo():     " + doc.getElementCount()
                + " elements (removed)");
        history.redo();
        System.out.println("  After redo():     " + doc.getElementCount()
                + " elements (re-added)");

        // Cleanup the test element
        doc.removeElement(doc.getElementCount() - 1);
        System.out.println("  Cleaned up test element");

        printFooter();
    }

    private static void printBanner() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  DESIGN PATTERNS DEMO");
        System.out.println("  Demonstrating 10 patterns in a cohesive document editor");
        System.out.println(SEPARATOR + "\n");
    }

    private static void section(String name) {
        System.out.println("\n--- " + name + " ---");
    }

    private static void printFooter() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  Demo completed successfully.");
        System.out.println("  Generated files are in the output/ directory.");
        System.out.println(SEPARATOR + "\n");
    }
}
