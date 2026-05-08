package com.glproject.structural;

import com.glproject.domain.Document;
import java.io.OutputStream;

public interface Exporter {
    void export(Document document, OutputStream output);
}
