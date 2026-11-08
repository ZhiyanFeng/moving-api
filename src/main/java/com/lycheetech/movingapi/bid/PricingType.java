package com.lycheetech.movingapi.bid;

/**
 * Mirrors the Postgres native enum type bid_pricing_type. A mover can quote
 * either a flat fixed price for the whole job, or an hourly rate with an
 * optional minimum number of billable hours.
 */
public enum PricingType {
    FIXED,
    HOURLY
}

