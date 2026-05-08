package com.glproject.domain;

import java.util.Objects;

public class TextElement implements Element {

    private final String content;

    public TextElement(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String render() {
        return content;
    }

    @Override
    public TextElement clone() {
        return new TextElement(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextElement that)) return false;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
