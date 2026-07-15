-- =============================================================
-- Schema: Moving API
-- =============================================================
-- Enable UUID extension (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA public;
-- =============================================================
-- Lookup / Status Tables
-- =============================================================
CREATE TABLE vehicle_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT
);
-- request for quotation table
CREATE TABLE rfq_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT
);
CREATE TABLE bid_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT
);
CREATE TABLE booking_statuses (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE countries (
    id         UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    code       VARCHAR(2)   NOT NULL UNIQUE, -- ISO 3166-1 alpha-2, e.g. 'US', 'CA'
    name       VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE subdivisions (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id  UUID         NOT NULL REFERENCES countries(id) ON DELETE CASCADE,
    code        VARCHAR(10)  NOT NULL, -- e.g. 'ON', 'NY', 'WA'
    name        VARCHAR(100) NOT NULL, -- e.g. 'Ontario', 'New York', 'Washington'
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (country_id, code)
);
CREATE TABLE metro_areas (
    id              UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    subdivision_id  UUID         NOT NULL REFERENCES subdivisions(id) ON DELETE CASCADE,
    name            VARCHAR(150) NOT NULL, -- e.g. 'Greater Toronto Area'
    description     TEXT,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (subdivision_id, name)
);
CREATE TABLE sub_areas (
    id             UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    metro_area_id  UUID         NOT NULL REFERENCES metro_areas(id) ON DELETE CASCADE,
    name           VARCHAR(150) NOT NULL, -- e.g. 'Downtown Toronto', 'North York'
    description    TEXT,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (metro_area_id, name)
);
CREATE TABLE moving_services (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 1. THE MASTER CATALOG (Pre-populated lookup of inventory item types)
CREATE TABLE inventory_items (
    id                   UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    item_name            VARCHAR(100)  NOT NULL UNIQUE, -- e.g., 'King Bed', '3-Seater Sofa', 'Medium Box'
    category             VARCHAR(50)   NOT NULL,        -- e.g., 'Bedroom', 'Living Room', 'Boxes'
    thumbnail_url        TEXT,                          -- URL of the item's thumbnail image

    -- LOGISTICS METRICS
    cubic_feet_volume    DECIMAL(5,2)  NOT NULL CHECK (cubic_feet_volume >= 0),  -- Used to calculate truck space needed
    estimated_weight_lbs INTEGER       NOT NULL DEFAULT 0 CHECK (estimated_weight_lbs >= 0), -- Used to ensure truck isn't overloaded

    is_active            BOOLEAN       NOT NULL DEFAULT TRUE
);
-- =============================================================
-- Core Tables
-- =============================================================
CREATE TABLE users (
    -- 1. Identity & Auth Mapping
    id                UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    firebase_uid      VARCHAR(128) NOT NULL UNIQUE,
    email             VARCHAR(255) NOT NULL UNIQUE,

    -- 2. Profile Info
    first_name        VARCHAR(100) NOT NULL,
    last_name         VARCHAR(100) NOT NULL,
    phone_number      VARCHAR(50)  NOT NULL,
    avatar_url        TEXT,

    -- 3. Live Marketplace Aggregates
    -- This stores the rolling average calculated from the review table
    rating_avg        NUMERIC(3,2) NOT NULL DEFAULT 5.00 CHECK (rating_avg BETWEEN 0 AND 5),
    completed_moves   INT          NOT NULL DEFAULT 0 CHECK (completed_moves >= 0),
    canceled_moves    INT          NOT NULL DEFAULT 0 CHECK (canceled_moves >= 0),

    -- 4. Audit Metadata
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index for searching users
CREATE INDEX idx_users_firebase_uid ON users(firebase_uid);
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
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255)
);
CREATE TABLE mover_service_areas (
    mover_id    UUID NOT NULL REFERENCES movers(id)     ON DELETE CASCADE,
    sub_area_id UUID NOT NULL REFERENCES sub_areas(id)  ON DELETE CASCADE,
    PRIMARY KEY (mover_id, sub_area_id)
);
CREATE TABLE mover_services (
    mover_id   UUID NOT NULL REFERENCES movers(id)           ON DELETE CASCADE,
    service_id UUID NOT NULL REFERENCES moving_services(id)  ON DELETE CASCADE,
    PRIMARY KEY (mover_id, service_id)
);
CREATE TABLE vehicles (
    id            UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    mover_id      UUID          NOT NULL REFERENCES movers(id)          ON DELETE CASCADE,
    status_id     UUID          NOT NULL REFERENCES vehicle_statuses(id),
    make          VARCHAR(100)  NOT NULL,
    model         VARCHAR(100)  NOT NULL,
    year          INTEGER       NOT NULL CHECK (year BETWEEN 1900 AND 2100),
    license_plate VARCHAR(50)   NOT NULL,
    capacity      DECIMAL(10,2) NOT NULL CHECK (capacity > 0),
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255)
);
CREATE TABLE rfqs (
    id                  UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id             UUID  NOT NULL REFERENCES users(id)             ON DELETE CASCADE,
    service_id          UUID           REFERENCES moving_services(id),
    status_id           UUID  NOT NULL REFERENCES rfq_statuses(id),
    origin_address      TEXT  NOT NULL,
    destination_address TEXT  NOT NULL,
    move_date           DATE  NOT NULL,
    description         TEXT,
    item_details        TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255)
);
CREATE TABLE bids (
    id         UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    mover_id   UUID          NOT NULL REFERENCES movers(id)             ON DELETE CASCADE,
    rfq_id     UUID          NOT NULL REFERENCES rfqs(id)               ON DELETE CASCADE,
    status_id  UUID          NOT NULL REFERENCES bid_statuses(id),
    amount     DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    details    TEXT,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE (mover_id, rfq_id)
);
CREATE TABLE moves (
    id              UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    rfq_id          UUID  NOT NULL REFERENCES rfqs(id)                  ON DELETE CASCADE,
    mover_id        UUID  NOT NULL REFERENCES movers(id)                ON DELETE CASCADE,
    customer_id     UUID  NOT NULL REFERENCES users(id)                 ON DELETE RESTRICT,
    bid_id          UUID  NOT NULL REFERENCES bids(id)                  ON DELETE CASCADE,
    vehicle_id      UUID  NOT NULL REFERENCES vehicles(id)              ON DELETE CASCADE,
    status_id       UUID  NOT NULL REFERENCES booking_statuses(id),
    start_date      DATE  NOT NULL,
    completion_date DATE,
    notes           TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    CHECK (completion_date IS NULL OR completion_date >= start_date)
);
CREATE TABLE move_inventories (
    id             UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- Link to the main move
    move_id        UUID          NOT NULL REFERENCES moves(id)           ON DELETE CASCADE,

    -- Link to the master item library
    item_id        UUID          NOT NULL REFERENCES inventory_items(id) ON DELETE RESTRICT,

    -- QUANTITY & DETAILS
    quantity       INTEGER       NOT NULL DEFAULT 1 CHECK (quantity > 0),
    is_fragile     BOOLEAN       NOT NULL DEFAULT FALSE,
    customer_notes TEXT, -- e.g., "Slightly scratched already"

    created_at     TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE payments (
    id                        UUID           PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- LINK TO CORE OPERATIONS
    move_id                   UUID           NOT NULL REFERENCES moves(id) ON DELETE RESTRICT,

    -- STRIPE DIRECT MAPS (Replaces lookup tables)
    -- Stripe Native States: 'requires_payment_method', 'processing', 'succeeded', 'canceled'
    stripe_status             VARCHAR(50)    NOT NULL DEFAULT 'requires_payment_method',

    -- STRIPE TRACKING IDS (Crucial for webhooks, customer support, and financial audits)
    stripe_payment_intent_id VARCHAR(255) UNIQUE, -- e.g., 'pi_3MvL...'
    stripe_invoice_id        VARCHAR(255) UNIQUE, -- e.g., 'in_3MvL...' (Only if using Stripe Invoicing)
    stripe_receipt_url       TEXT,                -- Link to Stripe's automatically generated PDF receipt

    -- FINANCIAL DATA
    amount                    DECIMAL(10,2)  NOT NULL CHECK (amount > 0), -- The dollar amount charged
    currency                  VARCHAR(3)     NOT NULL DEFAULT 'CAD', -- Standard ISO currency code (e.g., CAD, USD)

    -- TIMESTAMPS
    created_at                TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- =============================================================
-- Indexes
-- =============================================================
CREATE INDEX idx_mover_service_areas_mover_id    ON mover_service_areas(mover_id);
CREATE INDEX idx_mover_service_areas_sub_area_id ON mover_service_areas(sub_area_id);
CREATE INDEX idx_mover_services_mover_id   ON mover_services(mover_id);
CREATE INDEX idx_mover_services_service_id ON mover_services(service_id);
CREATE INDEX idx_subdivisions_country_id    ON subdivisions(country_id);
CREATE INDEX idx_metro_areas_subdivision_id ON metro_areas(subdivision_id);
CREATE INDEX idx_sub_areas_metro_area_id    ON sub_areas(metro_area_id);
CREATE INDEX idx_vehicles_mover_id   ON vehicles(mover_id);
CREATE INDEX idx_vehicles_status_id  ON vehicles(status_id);
CREATE INDEX idx_rfqs_user_id    ON rfqs(user_id);
CREATE INDEX idx_rfqs_service_id ON rfqs(service_id);
CREATE INDEX idx_rfqs_status_id  ON rfqs(status_id);
CREATE INDEX idx_bids_mover_id  ON bids(mover_id);
CREATE INDEX idx_bids_rfq_id    ON bids(rfq_id);
CREATE INDEX idx_bids_status_id ON bids(status_id);
CREATE INDEX idx_moves_rfq_id    ON moves(rfq_id);
CREATE INDEX idx_moves_mover_id  ON moves(mover_id);
CREATE INDEX idx_moves_customer_id ON moves(customer_id);
CREATE INDEX idx_moves_bid_id     ON moves(bid_id);
CREATE INDEX idx_moves_vehicle_id ON moves(vehicle_id);
CREATE INDEX idx_moves_status_id ON moves(status_id);
-- SPEED OPTIMIZATION
-- Speeds up calculating the total inventory weight/volume for a specific move
CREATE INDEX idx_move_inventories_move_id ON move_inventories(move_id);
CREATE INDEX idx_move_inventories_item_id ON move_inventories(item_id);
-- Speeds up fetching only the active catalog items shown to customers
CREATE INDEX idx_inventory_items_active ON inventory_items(is_active) WHERE is_active = TRUE;
-- Look up payment info for a specific move instantly
CREATE INDEX idx_payments_move_id ON payments(move_id);
-- Crucial index for when Stripe sends a webhook update using their ID
CREATE INDEX idx_payments_stripe_pi ON payments(stripe_payment_intent_id);
-- Speeds up dashboard/reporting queries filtering by payment status
CREATE INDEX idx_payments_status ON payments(stripe_status);
