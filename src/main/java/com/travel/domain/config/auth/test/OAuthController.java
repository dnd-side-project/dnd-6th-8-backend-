package com.travel.domain.config.auth.test;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Component
@AllArgsConstructor
@Api(tags = {"사용자인증 API"})
public class OAuthController {

    private String url = "http://localhost:8080/oauth2/authorization/kakao";
    @ResponseBody
    @GetMapping("/kakao")
    public ResponseEntity<String> socialLogin(@RequestParam String loginType) {
        return new ResponseEntity<String>(url,HttpStatus.OK);
    }
}


