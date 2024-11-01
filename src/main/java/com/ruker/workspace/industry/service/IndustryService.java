package com.ruker.workspace.industry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ruker.workspace.app.exception.DataRetrievalException;
import com.ruker.workspace.industry.entity.Industry;
import com.ruker.workspace.industry.repository.IndustryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;

    /**
     * Retrieves all industries from the repository.
     *
     * @return a list of Industry objects.
     * @throws DataRetrievalException if data retrieval fails.
     */
    public List<Industry> findAll() {
        try {
            return industryRepository.findAll();
        } catch (Exception e) {
            log.error("Failed to retrieve industries from repository", e);
            throw new DataRetrievalException("Unable to retrieve industries. Please try again later.", e);
        }
    }
}
