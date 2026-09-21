package com.lycheetech.movingapi.rfq.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A single ad-hoc item requiring special handling (e.g. piano, pool table,
 * gun safe), captured as free-form JSON on the rfqs.specialty_items column
 * since there is no inventory catalog table backing this. Reused as-is for
 * the request payload, the persisted entity attribute, and the response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyItem {

    @NotBlank
    private String itemName;

    @Min(1)
    @Builder.Default
    private Integer quantity = 1;

    private String notes;
}

