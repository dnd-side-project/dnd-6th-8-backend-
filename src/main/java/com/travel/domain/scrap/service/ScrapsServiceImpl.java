package com.travel.domain.scrap.service;

import com.travel.domain.archive.entity.Archives;
import com.travel.domain.archive.repository.ArchivesRepository;
import com.travel.domain.scrap.dto.ScrapPreviewDto;
import com.travel.domain.scrap.dto.ScrapsSaveRequestDto;
import com.travel.domain.scrap.entity.Scraps;
import com.travel.domain.scrap.repository.ScrapsRepository;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScrapsServiceImpl implements ScrapsService {

    private final ScrapsRepository scrapsRepository;
    private final ArchivesRepository archivesRepository;
    private final UserRepository userRepository;


    @Override
    public Scraps addScraps(String userEmail, Long archiveId) {
        User user = userRepository.findByEmail(userEmail);
        Archives archives = archivesRepository.findById(archiveId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + archiveId));
        Scraps scrap = scrapsRepository.save(Scraps.builder().user(user).archives(archives).build());
        return scrap;
    }

    @Override
    @Transactional
    public void unScraps(long scrapId) {
        Scraps scrap = scrapsRepository.findById(scrapId).orElseThrow(
                ()->new IllegalArgumentException("해당 스크랩 내역이 없습니다. id = " + scrapId));
        scrapsRepository.delete(scrap);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScrapPreviewDto> findByUser(String user) {
        List<Scraps> filtered = scrapsRepository.findByUser(user);
        return ScrapPreviewDto.listOf(filtered);
    }
}
