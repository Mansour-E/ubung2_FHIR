package de.medipolis.abstimmung.exception;

public class BestellungNichtGefundenException extends RuntimeException {
    private final String bestellId;

    public BestellungNichtGefundenException(String bestellId) {
        super("Bestellung nicht gefunden: " + bestellId);
        this.bestellId = bestellId;
    }

    public String getBestellId() {
        return bestellId;
    }
}
