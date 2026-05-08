package com.glproject.structural;

import com.glproject.domain.Element;

public class UnderlineDecorator extends ElementDecorator {

    public UnderlineDecorator(Element wrapped) {
        super(wrapped);
    }

    @Override
    public String render() {
        return "<u>" + wrapped.render() + "</u>";
    }

    @Override
    public Element clone() {
        return new UnderlineDecorator(wrapped.clone());
    }
}
