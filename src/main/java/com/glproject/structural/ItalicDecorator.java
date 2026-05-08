package com.glproject.structural;

import com.glproject.domain.Element;

public class ItalicDecorator extends ElementDecorator {

    public ItalicDecorator(Element wrapped) {
        super(wrapped);
    }

    @Override
    public String render() {
        return "<i>" + wrapped.render() + "</i>";
    }

    @Override
    public Element clone() {
        return new ItalicDecorator(wrapped.clone());
    }
}
