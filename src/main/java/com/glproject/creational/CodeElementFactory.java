package com.glproject.creational;

import com.glproject.domain.CodeElement;
import com.glproject.domain.Element;
import java.util.Map;

public class CodeElementFactory extends ElementFactory {

    @Override
    public Element createElement(Map<String, String> properties) {
        String language = properties.get("language");
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("'language' property is required for code element");
        }
        String code = properties.get("code");
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("'code' property is required for code element");
        }
        return new CodeElement(language, code);
    }

    @Override
    public Class<? extends Element> getElementType() {
        return CodeElement.class;
    }
}
