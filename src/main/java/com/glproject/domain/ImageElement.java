package com.glproject.domain;

import java.util.Objects;

public class ImageElement implements Element {

    private final String imagePath;
    private final int width;
    private final int height;

    public ImageElement(String imagePath, int width, int height) {
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String render() {
        // intermediate representation of the image element
        return String.format("[Image: %s (%dx%d)]", imagePath, width, height);
    }

    @Override
    public ImageElement clone() {
        return new ImageElement(imagePath, width, height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageElement that)) return false;
        return width == that.width && height == that.height && Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imagePath, width, height);
    }
}
