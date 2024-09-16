package com.travel.domain.archive.dto;

import com.travel.domain.archive.entity.*;
import com.travel.domain.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@Getter
@Setter
@ApiModel(value = "아카이브 기록하기")
public class ArchivesSaveRequestDto {

    @ApiModelProperty(value = "아카이브 제목", example = "대충 다녀도 아름다운 제주")
    private String title;

    @ApiModelProperty(value = "여행 장소", example = "부산")
    private String places;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "출발 날짜", example = "2021-12-10")
    private String firstDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "마지막 날짜", example = "2021-12-15")
    private String lastDay;

    @ApiModelProperty(value = "동행 여부", example = "함께")
    private String haveCompanion;

    @ApiModelProperty(value = "예산 계획", example = "최소한")
    private EBudget budget;

    @ApiModelProperty(value = "기록 스타일", example = "감성")
    private EArchivingStyle archivingStyle;

    @Builder
    public ArchivesSaveRequestDto(String title, String place , String firstDay, String lastDay,
                                  String haveCompanion, EBudget budget, EArchivingStyle archivingStyle) {
        this.title = title;
        this.places = place;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.haveCompanion = haveCompanion;
        this.budget = budget;
        this.archivingStyle = archivingStyle;
    }

    public Archives toEntity(User user, Place place, String imageUrl) {
        System.out.println(user.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // yyyy-MM-dd HH:mm:ss

        LocalDate firstDayFormatted = null;
        LocalDate lastDayFormatted = null;
        Boolean haveCompanionFormatted = null;

        if(firstDay != null){
            firstDayFormatted = LocalDate.parse(firstDay, formatter);
        }
        if(lastDay != null){
            lastDayFormatted = LocalDate.parse(lastDay, formatter);
        }
        if(haveCompanion != null){
            haveCompanionFormatted = Boolean.parseBoolean(haveCompanion);
        }

        return Archives.builder().title(title).place(place)
                .firstDay(firstDayFormatted)
                .lastDay(lastDayFormatted)
                .haveCompanion(haveCompanionFormatted)
                .budget(budget)
                .archivingStyle(archivingStyle).user(user).coverImage(imageUrl).build();
    }
}
