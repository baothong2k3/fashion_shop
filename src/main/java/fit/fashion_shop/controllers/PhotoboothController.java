/*
 * @ (#) PhotoboothController.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.requests.PhotoboothThemeRequest;
import fit.fashion_shop.dtos.responses.PhotoboothThemeResponse;
import fit.fashion_shop.services.PhotoboothService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/photobooth-themes")
@RequiredArgsConstructor
public class PhotoboothController {

    private final PhotoboothService photoboothService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PhotoboothThemeResponse>> createTheme(
            @Valid @ModelAttribute PhotoboothThemeRequest request,
            HttpServletRequest httpReq) {

        PhotoboothThemeResponse response = photoboothService.createTheme(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Tạo chủ đề Photobooth thành công",
                        response,
                        httpReq.getRequestURI()
                ));
    }
}
