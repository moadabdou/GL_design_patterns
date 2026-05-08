package com.glproject.behavioral;

import com.glproject.domain.Document;
import com.glproject.domain.Element;

public class AddElementCommand implements Command {
    private final Document document;
    private final Element element;
    private int insertionIndex = -1;

    public AddElementCommand(Document document, Element element) {
        this.document = document;
        this.element = element;
    }

    @Override
    public void execute() {
        insertionIndex = document.getElementCount();
        document.addElement(element);
    }

    @Override
    public void undo() {
        if (insertionIndex >= 0) {
            document.removeElement(insertionIndex);
        }
    }

    @Override
    public String getDescription() {
        if (insertionIndex >= 0) {
            return "Add element at index " + insertionIndex;
        }
        return "Add element (not executed)";
    }
}
