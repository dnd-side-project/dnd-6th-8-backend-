package com.travel.domain.day.service;

import com.travel.domain.archive.entity.Archives;
import com.travel.domain.archive.repository.ArchivesRepository;
import com.travel.domain.archive.service.ArchivesService;
import com.travel.domain.day.repository.DaysRepository;
import com.travel.domain.day.dto.DayDetailResponseDto;
import com.travel.domain.day.dto.DaysSaveRequestDto;
import com.travel.domain.day.entity.Days;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DayServiceImpl implements DaysService {

    private final DaysRepository daysRepository;
    private final ArchivesRepository archivesRepository;

    @Override
    @Transactional(readOnly=true)
    public DayDetailResponseDto saveDay(DaysSaveRequestDto daysSaveRequestDto, Long archiveId) {
        Days day = daysSaveRequestDto.toEntity();
        Archives archives = archivesRepository.findById(archiveId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + archiveId));
        day.setArchives(archives);
        daysRepository.save(day);
        return new DayDetailResponseDto(day);
    }

    @Override
    @Transactional(readOnly=true)
    public List<DayDetailResponseDto> getDays(Archives archives, Integer dayNumber) {
        List<Days> filtered = daysRepository.findByArchivesAndDayNumber(archives.getId());
        return DayDetailResponseDto.listOf(filtered);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDay(Long id, DaysSaveRequestDto daysSaveRequestDto) {
        Days day = daysRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 피드가 없습니다. id = " + id));

        if (daysSaveRequestDto.getDayNumber() != 0) {
            day.setDayNumber(daysSaveRequestDto.getDayNumber());
        }
        if (daysSaveRequestDto.getDate() != null) {
            day.setDate(daysSaveRequestDto.getDate());
        }
        if (daysSaveRequestDto.getWeather() != null) {
            day.setWeather(daysSaveRequestDto.getWeather());
        }
        if (daysSaveRequestDto.getImage() != null) {
            day.setImage(daysSaveRequestDto.getImage());
        }
        if (daysSaveRequestDto.getTravelDescription() != null) {
            day.setTravelDescription(daysSaveRequestDto.getTravelDescription());
        }
        if (daysSaveRequestDto.getEmotionDescription() != null) {
            day.setEmotionDescription(daysSaveRequestDto.getEmotionDescription());
        }
        if (daysSaveRequestDto.getTipDescription() != null) {
            day.setTipDescription(daysSaveRequestDto.getTipDescription());
        }

        daysRepository.save(day);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Days day = daysRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("해당 피드가 없습니다. id = " + id));
        daysRepository.delete(day);
    }
}