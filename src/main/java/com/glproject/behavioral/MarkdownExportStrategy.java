package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class MarkdownExportStrategy implements ExportStrategy {

    @Override
    public byte[] render(Document document) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(document.getTitle()).append("\n\n");

        for (Element element : document) {
            sb.append(renderElement(element)).append("\n\n");
        }

        return sb.toString().stripTrailing().getBytes(StandardCharsets.UTF_8);
    }

    private String renderElement(Element element) {
        if (element instanceof BoldDecorator dec) {
            return "**" + renderElement(dec.getWrapped()) + "**";
        }
        if (element instanceof ItalicDecorator dec) {
            return "*" + renderElement(dec.getWrapped()) + "*";
        }
        if (element instanceof UnderlineDecorator dec) {
            return "<u>" + renderElement(dec.getWrapped()) + "</u>";
        }
        if (element instanceof ColorDecorator dec) {
            return renderElement(dec.getWrapped());
        }
        if (element instanceof TextElement text) {
            return text.getContent();
        }
        if (element instanceof ImageElement img) {
            return "![" + img.getImagePath() + "](" + img.getImagePath() + ")";
        }
        if (element instanceof TableElement table) {
            return renderTable(table);
        }
        if (element instanceof CodeElement code) {
            return "```" + code.getLanguage() + "\n" + code.getCode() + "\n```";
        }
        return element.render();
    }

    private String renderTable(TableElement table) {
        List<List<String>> rows = table.getRows();
        if (rows.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int cols = rows.getFirst().size();

        String headerRow = "| " + rows.getFirst().stream()
                .map(cell -> cell.replace("|", "\\|"))
                .collect(Collectors.joining(" | ")) + " |";
        sb.append(headerRow).append("\n");

        sb.append("|");
        for (int i = 0; i < cols; i++) {
            sb.append(" --- |");
        }
        sb.append("\n");

        for (int i = 1; i < rows.size(); i++) {
            String rowStr = "| " + rows.get(i).stream()
                    .map(cell -> cell.replace("|", "\\|"))
                    .collect(Collectors.joining(" | ")) + " |";
            sb.append(rowStr).append("\n");
        }

        return sb.toString().stripTrailing();
    }
}
