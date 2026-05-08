package com.glproject.behavioral;

import com.glproject.domain.Document;

public interface ExportStrategy {
    byte[] render(Document document);
}
