package com.glproject.domain;

import java.util.Objects;

public class CodeElement implements Element {

    private final String language;
    private final String code;

    public CodeElement(String language, String code) {
        this.language = language;
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String render() {
        return String.format("```%s\n%s\n```", language, code);
    }

    @Override
    public CodeElement clone() {
        return new CodeElement(language, code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeElement that)) return false;
        return Objects.equals(language, that.language) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, code);
    }
}
