package com.glproject.creational;

import com.glproject.domain.Element;
import com.glproject.domain.ImageElement;
import java.util.Map;

public class ImageElementFactory extends ElementFactory {

    @Override
    public Element createElement(Map<String, String> properties) {
        String path = properties.get("path");
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("'path' property is required for image element");
        }
        int width = parseInt(properties, "width");
        int height = parseInt(properties, "height");
        return new ImageElement(path, width, height);
    }

    @Override
    public Class<? extends Element> getElementType() {
        return ImageElement.class;
    }

    private static int parseInt(Map<String, String> properties, String key) {
        String value = properties.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("'" + key + "' property is required for image element");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + key + "' must be a valid integer, got: " + value);
        }
    }
}
