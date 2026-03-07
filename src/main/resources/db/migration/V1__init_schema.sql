-- =============================================================
-- Schema: Moving API
-- =============================================================

-- Enable UUID extension (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- =============================================================
-- Lookup / Status Tables
-- =============================================================

CREATE TABLE vehicle_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE rfq_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE bid_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE booking_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE payment_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE invoice_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE regions (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE business_services (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);


-- =============================================================
-- Seed: Lookup Data
-- =============================================================

INSERT INTO vehicle_statuses  (name, created_at, updated_at, created_by, updated_by) VALUES
    ('ACTIVE',      now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('MAINTENANCE', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('INACTIVE',    now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');

INSERT INTO rfq_statuses (name, created_at, updated_at, created_by, updated_by) VALUES
    ('OPEN',      now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('ASSIGNED',  now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('COMPLETED', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('CANCELLED', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');

INSERT INTO bid_statuses (name, created_at, updated_at, created_by, updated_by) VALUES
    ('PENDING',  now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('ACCEPTED', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('REJECTED', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');

INSERT INTO booking_statuses (name, created_at, updated_at, created_by, updated_by) VALUES
    ('SCHEDULED',   now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('IN_PROGRESS', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('COMPLETED',   now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('CANCELLED',   now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');

INSERT INTO payment_statuses (name, created_at, updated_at, created_by, updated_by) VALUES
    ('PENDING',  now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('PAID',     now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('FAILED',   now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('REFUNDED', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');

INSERT INTO invoice_statuses (name, created_at, updated_at, created_by, updated_by) VALUES
    ('ISSUED',    now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('PAID',      now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('OVERDUE',   now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('CANCELLED', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');

INSERT INTO regions (name, description, created_at, updated_at, created_by, updated_by) VALUES
    ('GREATER_TORONTO_AREA', 'Greater Toronto Area',    now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('DOWNTOWN_TORONTO',     'Downtown Toronto core',   now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('NORTH_YORK',           'North York region',       now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('SCARBOROUGH',          'Scarborough region',      now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('ETOBICOKE',            'Etobicoke region',        now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('MISSISSAUGA',          'Mississauga area',        now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('BRAMPTON',             'Brampton area',           now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('MARKHAM',              'Markham area',            now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('VAUGHAN',              'Vaughan area',            now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');

INSERT INTO business_services (name, description, created_at, updated_at, created_by, updated_by) VALUES
    ('RESIDENTIAL_MOVING', 'Full-service residential moving',    now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('SMALL_MOVING',       'Small items and furniture moving',   now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('TRASH_GARBAGE',      'Trash and garbage removal',          now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED'),
    ('PICKUP_DROPOFF',     'Quick pick-up and drop-off service', now(), now(), 'SYSTEM_SEED', 'SYSTEM_SEED');


-- =============================================================
-- Core Tables
-- =============================================================

CREATE TABLE users (
    id           UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(50)  NOT NULL DEFAULT 'USER',
    phone_number VARCHAR(50),
    address      TEXT,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255)
);

CREATE TABLE movers (
    id           UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    address      TEXT,
    description  TEXT,
    avatar_url   TEXT,
    vehicle_url  TEXT,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255)
);

CREATE TABLE mover_regions (
    mover_id  UUID NOT NULL REFERENCES movers(id)            ON DELETE CASCADE,
    region_id UUID NOT NULL REFERENCES regions(id)           ON DELETE CASCADE,
    PRIMARY KEY (mover_id, region_id)
);

CREATE TABLE mover_services (
    mover_id   UUID NOT NULL REFERENCES movers(id)           ON DELETE CASCADE,
    service_id UUID NOT NULL REFERENCES business_services(id) ON DELETE CASCADE,
    PRIMARY KEY (mover_id, service_id)
);

CREATE TABLE vehicles (
    id            UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    mover_id      UUID          NOT NULL REFERENCES movers(id)          ON DELETE CASCADE,
    status_id     UUID          NOT NULL REFERENCES vehicle_statuses(id),
    make          VARCHAR(100)  NOT NULL,
    model         VARCHAR(100)  NOT NULL,
    year          INTEGER       NOT NULL,
    license_plate VARCHAR(50)   NOT NULL,
    capacity      DECIMAL(10,2) NOT NULL,
    created_at    TIMESTAMP     NOT NULL,
    updated_at    TIMESTAMP     NOT NULL,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255)
);

CREATE TABLE rfqs (
    id                  UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id             UUID  NOT NULL REFERENCES users(id)             ON DELETE CASCADE,
    service_id          UUID           REFERENCES business_services(id),
    status_id           UUID  NOT NULL REFERENCES rfq_statuses(id),
    origin_address      TEXT  NOT NULL,
    destination_address TEXT  NOT NULL,
    move_date           DATE  NOT NULL,
    description         TEXT,
    item_details        TEXT,
    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255)
);

CREATE TABLE bids (
    id         UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    mover_id   UUID          NOT NULL REFERENCES movers(id)             ON DELETE CASCADE,
    rfq_id     UUID          NOT NULL REFERENCES rfqs(id)               ON DELETE CASCADE,
    status_id  UUID          NOT NULL REFERENCES bid_statuses(id),
    amount     DECIMAL(10,2) NOT NULL,
    details    TEXT,
    created_at TIMESTAMP     NOT NULL,
    updated_at TIMESTAMP     NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE bookings (
    id              UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    rfq_id          UUID  NOT NULL REFERENCES rfqs(id)                  ON DELETE CASCADE,
    mover_id        UUID  NOT NULL REFERENCES movers(id)                ON DELETE CASCADE,
    bid_id          UUID  NOT NULL REFERENCES bids(id)                  ON DELETE CASCADE,
    vehicle_id      UUID  NOT NULL REFERENCES vehicles(id)              ON DELETE CASCADE,
    status_id       UUID  NOT NULL REFERENCES booking_statuses(id),
    start_date      DATE  NOT NULL,
    completion_date DATE,
    notes           TEXT,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255)
);

CREATE TABLE inventories (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id  UUID         NOT NULL REFERENCES bookings(id)           ON DELETE CASCADE,
    name        VARCHAR(255) NOT NULL,
    quantity    INTEGER      NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE payments (
    id             UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id     UUID          NOT NULL REFERENCES bookings(id)       ON DELETE CASCADE,
    status_id      UUID          NOT NULL REFERENCES payment_statuses(id),
    amount         DECIMAL(10,2) NOT NULL,
    payment_type   VARCHAR(100)  NOT NULL,
    transaction_id VARCHAR(255),
    created_at     TIMESTAMP     NOT NULL,
    updated_at     TIMESTAMP     NOT NULL,
    created_by     VARCHAR(255),
    updated_by     VARCHAR(255)
);

CREATE TABLE invoices (
    id         UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id UUID          NOT NULL REFERENCES bookings(id)           ON DELETE CASCADE,
    payment_id UUID                   REFERENCES payments(id)           ON DELETE SET NULL,
    status_id  UUID          NOT NULL REFERENCES invoice_statuses(id),
    amount_due DECIMAL(10,2) NOT NULL,
    issue_date DATE          NOT NULL,
    due_date   DATE          NOT NULL,
    created_at TIMESTAMP     NOT NULL,
    updated_at TIMESTAMP     NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);


-- =============================================================
-- Indexes
-- =============================================================

CREATE INDEX idx_mover_regions_mover    ON mover_regions(mover_id);
CREATE INDEX idx_mover_regions_region   ON mover_regions(region_id);
CREATE INDEX idx_mover_services_mover   ON mover_services(mover_id);
CREATE INDEX idx_mover_services_service ON mover_services(service_id);

CREATE INDEX idx_vehicles_mover   ON vehicles(mover_id);
CREATE INDEX idx_vehicles_status  ON vehicles(status_id);

CREATE INDEX idx_rfqs_user    ON rfqs(user_id);
CREATE INDEX idx_rfqs_service ON rfqs(service_id);
CREATE INDEX idx_rfqs_status  ON rfqs(status_id);

CREATE INDEX idx_bids_mover  ON bids(mover_id);
CREATE INDEX idx_bids_rfq    ON bids(rfq_id);
CREATE INDEX idx_bids_status ON bids(status_id);

CREATE INDEX idx_bookings_rfq    ON bookings(rfq_id);
CREATE INDEX idx_bookings_mover  ON bookings(mover_id);
CREATE INDEX idx_bookings_status ON bookings(status_id);

CREATE INDEX idx_inventories_booking ON inventories(booking_id);

CREATE INDEX idx_payments_booking ON payments(booking_id);
CREATE INDEX idx_payments_status  ON payments(status_id);

CREATE INDEX idx_invoices_booking ON invoices(booking_id);
CREATE INDEX idx_invoices_status  ON invoices(status_id);

