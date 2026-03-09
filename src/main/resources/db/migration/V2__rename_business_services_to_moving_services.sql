-- =============================================================
-- V2: Rename business_services -> moving_services
-- =============================================================

-- Drop old index on the service column before renaming
DROP INDEX idx_mover_services_service;

-- Rename the table
ALTER TABLE business_services RENAME TO moving_services;

-- Recreate the service index under its new name
CREATE INDEX idx_moving_services_service ON mover_services(service_id);
