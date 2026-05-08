package com.glproject.behavioral;

import com.glproject.domain.Document;
import com.glproject.domain.Element;

public class ModifyStyleCommand implements Command {
    private final Document document;
    private final int index;
    private final Element oldElement;
    private final Element newElement;

    public ModifyStyleCommand(Document document, int index, Element oldElement, Element newElement) {
        this.document = document;
        this.index = index;
        this.oldElement = oldElement;
        this.newElement = newElement;
    }

    @Override
    public void execute() {
        document.setElement(index, newElement);
    }

    @Override
    public void undo() {
        document.setElement(index, oldElement);
    }

    @Override
    public String getDescription() {
        return "Modify style at index " + index + ": "
                + oldElement.getClass().getSimpleName()
                + " -> " + newElement.getClass().getSimpleName();
    }
}
