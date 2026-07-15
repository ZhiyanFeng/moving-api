-- =============================================================
-- Seed: Lookup Data
-- =============================================================

INSERT INTO vehicle_statuses (name, description) VALUES
    ('ACTIVE',      'Vehicle is available and in service'),
    ('MAINTENANCE', 'Vehicle is temporarily out of service for maintenance'),
    ('INACTIVE',    'Vehicle is decommissioned or not currently in use');

INSERT INTO rfq_statuses (name, description) VALUES
    ('OPEN',      'The customer posted the request, and movers can see it and place bids'),
    ('CLOSED',  'A bid was accepted! The RFQ is now locked. A new row is instantly born in the moves ' ||
                        'table to handle the scheduling and execution'),
    ('CANCELLED', 'Customer changed their mind before a bid was accepted. The RFQ is now locked and no further bids can be submitted');

INSERT INTO bid_statuses (name, description) VALUES
    ('PENDING',  'Bid has been submitted and is awaiting a decision'),
    ('ACCEPTED', 'Bid has been accepted by the customer'),
    ('REJECTED', 'Bid was declined by the customer');

INSERT INTO booking_statuses (name) VALUES
    ('SCHEDULED'),
    ('IN_PROGRESS'),
    ('COMPLETED'),
    ('CANCELLED');


-- =============================================================
-- Geography: Countries -> Subdivisions -> Metro Areas -> Sub Areas
-- =============================================================
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

INSERT INTO moving_services (name, description) VALUES
    ('RESIDENTIAL_MOVING', 'Full-service residential moving'),
    ('SMALL_MOVING',       'Small items and furniture moving'),
    ('TRASH_GARBAGE',      'Trash and garbage removal'),
    ('PICKUP_DROP-OFF',     'Quick pick-up and drop-off service');

INSERT INTO inventory_items (item_name, category, thumbnail_url, cubic_feet_volume, estimated_weight_lbs, is_active) VALUES
-- Living Room & Entertainment
('Sofa (3-Seater)', 'Living Room', '/images/living-room/sofa-3-seater.webp', 50.00, 150, TRUE),
('Bookcase (Tall)', 'Living Room', '/images/living-room/bookcase-tall.webp', 20.00, 80, TRUE),
('Armchair', 'Living Room', '/images/living-room/armchair.webp', 20.00, 50, TRUE),
('Coffee Table', 'Living Room', '/images/living-room/coffee-table.webp', 15.00, 40, TRUE),
('TV Stand / Media Console', 'Living Room', '/images/living-room/tv-stand.webp', 25.00, 75, TRUE),
('Rug (Large - 8x10)', 'Living Room', '/images/living-room/rug-large.webp', 5.00, 35, TRUE),
('Side Table', 'Living Room', '/images/living-room/side-table.webp', 4.00, 15, TRUE),
('Piano (Upright)', 'Living Room', '/images/living-room/piano-upright.webp', 70.00, 500, TRUE),

-- Bedroom & Closet
('Mattress (Queen)', 'Bedroom', '/images/bedroom/mattress-queen.webp', 40.00, 70, TRUE),
('Nightstand', 'Bedroom', '/images/bedroom/nightstand.webp', 6.00, 25, TRUE),
('Dresser (6-Drawer)', 'Bedroom', '/images/bedroom/dresser-6-drawer.webp', 45.00, 130, TRUE),
('Nightstand (Secondary)', 'Bedroom', '/images/bedroom/nightstand-secondary.webp', 6.00, 25, TRUE),
('Bed Frame (Queen)', 'Bedroom', '/images/bedroom/bedframe-queen.webp', 20.00, 80, TRUE),
('Wardrobe (Free-standing)', 'Bedroom', '/images/bedroom/wardrobe.webp', 55.00, 150, TRUE),
('Mirror (Full-Length)', 'Bedroom', '/images/bedroom/mirror-full.webp', 4.00, 20, TRUE),
('Bed Frame (Twin)', 'Bedroom', '/images/bedroom/bedframe-twin.webp', 10.00, 45, TRUE),
('Bed Frame (Full / Double)', 'Bedroom', '/images/bedroom/bedframe-double.webp', 15.00, 65, TRUE),
('Bed Frame (King)', 'Bedroom', '/images/bedroom/bedframe-king.webp', 30.00, 110, TRUE),
('Mattress (Twin)', 'Bedroom', '/images/bedroom/mattress-twin.webp', 20.00, 45, TRUE),
('Mattress (Full / Double)', 'Bedroom', '/images/bedroom/mattress-double.webp', 30.00, 60, TRUE),
('Mattress (King)', 'Bedroom', '/images/bedroom/mattress-king.webp', 60.00, 100, TRUE),

