-- =============================================================
-- Schema: Moving API
-- =============================================================
-- Enable UUID extension (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA public;
-- =============================================================
-- Lookup / Status Tables
-- =============================================================
-- request for quotation table
CREATE TABLE rfq_statuses (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT
);
CREATE TABLE bid_statuses (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT
);
CREATE TABLE booking_statuses (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE countries (
    id         BIGSERIAL    PRIMARY KEY,
    code       VARCHAR(2)   NOT NULL UNIQUE, -- ISO 3166-1 alpha-2, e.g. 'US', 'CA'
    name       VARCHAR(100) NOT NULL UNIQUE,
    -- Deprecation flag: retire a region from active use without breaking
    -- historical FK references (e.g. addresses/mover_service_areas).
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE subdivisions (
    id          BIGSERIAL    PRIMARY KEY,
    country_id  BIGINT       NOT NULL REFERENCES countries(id) ON DELETE CASCADE,
    code        VARCHAR(10)  NOT NULL, -- e.g. 'ON', 'NY', 'WA'
    name        VARCHAR(100) NOT NULL, -- e.g. 'Ontario', 'New York', 'Washington'
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (country_id, code)
);
CREATE TABLE metro_areas (
    id              BIGSERIAL    PRIMARY KEY,
    subdivision_id  BIGINT       NOT NULL REFERENCES subdivisions(id) ON DELETE CASCADE,
    name            VARCHAR(150) NOT NULL, -- e.g. 'Greater Toronto Area'
    description     TEXT,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (subdivision_id, name)
);
CREATE TABLE sub_areas (
    id             BIGSERIAL    PRIMARY KEY,
    metro_area_id  BIGINT       NOT NULL REFERENCES metro_areas(id) ON DELETE CASCADE,
    name           VARCHAR(150) NOT NULL, -- e.g. 'Downtown Toronto', 'North York'
    description    TEXT,
    is_active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (metro_area_id, name)
);
CREATE TABLE moving_services (
    id          BIGSERIAL    PRIMARY KEY,
    code        VARCHAR(100) NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Represents the fixed categories of homes (e.g. '3-bedroom-house'), used to
-- estimate truck size / cubic feet for a move.
CREATE TABLE property_types (
    id                   BIGSERIAL     PRIMARY KEY,
    slug                 VARCHAR(50)   NOT NULL UNIQUE,    -- e.g. '2-bed-house', '2-bed-basement'
    display_name         VARCHAR(100)  NOT NULL,           -- e.g. '2 Bedroom'
    category             VARCHAR(50)   NOT NULL,           -- e.g. 'House', 'Townhouse', 'Apartment', 'Storage'
    section_name         VARCHAR(50)   NOT NULL,           -- e.g. 'Full House', 'Basement Suite', 'Apartment'
    room_count           INT           NOT NULL DEFAULT 0 CHECK (room_count >= 0),
    is_basement          BOOLEAN       NOT NULL DEFAULT false,
    estimated_cubic_feet INT           NOT NULL CHECK (estimated_cubic_feet >= 0),
    display_order        INT           NOT NULL DEFAULT 0,
    is_active            BOOLEAN       NOT NULL DEFAULT true,
    created_at           TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- =============================================================
-- Core Tables
-- =============================================================
CREATE TABLE users (
    -- 1. Identity & Auth Mapping
    id                UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    firebase_uid      VARCHAR(128) NOT NULL,
    email             VARCHAR(255) NOT NULL,

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
    updated_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 5. Soft Delete: account deletion must preserve historical moves/payments
    -- referencing this user, so we mark rather than hard-delete. Uniqueness on
    -- email/firebase_uid is enforced only among non-deleted rows (see partial
    -- unique indexes below), allowing the same email to be reused later.
    deleted_at        TIMESTAMPTZ
);

-- Index for searching users
CREATE INDEX idx_users_firebase_uid ON users(firebase_uid);

CREATE TABLE addresses (
    id                     UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id                UUID          REFERENCES users(id) ON DELETE CASCADE,

    -- Google Maps Identifiers
    google_place_id        VARCHAR(255)  NOT NULL, -- Essential for Google Terms compliance
    formatted_address      TEXT          NOT NULL, -- Full string from Google

    -- Coordinates for instant mapping
    latitude               DECIMAL(10,8) NOT NULL,
    longitude              DECIMAL(11,8) NOT NULL,

    -- Parsing for specific business logic (taxing, dispatching)
    postal_code            VARCHAR(20),
    city                   VARCHAR(100),
    state_province         VARCHAR(100),
    country                VARCHAR(100),

    -- Custom Logistics (Google doesn't provide this)
    label                  VARCHAR(50), -- e.g., 'Home', 'Office', 'Storage'
    unit_apartment_number  VARCHAR(20),
    has_elevator           BOOLEAN       NOT NULL DEFAULT FALSE,
    parking_restrictions   TEXT,

    -- Soft delete: keep the row intact so historical moves that reference
    -- this address via pickup_address_id/dropoff_address_id remain valid.
    deleted_at             TIMESTAMPTZ,

    created_at             TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movers (
    id           UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    address      TEXT,
    description  TEXT,
    avatar_url   TEXT,
    vehicle_url  TEXT,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255),
    -- Soft delete: a mover with bid/move history must not be hard-deleted
    -- (would cascade into bids/moves/payments). Email uniqueness is enforced
    -- only among non-deleted rows (see partial unique index below).
    deleted_at   TIMESTAMPTZ
);
CREATE TABLE mover_service_areas (
    mover_id    UUID   NOT NULL REFERENCES movers(id)     ON DELETE CASCADE,
    sub_area_id BIGINT NOT NULL REFERENCES sub_areas(id)  ON DELETE CASCADE,
    PRIMARY KEY (mover_id, sub_area_id)
);
CREATE TABLE mover_services (
    mover_id   UUID   NOT NULL REFERENCES movers(id)           ON DELETE CASCADE,
    service_id BIGINT NOT NULL REFERENCES moving_services(id)  ON DELETE CASCADE,
    PRIMARY KEY (mover_id, service_id)
);

-- request for quotation (RFQ) table, representing a customer's request for moving services
CREATE TABLE rfqs (
    id                  UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id             UUID  NOT NULL REFERENCES users(id)             ON DELETE CASCADE,
    service_id          BIGINT         REFERENCES moving_services(id),
    status_id           BIGINT NOT NULL REFERENCES rfq_statuses(id),
    origin_address      JSONB NOT NULL,
    destination_address JSONB NOT NULL,
    move_date           DATE  NOT NULL,
    notes               TEXT,

    -- Ad-hoc items needing special handling (e.g. piano, pool table), captured
    -- as free-form JSON since there is no inventory catalog table backing this.
    specialty_items     JSONB NOT NULL DEFAULT '[]'::jsonb,

    -- Driving distance between origin/destination, fetched once from the
    -- Google Distance Matrix API at create/update time so movers can see
    -- trip length upfront when bidding. Nullable: left blank if either
    -- address is missing coordinates or the external API call fails.
    estimated_distance_km NUMERIC(10,2),

    -- Soft delete: lets a customer remove a stale/unwanted RFQ without
    -- destroying bid history tied to it (see BidStatus.WITHDRAWN for the
    -- mover-side equivalent of withdrawing a bid).
    deleted_at          TIMESTAMPTZ,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255)
);
CREATE TYPE bid_pricing_type AS ENUM ('FIXED', 'HOURLY');

CREATE TABLE bids (
    id             UUID             PRIMARY KEY DEFAULT gen_random_uuid(), -- PostgreSQL native UUID generator
    mover_id       UUID             NOT NULL REFERENCES movers(id) ON DELETE CASCADE,
    rfq_id         UUID             NOT NULL REFERENCES rfqs(id) ON DELETE CASCADE,
    status_id      BIGINT           NOT NULL REFERENCES bid_statuses(id),

    pricing_type   bid_pricing_type NOT NULL,
    price          DECIMAL(10,2)    NOT NULL CHECK (price > 0),
    min_hours      INT              DEFAULT 0 CHECK (min_hours >= 0),

    details        TEXT,
    created_at     TIMESTAMPTZ      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(255),
    updated_by     VARCHAR(255),

    UNIQUE (mover_id, rfq_id),

    -- This constraint works natively out-of-the-box in PostgreSQL
    CONSTRAINT check_hourly_minimum CHECK (
        (pricing_type = 'FIXED' AND min_hours = 0) OR
        (pricing_type = 'HOURLY')
    )
);
CREATE TABLE moves (
    id              UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    -- RESTRICT (not CASCADE): moves are financial/audit records and must
    -- never disappear as a side effect of deleting their originating RFQ or
    -- bid. Combined with soft-delete on rfqs, the underlying row survives.
    rfq_id          UUID  NOT NULL REFERENCES rfqs(id)                  ON DELETE RESTRICT,
    mover_id        UUID  NOT NULL REFERENCES movers(id)                ON DELETE CASCADE,
    customer_id     UUID  NOT NULL REFERENCES users(id)                 ON DELETE RESTRICT,
    bid_id          UUID  NOT NULL REFERENCES bids(id)                  ON DELETE RESTRICT,
    status_id       BIGINT NOT NULL REFERENCES booking_statuses(id),

    -- Operational Links (Points to the addresses table)
    pickup_address_id  UUID REFERENCES addresses(id),
    dropoff_address_id UUID REFERENCES addresses(id),

    -- Historical Snapshots (Locked in at time of billing)
    final_pickup_address_text  TEXT          NOT NULL,
    final_dropoff_address_text TEXT          NOT NULL,
    billed_distance_meters     INT,
    final_price                DECIMAL(10,2),

    scheduled_date  TIMESTAMPTZ NOT NULL,
    notes           TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255)
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
-- Partial unique indexes: enforce uniqueness only among non-deleted rows,
-- so a soft-deleted user's/mover's email or firebase_uid can be reused.
CREATE UNIQUE INDEX idx_users_email_active        ON users(email)        WHERE deleted_at IS NULL;
CREATE UNIQUE INDEX idx_users_firebase_uid_active ON users(firebase_uid) WHERE deleted_at IS NULL;
CREATE UNIQUE INDEX idx_movers_email_active       ON movers(email)      WHERE deleted_at IS NULL;
-- Speed up filtering out soft-deleted rows on frequently-queried tables
CREATE INDEX idx_movers_deleted_at ON movers(deleted_at);
CREATE INDEX idx_rfqs_deleted_at   ON rfqs(deleted_at);

CREATE INDEX idx_mover_service_areas_mover_id    ON mover_service_areas(mover_id);
CREATE INDEX idx_mover_service_areas_sub_area_id ON mover_service_areas(sub_area_id);
CREATE INDEX idx_mover_services_mover_id   ON mover_services(mover_id);
CREATE INDEX idx_mover_services_service_id ON mover_services(service_id);
CREATE INDEX idx_subdivisions_country_id    ON subdivisions(country_id);
CREATE INDEX idx_metro_areas_subdivision_id ON metro_areas(subdivision_id);
CREATE INDEX idx_sub_areas_metro_area_id    ON sub_areas(metro_area_id);
-- Crucial index for fast location lookups
CREATE INDEX idx_addresses_place_id ON addresses(google_place_id);
CREATE INDEX idx_addresses_user_id  ON addresses(user_id);
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
CREATE INDEX idx_moves_status_id ON moves(status_id);
CREATE INDEX idx_moves_pickup_address_id  ON moves(pickup_address_id);
CREATE INDEX idx_moves_dropoff_address_id ON moves(dropoff_address_id);
-- Look up payment info for a specific move instantly
CREATE INDEX idx_payments_move_id ON payments(move_id);
-- Crucial index for when Stripe sends a webhook update using their ID
CREATE INDEX idx_payments_stripe_pi ON payments(stripe_payment_intent_id);
-- Speeds up dashboard/reporting queries filtering by payment status
CREATE INDEX idx_payments_status ON payments(stripe_status);
