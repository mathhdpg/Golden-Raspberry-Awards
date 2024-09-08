package com.mg.gra.application.mapper;

import org.springframework.stereotype.Component;

import com.mg.gra.application.dto.AwardIntervalResultDTO;
import com.mg.gra.domain.models.AwardIntervalResult;

@Component
public class AwardIntervalResultMapper {

    private final AwardIntervalMapper awardIntervalMapper;

    public AwardIntervalResultMapper(AwardIntervalMapper awardIntervalMapper) {
        this.awardIntervalMapper = awardIntervalMapper;
    }

    public AwardIntervalResultDTO toDTO(AwardIntervalResult result) {
        return new AwardIntervalResultDTO(
                awardIntervalMapper.toDTO(result.min()),
                awardIntervalMapper.toDTO(result.max()));
    }

    public AwardIntervalResult toDomain(AwardIntervalResultDTO dto) {
        return new AwardIntervalResult(
                awardIntervalMapper.toDomain(dto.min()),
                awardIntervalMapper.toDomain(dto.max()));
    }

}