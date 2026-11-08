package com.lycheetech.movingapi.common.geo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Computes driving distance between two coordinates via the Google Distance
 * Matrix API, used to give movers an upfront trip-length estimate on an RFQ
 * before they bid. Calculated once (on RFQ create/update) and cached on the
 * {@code rfqs.estimated_distance_km} column rather than looked up on every read.
 *
 * <p>Fails soft: if the API key isn't configured, coordinates are missing, or
 * the call fails for any reason, {@link #getDistanceKm} returns an empty
 * {@link Optional} rather than throwing, so RFQ creation is never blocked by
 * this external dependency.
 */
@Slf4j
@Component
public class GeoDistanceUtil {

    private static final String DISTANCE_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";

    private final RestClient restClient;
    private final String apiKey;

    public GeoDistanceUtil(@Value("${google.maps.api-key:}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl(DISTANCE_MATRIX_URL)
                .build();
    }

    public Optional<BigDecimal> getDistanceKm(BigDecimal originLat, BigDecimal originLng,
                                               BigDecimal destinationLat, BigDecimal destinationLng) {
        if (apiKey == null || apiKey.isBlank()) {
            log.debug("Google Maps API key not configured; skipping RFQ distance calculation");
            return Optional.empty();
        }
        if (originLat == null || originLng == null || destinationLat == null || destinationLng == null) {
            log.debug("Missing coordinates; skipping RFQ distance calculation");
            return Optional.empty();
        }

        try {
            DistanceMatrixResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("origins", originLat + "," + originLng)
                            .queryParam("destinations", destinationLat + "," + destinationLng)
                            .queryParam("units", "metric")
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .body(DistanceMatrixResponse.class);

            return extractDistanceKm(response);
        } catch (RestClientException ex) {
            log.warn("Google Distance Matrix API call failed, leaving estimatedDistanceKm null: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private Optional<BigDecimal> extractDistanceKm(DistanceMatrixResponse response) {
        if (response == null || !"OK".equals(response.status())
                || response.rows() == null || response.rows().isEmpty()) {
            return Optional.empty();
        }
        List<Element> elements = response.rows().get(0).elements();
        if (elements == null || elements.isEmpty()) {
            return Optional.empty();
        }
        Element element = elements.get(0);
        if (!"OK".equals(element.status()) || element.distance() == null) {
            log.warn("Google Distance Matrix element status not OK: {}", element.status());
            return Optional.empty();
        }

        BigDecimal meters = BigDecimal.valueOf(element.distance().value());
        return Optional.of(meters.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP));
    }

    // Minimal shape of the Google Distance Matrix API JSON response.
    // https://developers.google.com/maps/documentation/distance-matrix/distance-matrix
    private record DistanceMatrixResponse(String status, List<Row> rows) {
    }

    private record Row(List<Element> elements) {
    }

    private record Element(String status, Distance distance) {
    }

    private record Distance(String text, long value) {
    }
}

