/*
 * @ (#) PhotoboothPublicController.java    1.0    25/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 25/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.responses.PhotoboothThemeResponse;
import fit.fashion_shop.services.PhotoboothService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/photobooth-themes")
@RequiredArgsConstructor
public class PhotoboothPublicController {

    private final PhotoboothService photoboothService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PhotoboothThemeResponse>>> getAllThemes(HttpServletRequest httpReq) {

        List<PhotoboothThemeResponse> themes = photoboothService.getAllThemes();

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Lấy danh sách chủ đề Photobooth thành công",
                themes,
                httpReq.getRequestURI()
        ));
    }
}
