-- Medikamenten-Tabelle fuer Integrationstests
CREATE TABLE IF NOT EXISTS medikamente (
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    dosierung_mg   DECIMAL(10,2) NOT NULL,
    einheit        VARCHAR(50) NOT NULL,
    patient_id     VARCHAR(100) NOT NULL,
    status         VARCHAR(50) NOT NULL DEFAULT 'NEU',
    erstellt_am    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_medikamente_patient_id ON medikamente(patient_id);
CREATE INDEX idx_medikamente_status ON medikamente(status);
