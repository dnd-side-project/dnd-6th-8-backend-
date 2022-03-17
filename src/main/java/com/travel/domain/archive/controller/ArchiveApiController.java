package com.travel.domain.archive.controller;

import com.travel.domain.archive.dto.ArchiveDetailResponseDto;
import com.travel.domain.archive.dto.ArchiveResponseDto;
import com.travel.domain.archive.dto.ArchivesSaveRequestDto;
import com.travel.domain.archive.entity.EPlaces;
import com.travel.domain.archive.service.ArchivesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(tags = {"게시글 API 정보를 제공"})
public class ArchiveApiController {

    private final ArchivesService archivesService;


    @ApiOperation(value = "아카이브 생성 api")
    @PostMapping(path = "/archives", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ArchiveDetailResponseDto> saveArchive
            (@ModelAttribute ArchivesSaveRequestDto archivesSaveRequestDto, @ApiIgnore Principal principal){
        System.out.println(principal.getName());
        System.out.println("controller");
        System.out.println(archivesSaveRequestDto.getCoverPicture());
        ArchiveDetailResponseDto archivesResponseDto = archivesService.saveArchive(archivesSaveRequestDto, principal.getName());
        return ResponseEntity.created(URI.create("/api/v1/archives" + archivesResponseDto.getId()))
                .body(archivesResponseDto);
    }

    @ApiOperation(value = "아카이브 id로 가져오기 API")
    @GetMapping("/archives/{id}")
    public ResponseEntity<ArchiveDetailResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(archivesService.findById(id));
    }

    @ApiOperation(value = "아카이브 업데이트 API")
    @PutMapping("/archives/{id}")
    public ResponseEntity<Void> updateArchive
            (@PathVariable Long id, @RequestBody ArchivesSaveRequestDto archivesSaveRequestDto){
        archivesService.updateArchive(id, archivesSaveRequestDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "아카이브 공유여부변경 API")
    @PutMapping("/archives/{id}/share")
    public ResponseEntity<Void> changeArchiveStatus
            (@PathVariable Long id, @RequestParam boolean isShare){
        archivesService.updateArchiveShare(id, isShare);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "장소별로 게시물 필터링 API")
    @GetMapping("/archives/places")
    public ResponseEntity<List<ArchiveResponseDto>> getArchiveListByPlace(@RequestParam String place){
        List<ArchiveResponseDto> archivesResponseDtos = archivesService.findByPlace(place);
        return ResponseEntity.ok(archivesResponseDtos);
    }

    @ApiOperation(value = "추천 게시물 API")
    @GetMapping("/archives/suggestions")
    public ResponseEntity<List<ArchiveResponseDto>> getArchiveRecommendation(@ApiIgnore Principal principal){
        List<ArchiveResponseDto> archivesResponseDtos = archivesService.findByRecommendation(principal.getName());
        return ResponseEntity.ok(archivesResponseDtos);
    }



    @ApiOperation(value = "아카이브 삭제 API")
    @DeleteMapping("/archives/{archiveId}")
    public ResponseEntity<Void> deleteArchive(@PathVariable Long archiveId){
        archivesService.delete(archiveId);
        return ResponseEntity.noContent().build();
    }

}
