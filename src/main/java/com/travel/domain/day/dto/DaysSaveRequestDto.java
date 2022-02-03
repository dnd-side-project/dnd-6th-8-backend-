package com.travel.domain.day.dto;

import com.travel.domain.day.entity.Days;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@ApiModel(value = "데이 피드 기록하기")
public class DaysSaveRequestDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value="다녀온 날짜", example = "2021-12-10", required = true)
    private LocalDate date;

    @ApiModelProperty(value="날씨", example="맑음", required = true)
    private String weather;

    @ApiModelProperty(value="피드 사진 링크", example="https://~~~", required = true)
    private String image;

    @ApiModelProperty(value="하루의 여정", example="제주도의 인생 맛집을 찾았다! 간장게장과", required = true)
    private String travelDescription;

    @ApiModelProperty(value="느꼈던 감정", example="가족과 오랜만에 갔던 여행이라 더 좋았다. 가족과의", required = true)
    private String emotionDescription;

    @ApiModelProperty(value="여행 꿀팁", example="성산일출봉 갈 때 주차장 자리 파악하고 가기! 그리고,", required = true)
    private String tipDescription;

    @Builder
    public DaysSaveRequestDto(LocalDate date, String weather, String image, String travelDescription, String emotionDescription, String tipDescription) {
        this.date = date;
        this.weather = weather;
        this.image = image;
        this.travelDescription = travelDescription;
        this.emotionDescription = emotionDescription;
        this.tipDescription = tipDescription;
    }

    public DaysSaveRequestDto() {}

    public Days toEntity() { return Days.builder().weather(weather).travelDescription(travelDescription).emotionDescription(emotionDescription).build(); }

}