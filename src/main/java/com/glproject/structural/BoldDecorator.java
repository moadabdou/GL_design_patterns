package com.glproject.structural;

import com.glproject.domain.Element;

public class BoldDecorator extends ElementDecorator {

    public BoldDecorator(Element wrapped) {
        super(wrapped);
    }

    @Override
    public String render() {
        return "<b>" + wrapped.render() + "</b>";
    }

    @Override
    public Element clone() {
        return new BoldDecorator(wrapped.clone());
    }
}
