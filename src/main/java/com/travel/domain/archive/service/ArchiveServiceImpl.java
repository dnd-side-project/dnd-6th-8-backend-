package com.travel.domain.archive.service;

import com.travel.domain.archive.dto.*;
import com.travel.domain.archive.entity.*;
import com.travel.domain.archive.repository.ArchivesRepository;
import com.travel.domain.archive.repository.PlaceRepository;
import com.travel.domain.archive.repository.ReportRepository;
import com.travel.domain.common.S3Uploader;
import com.travel.domain.emoji.repository.UserEmojiSelectedRepository;
import com.travel.domain.user.entity.Survey;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl implements ArchivesService {

    private final ArchivesRepository archivesRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final S3Uploader s3Uploader;
    private final ReportRepository reportRepository;
    private final UserEmojiSelectedRepository userEmojiSelectedRepository;

    @Override
    @Transactional()
    public ArchiveDetailResponseDto saveArchive(MultipartFile coverImage, ArchivesSaveRequestDto archivesSaveRequestDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        boolean placeExists = placeRepository.existsByName(archivesSaveRequestDto.getPlaces());

        Place place = placeHandler(archivesSaveRequestDto.getPlaces());

        String imageUrl = null;

        System.out.println(coverImage == null);
        if (coverImage != null) {
            System.out.println("image not null");
            try {
                imageUrl = s3Uploader.upload(coverImage
                        , "archive");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Archives archive = archivesRepository.save(archivesSaveRequestDto.toEntity(user, place, imageUrl));
        return new ArchiveDetailResponseDto(archive);
    }

    @Override
    @Transactional()
    public ArchiveDetailResponseDto updateArchive(MultipartFile coverImage, ArchiveUpdateRequestDto archiveUpdateRequestDtoRequestDto, String userEmail, Long archiveId) {
        User user = userRepository.findByEmail(userEmail);
        Archives archive = archivesRepository.getById(archiveId);

        if (archiveUpdateRequestDtoRequestDto.getTitle() !=null && !archiveUpdateRequestDtoRequestDto.getTitle().equals(archive.getTitle())) {
            System.out.println("updating");
            archive.setTitle(archiveUpdateRequestDtoRequestDto.getTitle());
        }
        if (archiveUpdateRequestDtoRequestDto.getFirstDay() !=null && !archiveUpdateRequestDtoRequestDto.getFirstDay().equals(archive.getFirstDay())) {
            archive.setFirstDay(LocalDate.parse(archiveUpdateRequestDtoRequestDto.getFirstDay()));
        }
        if (archiveUpdateRequestDtoRequestDto.getLastDay() !=null && !archiveUpdateRequestDtoRequestDto.getLastDay().equals(archive.getLastDay())) {
            archive.setLastDay(LocalDate.parse(archiveUpdateRequestDtoRequestDto.getLastDay()));
        }
        if (archiveUpdateRequestDtoRequestDto.getArchivingStyle() !=null && !archiveUpdateRequestDtoRequestDto.getArchivingStyle().equals(archive.getArchivingStyle())) {
            archive.setArchivingStyle(archiveUpdateRequestDtoRequestDto.getArchivingStyle());
        }
        if (archiveUpdateRequestDtoRequestDto.getPlaces() !=null && !archiveUpdateRequestDtoRequestDto.getPlaces().equals((archive.getPlace()))) {
            boolean placeExists = placeRepository.existsByName(archiveUpdateRequestDtoRequestDto.getPlaces());
            Place place = placeHandler(archiveUpdateRequestDtoRequestDto.getPlaces());
            archive.setPlace(place);
        }
        if (archiveUpdateRequestDtoRequestDto.getBudget() !=null && !archiveUpdateRequestDtoRequestDto.getBudget().equals(archive.getBudget())) {
            archive.setBudget(archiveUpdateRequestDtoRequestDto.getBudget());
        }
        if (archiveUpdateRequestDtoRequestDto.getHaveCompanion() !=null
                && Boolean.parseBoolean(archiveUpdateRequestDtoRequestDto.getHaveCompanion()) != archive.getHaveCompanion()) {
            archive.setHaveCompanion(Boolean.parseBoolean(archiveUpdateRequestDtoRequestDto.getHaveCompanion()));
        }

        String imageUrl = null;

        if (coverImage != null && !coverImage.isEmpty()) {
            System.out.println("image not null");
            try {
                imageUrl = s3Uploader.upload(coverImage
                        , "archive");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        archive.setCoverImage(imageUrl);
        System.out.println(archive.getTitle());
        archivesRepository.save(archive);
        return new ArchiveDetailResponseDto(archive);
    }

    @Override
    @Transactional(readOnly = true)
    public ArchiveDetailResponseDto findById(Long id) {
        Archives archive = archivesRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));
        return new ArchiveDetailResponseDto(archive);
    }

    @Override
    @Transactional(readOnly = true)
    public HomeResponse getMain(String userEmail) {
        Pageable limitThree = PageRequest.of(0,3);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        User user = userRepository.findByEmail(userEmail);
        List<Archives> archivesList = userEmojiSelectedRepository.orderByCount(limitThree);
        long totalArchiveNum = archivesRepository.count();
        return new HomeResponse(archivesList, totalArchiveNum, user.getUserName());
    }

    @Override
    public void setBadges(Long id, EBadges badges) {
        Archives archive = archivesRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));
        archive.setBadges(badges);
        archivesRepository.save(archive);
    }

    public Place placeHandler(String placeName) {
        boolean placeExists = placeRepository.existsByName(placeName);
        Place place = null;
        if (placeExists) {
            place = placeRepository.getByName(placeName);
        } else {
            place = placeRepository.save(place.builder()
                    .name(placeName).build());
        }
        return place;
    }


    @Override
    public void updateArchiveShare(Long id, boolean isShare) {

        Archives archive = archivesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));

        if (archive.isShare() != isShare) {
            archive.setShare(isShare);
        }
        archivesRepository.save(archive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArchiveResponseDto> findByPlace(String place) {
        Place placeEntity = placeRepository.getByName(place);
        List<Archives> filtered = archivesRepository.findByPlace_Id(placeEntity.getId());
        return ArchiveResponseDto.listOf(filtered);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArchiveResponseDto> findByRecommendation(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        List<Archives> archivesList;
        if(user.getSurvey() != null){
            archivesList = findBySurvey(user.getSurvey());
        }else{
            archivesList = defaultRecommendation();
        }
        return ArchiveResponseDto.listOf(archivesList);
    }

    private List<Archives> findBySurvey(Survey survey){
        System.out.println(survey.getBudget());
        return archivesRepository.findByArchivingStyleAndBudgetAndHaveCompanion(survey.getArchivingStyle().toString(),survey.getBudget().toString(),survey.isHaveCompanion());
    }

    private List<Archives> defaultRecommendation(){
        return archivesRepository.findRandom();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Archives archive = archivesRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));
        if(archive.getCoverImage() != null){
            s3Uploader.deleteS3(archive.getCoverImage(), "archive");
        }
        archivesRepository.delete(archive);
    }

    @Override
    public void reportArchive(long archiveId, String userEmail, EReportType reportType){
        Archives archives = archivesRepository.getById(archiveId);
        User user = userRepository.findByEmail(userEmail);
        Report report = Report.builder().reportType(reportType).archives(archives).user(user).build();
        reportRepository.save(report);
    }


}
