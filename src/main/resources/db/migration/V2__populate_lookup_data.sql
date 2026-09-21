-- =============================================================
-- Seed: Lookup Data
-- =============================================================


INSERT INTO rfq_statuses (name, description) VALUES
    ('OPEN',      'The customer posted the request, and movers can see it and place bids'),
    ('CLOSED',  'A bid was accepted! The RFQ is now locked. A new row is instantly born in the moves ' ||
                        'table to handle the scheduling and execution'),
    ('CANCELLED', 'Customer changed their mind before a bid was accepted. The RFQ is now locked and no further bids can be submitted');

INSERT INTO bid_statuses (name, description) VALUES
    ('PENDING',  'Bid has been submitted and is awaiting a decision'),
    ('ACCEPTED', 'Bid has been accepted by the customer'),
    ('REJECTED', 'Bid was declined by the customer');


-- =============================================================
-- Geography: Countries -> Subdivisions -> Metro Areas -> Sub Areas
-- =============================================================
INSERT INTO booking_statuses (name) VALUES
    ('SCHEDULED'),
    ('IN_PROGRESS'),
    ('COMPLETED'),
    ('CANCELLED');

INSERT INTO countries (code, name) VALUES
    ('CA', 'Canada'),
    ('US', 'United States');

INSERT INTO subdivisions (country_id, code, name) VALUES
    ((SELECT id FROM countries WHERE code = 'CA'), 'ON', 'Ontario'),
    ((SELECT id FROM countries WHERE code = 'US'), 'NY', 'New York'),
    ((SELECT id FROM countries WHERE code = 'US'), 'WA', 'Washington');

INSERT INTO metro_areas (subdivision_id, name, description) VALUES
    ((SELECT id FROM subdivisions WHERE code = 'ON'), 'GREATER_TORONTO_AREA', 'Greater Toronto Area'),
    ((SELECT id FROM subdivisions WHERE code = 'ON'), 'OTTAWA_AREA',          'Ottawa metropolitan area'),
    ((SELECT id FROM subdivisions WHERE code = 'NY'), 'NEW_YORK_CITY_METRO',  'New York City metropolitan area'),
    ((SELECT id FROM subdivisions WHERE code = 'WA'), 'GREATER_SEATTLE_AREA', 'Greater Seattle area');

INSERT INTO sub_areas (metro_area_id, name, description) VALUES
    -- Greater Toronto Area
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'DOWNTOWN_TORONTO', 'Downtown Toronto core'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'NORTH_YORK',        'North York region'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'SCARBOROUGH',       'Scarborough region'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'ETOBICOKE',         'Etobicoke region'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'MISSISSAUGA',       'Mississauga area'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'BRAMPTON',          'Brampton area'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'MARKHAM',           'Markham area'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_TORONTO_AREA'), 'VAUGHAN',           'Vaughan area'),

    -- Ottawa Area
    ((SELECT id FROM metro_areas WHERE name = 'OTTAWA_AREA'), 'DOWNTOWN_OTTAWA', 'Downtown Ottawa core'),
    ((SELECT id FROM metro_areas WHERE name = 'OTTAWA_AREA'), 'KANATA',         'Kanata suburb'),
    ((SELECT id FROM metro_areas WHERE name = 'OTTAWA_AREA'), 'ORLEANS',        'Orleans suburb'),

    -- New York City Metro
    ((SELECT id FROM metro_areas WHERE name = 'NEW_YORK_CITY_METRO'), 'MANHATTAN',     'Manhattan borough'),
    ((SELECT id FROM metro_areas WHERE name = 'NEW_YORK_CITY_METRO'), 'BROOKLYN',      'Brooklyn borough'),
    ((SELECT id FROM metro_areas WHERE name = 'NEW_YORK_CITY_METRO'), 'QUEENS',        'Queens borough'),
    ((SELECT id FROM metro_areas WHERE name = 'NEW_YORK_CITY_METRO'), 'THE_BRONX',     'The Bronx borough'),
    ((SELECT id FROM metro_areas WHERE name = 'NEW_YORK_CITY_METRO'), 'STATEN_ISLAND', 'Staten Island borough'),

    -- Greater Seattle Area
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_SEATTLE_AREA'), 'DOWNTOWN_SEATTLE', 'Downtown Seattle core'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_SEATTLE_AREA'), 'BELLEVUE',         'Bellevue suburb'),
    ((SELECT id FROM metro_areas WHERE name = 'GREATER_SEATTLE_AREA'), 'TACOMA',           'Tacoma suburb');


INSERT INTO moving_services (code, name, description, is_active) VALUES
    ('RESIDENTIAL_MOVING', 'Residential Moving',       'Full-service residential moving', TRUE),
    ('SMALL_MOVING',       'Small Moving',             'Small items and furniture moving', TRUE),
    ('TRASH_GARBAGE',      'Trash & Garbage Removal',  'Trash and garbage removal', TRUE),
    ('PICKUP_DROPOFF',     'Pickup & Drop-off',        'Quick pick-up and drop-off service', TRUE),
    ('COMMERCIAL_MOVING',  'Commercial Moving',        'Office and commercial relocation services', TRUE),
    ('STORAGE_SOLUTIONS',  'Storage Solutions',        'Short-term and long-term storage options', TRUE);

-- =============================================================
-- Property Types: used to estimate truck size
-- =============================================================
INSERT INTO property_types (slug, display_name, "group", estimated_cubic_feet) VALUES
    -- Apartment
    ('studio-apartment',     'Studio Apartment',      'Apartment',  450),
    ('1-bedroom-apartment',  '1 Bedroom Apartment',   'Apartment',  650),
    ('2-bedroom-apartment',  '2 Bedroom Apartment',   'Apartment',  900),
    ('3-bedroom-apartment',  '3 Bedroom Apartment',   'Apartment', 1100),

    -- Basement
    ('1-bedroom-basement',   '1 Bedroom Basement',    'Basement',   600),
    ('2-bedroom-basement',   '2 Bedroom Basement',    'Basement',   800),
    ('3-bedroom-basement',   '3 Bedroom Basement',    'Basement',  1000),

    -- House
    ('2-bedroom-house',      '2 Bedroom House',       'House',     1000),
    ('3-bedroom-house',      '3 Bedroom House',       'House',     1400),
    ('4-bedroom-house',      '4 Bedroom House',       'House',     1800),
    ('5-plus-bedroom-house', '5+ Bedroom House',      'House',     2400),

    -- Townhouse
    ('2-bedroom-townhouse',  '2 Bedroom Townhouse',   'Townhouse', 1100),
    ('3-bedroom-townhouse',  '3 Bedroom Townhouse',   'Townhouse', 1300);

