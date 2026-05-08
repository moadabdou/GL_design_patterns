package com.glproject.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableElement implements Element {

    private final List<List<String>> rows;

    public TableElement(List<List<String>> rows) {
        this.rows = deepCopy(rows);
    }

    public List<List<String>> getRows() {
        return deepCopy(rows);
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return rows.isEmpty() ? 0 : rows.getFirst().size();
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("[Table]\n");
        for (List<String> row : rows) {
            sb.append("  | ");
            sb.append(String.join(" | ", row));
            sb.append(" |\n");
        }
        return sb.toString();
    }

    @Override
    public TableElement clone() {
        return new TableElement(rows);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableElement that)) return false;
        return Objects.equals(rows, that.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }

    private static List<List<String>> deepCopy(List<List<String>> source) {
        List<List<String>> copy = new ArrayList<>();
        for (List<String> row : source) {
            copy.add(new ArrayList<>(row));
        }
        return copy;
    }
}
