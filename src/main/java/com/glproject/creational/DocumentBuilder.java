package com.glproject.creational;

import com.glproject.domain.*;
import java.util.List;
import java.util.Map;

public class DocumentBuilder {

    private Document document;

    public DocumentBuilder() {
        this.document = new Document("");
    }

    public DocumentBuilder setTitle(String title) {
        document.setTitle(title);
        return this;
    }

    public DocumentBuilder addParagraph(String text) {
        TextElement element = ElementFactory.createElement(TextElement.class, Map.of("content", text));
        document.addElement(element);
        return this;
    }

    public DocumentBuilder addImage(String path, int width, int height) {
        ImageElement element = ElementFactory.createElement(ImageElement.class,
                Map.of("path", path, "width", String.valueOf(width), "height", String.valueOf(height)));
        document.addElement(element);
        return this;
    }

    public DocumentBuilder addTable(List<List<String>> rows) {
        StringBuilder rowsStr = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            if (i > 0) rowsStr.append(";");
            List<String> row = rows.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (j > 0) rowsStr.append(",");
                rowsStr.append(row.get(j));
            }
        }
        TableElement element = ElementFactory.createElement(TableElement.class, Map.of("rows", rowsStr.toString()));
        document.addElement(element);
        return this;
    }

    public Document build() {
        Document result = document;
        document = new Document("");
        return result;
    }
}
