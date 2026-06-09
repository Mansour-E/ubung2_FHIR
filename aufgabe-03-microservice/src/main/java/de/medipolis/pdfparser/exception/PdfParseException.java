package de.medipolis.pdfparser.exception;

/**
 * Wird geworfen wenn das PDF nicht lesbar oder unvollstaendig ist.
 */
public class PdfParseException extends RuntimeException {

    private final String correlationId;

    public PdfParseException(String message, String correlationId) {
        super(message);
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
