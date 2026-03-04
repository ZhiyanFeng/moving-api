-- =============================================================
-- Schema: Moving API
-- =============================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- =============================================================
-- Lookup / Status Tables
-- =============================================================

CREATE TABLE vehicle_statuses (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE rfq_statuses (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE bid_statuses (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE booking_statuses (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE payment_statuses (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE invoice_statuses (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE regions (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE business_services (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- =============================================================
-- Seed: Lookup Data
-- =============================================================

INSERT INTO vehicle_statuses (name) VALUES
    ('ACTIVE'),
    ('MAINTENANCE'),
    ('INACTIVE');

INSERT INTO rfq_statuses (name) VALUES
    ('OPEN'),
    ('ASSIGNED'),
    ('COMPLETED'),
    ('CANCELLED');

INSERT INTO bid_statuses (name) VALUES
    ('PENDING'),
    ('ACCEPTED'),
    ('REJECTED');

INSERT INTO booking_statuses (name) VALUES
    ('SCHEDULED'),
    ('IN_PROGRESS'),
    ('COMPLETED'),
    ('CANCELLED');

INSERT INTO payment_statuses (name) VALUES
    ('PENDING'),
    ('PAID'),
    ('FAILED'),
    ('REFUNDED');

INSERT INTO invoice_statuses (name) VALUES
    ('ISSUED'),
    ('PAID'),
    ('OVERDUE'),
    ('CANCELLED');

INSERT INTO regions (name, description) VALUES
    ('GREATER_TORONTO_AREA', 'Greater Toronto Area'),
    ('DOWNTOWN_TORONTO',     'Downtown Toronto core'),
    ('NORTH_YORK',           'North York region'),
    ('SCARBOROUGH',          'Scarborough region'),
    ('ETOBICOKE',            'Etobicoke region'),
    ('MISSISSAUGA',          'Mississauga area'),
    ('BRAMPTON',             'Brampton area'),
    ('MARKHAM',              'Markham area'),
    ('VAUGHAN',              'Vaughan area');

INSERT INTO business_services (name, description) VALUES
    ('RESIDENTIAL_MOVING', 'Full-service residential moving'),
    ('SMALL_MOVING',       'Small items and furniture moving'),
    ('TRASH_GARBAGE',      'Trash and garbage removal'),
    ('PICKUP_DROPOFF',     'Quick pick-up and drop-off service');


-- =============================================================
-- Core Tables
-- =============================================================

CREATE TABLE users (
    id           UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    address      TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
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
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE mover_regions (
    mover_id  UUID NOT NULL REFERENCES movers(id)   ON DELETE CASCADE,
    region_id UUID NOT NULL REFERENCES regions(id)  ON DELETE CASCADE,
    PRIMARY KEY (mover_id, region_id)
);

CREATE TABLE mover_services (
    mover_id   UUID NOT NULL REFERENCES movers(id)   ON DELETE CASCADE,
    service_id UUID NOT NULL REFERENCES business_services(id) ON DELETE CASCADE,
    PRIMARY KEY (mover_id, service_id)
);

CREATE TABLE vehicles (
    id            UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    mover_id      UUID          NOT NULL REFERENCES movers(id)           ON DELETE CASCADE,
    status_id     UUID          NOT NULL REFERENCES vehicle_statuses(id),
    make          VARCHAR(100)  NOT NULL,
    model         VARCHAR(100)  NOT NULL,
    year          INTEGER       NOT NULL,
    license_plate VARCHAR(50)   NOT NULL,
    capacity      DECIMAL(10,2) NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfqs (
    id                   UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id              UUID  NOT NULL REFERENCES users(id)       ON DELETE CASCADE,
    service_id           UUID           REFERENCES business_services(id),
    status_id            UUID  NOT NULL REFERENCES rfq_statuses(id),
    origin_address       TEXT  NOT NULL,
    destination_address  TEXT  NOT NULL,
    move_date            DATE  NOT NULL,
    description          TEXT,
    item_details         TEXT,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bids (
    id         UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    mover_id   UUID          NOT NULL REFERENCES movers(id) ON DELETE CASCADE,
    rfq_id     UUID          NOT NULL REFERENCES rfqs(id)   ON DELETE CASCADE,
    status_id  UUID          NOT NULL REFERENCES bid_statuses(id),
    amount     DECIMAL(10,2) NOT NULL,
    details    TEXT,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bookings (
    id              UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    rfq_id          UUID  NOT NULL REFERENCES rfqs(id)          ON DELETE CASCADE,
    mover_id        UUID  NOT NULL REFERENCES movers(id)         ON DELETE CASCADE,
    bid_id          UUID  NOT NULL REFERENCES bids(id)           ON DELETE CASCADE,
    vehicle_id      UUID  NOT NULL REFERENCES vehicles(id)       ON DELETE CASCADE,
    status_id       UUID  NOT NULL REFERENCES booking_statuses(id),
    start_date      DATE  NOT NULL,
    completion_date DATE,
    notes           TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventories (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id  UUID         NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    name        VARCHAR(255) NOT NULL,
    quantity    INTEGER      NOT NULL,
    description TEXT
);

CREATE TABLE payments (
    id             UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id     UUID          NOT NULL REFERENCES bookings(id)         ON DELETE CASCADE,
    status_id      UUID          NOT NULL REFERENCES payment_statuses(id),
    amount         DECIMAL(10,2) NOT NULL,
    payment_type   VARCHAR(100)  NOT NULL,
    transaction_id VARCHAR(255),
    payment_date   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE invoices (
    id         UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id UUID          NOT NULL REFERENCES bookings(id)          ON DELETE CASCADE,
    payment_id UUID                   REFERENCES payments(id)          ON DELETE SET NULL,
    status_id  UUID          NOT NULL REFERENCES invoice_statuses(id),
    amount_due DECIMAL(10,2) NOT NULL,
    issue_date DATE          NOT NULL,
    due_date   DATE          NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- =============================================================
-- Indexes
-- =============================================================

CREATE INDEX idx_mover_regions_mover    ON mover_regions(mover_id);
CREATE INDEX idx_mover_regions_region   ON mover_regions(region_id);
CREATE INDEX idx_mover_services_mover   ON mover_services(mover_id);
CREATE INDEX idx_mover_services_service ON mover_services(service_id);

CREATE INDEX idx_vehicles_mover  ON vehicles(mover_id);
CREATE INDEX idx_vehicles_status ON vehicles(status_id);

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

