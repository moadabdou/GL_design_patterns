package com.glproject.creational;

import com.glproject.domain.Element;
import com.glproject.domain.TextElement;
import java.util.Map;

public class TextElementFactory extends ElementFactory {

    @Override
    public Element createElement(Map<String, String> properties) {
        String content = properties.get("content");
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("'content' property is required for text element");
        }
        return new TextElement(content);
    }

    @Override
    public Class<? extends Element> getElementType() {
        return TextElement.class;
    }
}
