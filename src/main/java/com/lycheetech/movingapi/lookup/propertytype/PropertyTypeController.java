package com.lycheetech.movingapi.lookup.propertytype;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.lookup.propertytype.dto.PropertyTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property-types")
@RequiredArgsConstructor
public class PropertyTypeController {

    private final PropertyTypeService propertyTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyTypeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(propertyTypeService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyTypeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(propertyTypeService.findById(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<PropertyTypeResponse>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(propertyTypeService.findBySlug(slug)));
    }
}

