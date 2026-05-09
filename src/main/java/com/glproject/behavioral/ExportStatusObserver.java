package com.glproject.behavioral;

public class ExportStatusObserver implements DocumentObserver {

    private boolean exportInProgress;
    private String lastExportPath;
    private boolean lastExportSucceeded;

    @Override
    public void onEvent(DocumentEvent event, Object data) {
        switch (event) {
            case EXPORT_STARTED -> {
                exportInProgress = true;
                lastExportPath = (String) data;
                lastExportSucceeded = false;
            }
            case EXPORT_COMPLETED -> {
                exportInProgress = false;
                lastExportSucceeded = true;
            }
            case EXPORT_FAILED -> {
                exportInProgress = false;
                lastExportSucceeded = false;
            }
            default -> {}
        }
    }

    public boolean isExportInProgress() {
        return exportInProgress;
    }

    public String getLastExportPath() {
        return lastExportPath;
    }

    public boolean isLastExportSucceeded() {
        return lastExportSucceeded;
    }
}
