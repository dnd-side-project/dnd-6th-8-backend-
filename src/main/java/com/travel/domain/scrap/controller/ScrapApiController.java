package com.travel.domain.scrap.controller;

import com.travel.domain.scrap.dto.ScrapPreviewDto;
//import com.travel.domain.scrap.dto.ScrapsSaveRequestDto;
import com.travel.domain.scrap.service.ScrapsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(tags = {"스크랩 API 정보를 제공"})
public class ScrapApiController {

    private final ScrapsService scrapsService;

    @ApiOperation(value = "스크랩 추가 api")
    @PostMapping("/archives/{ARCHIVE_ID}/scraps")
    public void addScraps(@PathVariable Long ARCHIVE_ID, @ApiIgnore Principal principal){
        scrapsService.addScraps(ARCHIVE_ID, principal.getName());
    }

    @ApiOperation(value = "스크랩 취소 api")
    @DeleteMapping("/archives/{SCRAP_ID}/unScraps")
    public void unScraps(@PathVariable long SCRAP_ID) {
        scrapsService.unScraps(SCRAP_ID);

    }

    @ApiOperation(value = "유저별 스크랩 목록 api")
    @GetMapping("/archives/scraps")
    public ResponseEntity<List<ScrapPreviewDto>> getScrapListByUser(@ApiIgnore Principal principal){
        List<ScrapPreviewDto> scrapPreviewDto = scrapsService.findByUser(principal.getName());
        return ResponseEntity.ok(scrapPreviewDto);
    }
}