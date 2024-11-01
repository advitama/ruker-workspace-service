package com.ruker.workspace.industry.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representing an Industry Response.
 * Contains basic information such as industry ID and name.
 */
@Data
@AllArgsConstructor
public class IndustryResponse {

    private final Integer id;
    private final String name;
}