-- Kitchen & Dining
('Dining Chair', 'Dining Room', '/images/dining/dining-chair.webp', 5.00, 15, TRUE),
('Refrigerator', 'Kitchen', '/images/kitchen/refrigerator.webp', 60.00, 250, TRUE),
('Dining Table', 'Dining Room', '/images/dining/dining-table.webp', 40.00, 120, TRUE),
('China Cabinet', 'Dining Room', '/images/dining/china-cabinet.webp', 50.00, 200, TRUE),
('Microwave Oven', 'Kitchen', '/images/kitchen/microwave.webp', 4.00, 30, TRUE),
('Bar Stool', 'Kitchen', '/images/kitchen/bar-stool.webp', 8.00, 20, TRUE),
('Kitchen Cart / Island', 'Kitchen', '/images/kitchen/kitchen-cart.webp', 20.00, 60, TRUE),

-- Boxes & Supplies
('Box (Medium)', 'Boxes', '/images/boxes/box-medium.webp', 3.00, 40, TRUE),
('Box (Large)', 'Boxes', '/images/boxes/box-large.webp', 4.50, 50, TRUE),
('Box (Small)', 'Boxes', '/images/boxes/box-small.webp', 1.50, 30, TRUE),
('Wardrobe Box', 'Boxes', '/images/boxes/box-wardrobe.webp', 15.00, 40, TRUE),

-- Office & Study
('Desk (Large)', 'Office', '/images/office/desk-large.webp', 30.00, 90, TRUE),
('Office Chair', 'Office', '/images/office/office-chair.webp', 10.00, 35, TRUE),
('File Cabinet (2-Drawer)', 'Office', '/images/office/file-cabinet.webp', 10.00, 50, TRUE),
('Computer Monitor', 'Office', '/images/office/monitor.webp', 3.00, 15, TRUE),
('Printer / Scanner', 'Office', '/images/office/printer.webp', 5.00, 25, TRUE),

-- Electronics
('Flat Screen TV (55")', 'Electronics', '/images/electronics/tv-55.webp', 8.00, 45, TRUE),

-- Garage, Outdoor & Fitness
('Bicycle', 'Garage/Sports', '/images/garage/bicycle.webp', 15.00, 30, TRUE),
('Patio Table', 'Outdoor', '/images/outdoor/patio-table.webp', 30.00, 50, TRUE),
('Patio Chair', 'Outdoor', '/images/outdoor/patio-chair.webp', 10.00, 15, TRUE),
('Lawn Mower (Push)', 'Garage', '/images/garage/lawn-mower.webp', 20.00, 70, TRUE),
('Treadmill', 'Fitness', '/images/fitness/treadmill.webp', 60.00, 200, TRUE),
('Dumbbell Set', 'Fitness', '/images/fitness/dumbbell-set.webp', 5.00, 100, TRUE),
('Tool Chest', 'Garage', '/images/garage/tool-chest.webp', 15.00, 80, TRUE),

-- Laundry & Household
('Washing Machine', 'Laundry', '/images/laundry/washing-machine.webp', 35.00, 180, TRUE),
('Clothes Dryer', 'Laundry', '/images/laundry/clothes-dryer.webp', 35.00, 130, TRUE),
('Vacuum Cleaner', 'Household', '/images/household/vacuum.webp', 7.00, 18, TRUE);
