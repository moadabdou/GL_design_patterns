package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HTMLExportStrategy implements ExportStrategy {

    @Override
    public byte[] render(Document document) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n<head>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<title>").append(escapeHtml(document.getTitle())).append("</title>\n");
        sb.append("</head>\n<body>\n");
        sb.append("<h1>").append(escapeHtml(document.getTitle())).append("</h1>\n");

        for (Element element : document) {
            sb.append(renderElement(element)).append("\n");
        }

        sb.append("</body>\n</html>");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String renderElement(Element element) {
        if (element instanceof BoldDecorator dec) {
            return "<b>" + renderElement(dec.getWrapped()) + "</b>";
        }
        if (element instanceof ItalicDecorator dec) {
            return "<i>" + renderElement(dec.getWrapped()) + "</i>";
        }
        if (element instanceof UnderlineDecorator dec) {
            return "<u>" + renderElement(dec.getWrapped()) + "</u>";
        }
        if (element instanceof ColorDecorator dec) {
            return "<span style=\"color:" + dec.getColor() + "\">" + renderElement(dec.getWrapped()) + "</span>";
        }
        if (element instanceof TextElement text) {
            return "<p>" + escapeHtml(text.getContent()) + "</p>";
        }
        if (element instanceof ImageElement img) {
            return "<img src=\"" + escapeHtml(img.getImagePath()) + "\" width=\"" + img.getWidth()
                    + "\" height=\"" + img.getHeight() + "\" alt=\"" + escapeHtml(img.getImagePath()) + "\">";
        }
        if (element instanceof TableElement table) {
            return renderTable(table);
        }
        if (element instanceof CodeElement code) {
            return "<pre><code>" + escapeHtml(code.getCode()) + "</code></pre>";
        }
        return "<p>" + escapeHtml(element.render()) + "</p>";
    }

    private String renderTable(TableElement table) {
        StringBuilder sb = new StringBuilder("<table>\n");
        for (List<String> row : table.getRows()) {
            sb.append("<tr>\n");
            for (String cell : row) {
                sb.append("<td>").append(escapeHtml(cell)).append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
