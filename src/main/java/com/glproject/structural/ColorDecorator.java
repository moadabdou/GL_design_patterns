package com.glproject.structural;

import com.glproject.domain.Element;

public class ColorDecorator extends ElementDecorator {

    private final String color;

    public ColorDecorator(Element wrapped, String color) {
        super(wrapped);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String render() {
        return "<span style=\"color:" + color + "\">" + wrapped.render() + "</span>";
    }

    @Override
    public Element clone() {
        return new ColorDecorator(wrapped.clone(), color);
    }
}
