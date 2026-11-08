package com.lycheetech.movingapi.lookup.propertytype.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyTypeResponse {

    private Long id;
    private String slug;
    private String displayName;
    private String category;
    private String sectionName;
    private Integer roomCount;
    private boolean isBasement;
    private Integer estimatedCubicFeet;
    private Integer displayOrder;
    private boolean isActive;
}

