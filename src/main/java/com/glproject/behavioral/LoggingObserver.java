package com.glproject.behavioral;

import com.glproject.util.Logger;

public class LoggingObserver implements DocumentObserver {

    private static final Logger logger = Logger.getInstance();

    @Override
    public void onEvent(DocumentEvent event, Object data) {
        logger.info("DocumentEvent: {} data: {}", event, data);
    }
}
