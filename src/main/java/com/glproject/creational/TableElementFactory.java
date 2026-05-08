package com.glproject.creational;

import com.glproject.domain.Element;
import com.glproject.domain.TableElement;
import java.util.*;

public class TableElementFactory extends ElementFactory {

    @Override
    public Element createElement(Map<String, String> properties) {
        String rowsStr = properties.get("rows");
        if (rowsStr == null || rowsStr.isBlank()) {
            throw new IllegalArgumentException("'rows' property is required for table element");
        }

        List<List<String>> rows = new ArrayList<>();
        for (String rowStr : rowsStr.split(";")) {
            List<String> row = new ArrayList<>();
            for (String cell : rowStr.split(",")) {
                row.add(cell.trim());
            }
            rows.add(row);
        }
        return new TableElement(rows);
    }

    @Override
    public Class<? extends Element> getElementType() {
        return TableElement.class;
    }
}
