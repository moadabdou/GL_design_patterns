package com.glproject.behavioral;

import com.glproject.domain.*;
import com.glproject.structural.*;

import java.awt.Color;
import java.util.List;

public class PDFExportStrategy implements ExportStrategy {

    @Override
    public byte[] render(Document document) {
        PDFBuilder pdf = new PDFLibraryAdapter();
        pdf.newPage();

        float margin = 50;
        float y = margin;
        float pageHeight = pdf.getPageHeight();
        float pageWidth = pageHeight;
        float bottomMargin = 50;
        float leading = 16;
        float usableWidth = pageWidth - 2 * margin;

        pdf.setFont(PDFBuilder.Font.HELVETICA_BOLD, 18);
        pdf.writeText(document.getTitle(), margin, y);
        y += 30;

        for (Element element : document) {
            boolean bold = false;
            boolean italic = false;
            Color awtColor = Color.BLACK;
            Element current = element;

            // Unwrap decorators to determine final styling
            while (current instanceof ElementDecorator dec) {
                if (dec instanceof BoldDecorator) bold = true;
                if (dec instanceof ItalicDecorator) italic = true;
                if (dec instanceof ColorDecorator col) awtColor = parseColor(col.getColor());
                current = dec.getWrapped();
            }

            PDFBuilder.Font font;
            if (bold && italic) font = PDFBuilder.Font.HELVETICA_BOLD_OBLIQUE;
            else if (bold) font = PDFBuilder.Font.HELVETICA_BOLD;
            else if (italic) font = PDFBuilder.Font.HELVETICA_OBLIQUE;
            else font = PDFBuilder.Font.HELVETICA;

            pdf.setColor(awtColor.getRed() / 255f, awtColor.getGreen() / 255f, awtColor.getBlue() / 255f);

            if (current instanceof ImageElement img) {
                float scale = Math.min(1, usableWidth / img.getWidth());
                float drawWidth = img.getWidth() * scale;
                float drawHeight = img.getHeight() * scale;

                if (y + drawHeight > pageHeight - bottomMargin) {
                    pdf.newPage();
                    y = margin;
                }
                try {
                    pdf.drawImage(img.getImagePath(), margin, y, drawWidth, drawHeight);
                } catch (Exception e) {
                    pdf.setFont(PDFBuilder.Font.HELVETICA, 12);
                    pdf.writeText("[Image: " + img.getImagePath() + "]", margin, y);
                }
                y += drawHeight + leading;
            } else if (current instanceof TableElement table) {
                List<List<String>> rows = table.getRows();
                if (rows.isEmpty()) continue;

                int cols = rows.getFirst().size();
                float cellPadding = 4;
                float rowHeight = leading + 4;

                pdf.setFont(font, 12);

                float[] colWidths = new float[cols];
                for (List<String> row : rows) {
                    for (int i = 0; i < cols && i < row.size(); i++) {
                        float w = pdf.getTextWidth(row.get(i)) + 2 * cellPadding;
                        if (w > colWidths[i]) colWidths[i] = w;
                    }
                }

                for (int i = 0; i < cols; i++) {
                    if (colWidths[i] < 20) colWidths[i] = 20;
                }

                float totalWidth = 0;
                for (int i = 0; i < cols; i++) totalWidth += colWidths[i];

                if (totalWidth > usableWidth) {
                    float scale = usableWidth / totalWidth;
                    for (int i = 0; i < cols; i++) colWidths[i] *= scale;
                }

                float tableHeight = rowHeight * rows.size();
                if (y + tableHeight > pageHeight - bottomMargin) {
                    pdf.newPage();
                    y = margin;
                }

                pdf.setLineWidth(0.5f);

                for (List<String> row : rows) {
                    float cellX = margin;
                    float cellY = y;

                    for (int i = 0; i < cols; i++) {
                        String cellText = i < row.size() ? row.get(i) : "";
                        float cellW = colWidths[i];

                        pdf.drawLine(cellX, cellY, cellX + cellW, cellY);
                        pdf.drawLine(cellX, cellY, cellX, cellY + rowHeight);
                        pdf.drawLine(cellX + cellW, cellY, cellX + cellW, cellY + rowHeight);
                        pdf.drawLine(cellX, cellY + rowHeight, cellX + cellW, cellY + rowHeight);

                        float textY = cellY + pdf.getFontAscent() + 2;
                        pdf.writeText(cellText, cellX + cellPadding, textY);

                        cellX += cellW;
                    }

                    y += rowHeight;
                }
                y += leading;
            } else {
                pdf.setFont(font, 12);
                String text = current.render();
                String[] lines = text.split("\n", -1);
                for (String line : lines) {
                    if (y > pageHeight - bottomMargin - leading) {
                        pdf.newPage();
                        y = margin;
                    }
                    pdf.writeText(line, margin, y);
                    y += leading;
                }
            }
        }

        return pdf.toByteArray();
    }

    private static Color parseColor(String color) {
        return switch (color.toLowerCase()) {
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            case "yellow" -> Color.YELLOW;
            case "orange" -> Color.ORANGE;
            case "purple" -> new Color(128, 0, 128);
            case "cyan" -> Color.CYAN;
            case "magenta" -> Color.MAGENTA;
            case "pink" -> Color.PINK;
            case "gray", "grey" -> Color.GRAY;
            case "black" -> Color.BLACK;
            case "white" -> Color.WHITE;
            default -> {
                try {
                    String hex = color.startsWith("#") ? color : "#" + color;
                    yield Color.decode(hex);
                } catch (Exception e) {
                    yield Color.BLACK;
                }
            }
        };
    }
}
