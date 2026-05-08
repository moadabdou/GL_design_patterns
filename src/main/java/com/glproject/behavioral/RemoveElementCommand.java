package com.glproject.behavioral;

import com.glproject.domain.Document;
import com.glproject.domain.Element;

public class RemoveElementCommand implements Command {
    private final Document document;
    private final int index;
    private Element removedElement;

    public RemoveElementCommand(Document document, int index) {
        this.document = document;
        this.index = index;
    }

    @Override
    public void execute() {
        removedElement = document.removeElement(index);
    }

    @Override
    public void undo() {
        document.addElement(index, removedElement);
    }

    @Override
    public String getDescription() {
        return "Remove element at index " + index;
    }
}
