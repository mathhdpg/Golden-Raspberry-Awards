package com.mg.gra.adapters.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mg.gra.adapters.dto.AwardIntervalDTO;
import com.mg.gra.domain.entity.AwardInterval;

@Component
public class AwardIntervalMapper {

    public AwardInterval toDomain(AwardIntervalDTO dto) {
        return new AwardInterval(
                dto.producer(),
                dto.interval(),
                dto.previousWin(),
                dto.followingWin());
    }

    public AwardIntervalDTO toDTO(AwardInterval domain) {
        return new AwardIntervalDTO(
                domain.producer(),
                domain.interval(),
                domain.previousWin(),
                domain.followingWin());
    }

    public List<AwardInterval> toDomain(List<AwardIntervalDTO> dtos) {
        return dtos.stream()
                .map(this::toDomain)
                .toList();
    }

    public List<AwardIntervalDTO> toDTO(List<AwardInterval> domains) {
        return domains.stream()
                .map(this::toDTO)
                .toList();
    }

}