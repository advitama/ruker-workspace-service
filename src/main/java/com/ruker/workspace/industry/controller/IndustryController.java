package com.ruker.workspace.industry.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruker.workspace.app.dto.response.ApiResponse;
import com.ruker.workspace.industry.dto.response.IndustryResponse;
import com.ruker.workspace.industry.entity.Industry;
import com.ruker.workspace.industry.service.IndustryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/industry")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService industryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<IndustryResponse>>> getIndustries() {
        try {
            List<IndustryResponse> response = industryService.findAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>(response, "Industries retrieved successfully"));
        } catch (Exception e) {
            return handleException(e, "Error getting industries");
        }
    }

    private IndustryResponse convertToResponse(Industry industry) {
        return new IndustryResponse(industry.getId(), industry.getName());
    }

    private ResponseEntity<ApiResponse<List<IndustryResponse>>> handleException(Exception e, String errorMessage) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(errorMessage, e.getMessage()));
    }

}
