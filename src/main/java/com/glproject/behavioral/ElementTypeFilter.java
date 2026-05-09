package com.glproject.behavioral;

import com.glproject.domain.Element;
import com.glproject.domain.ImageElement;
import com.glproject.domain.TextElement;
import com.glproject.structural.ElementDecorator;

@FunctionalInterface
public interface ElementTypeFilter {

    boolean matches(Element element);

    static ElementTypeFilter textOnly() {
        return element -> element instanceof TextElement;
    }

    static ElementTypeFilter imageOnly() {
        return element -> element instanceof ImageElement;
    }

    static ElementTypeFilter decoratedOnly() {
        return element -> element instanceof ElementDecorator;
    }

    static ElementTypeFilter all() {
        return element -> true;
    }
}
