/*
 * @ (#) CustomizedSelectionResponse.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.CustomizedSelection;
import fit.fashion_shop.enums.BagType;
import java.util.List;
import java.util.stream.Collectors;

public record CustomizedSelectionResponse(
        Long id,
        BagType bagType,
        String bagColorName,
        String bagColorCode,
        String bagColorImage,
        String letterContent,
        String photoboothThemeName,
        Double totalCustomPrice,
        List<CustomizedShirtResponse> shirts,
        List<CustomizedPhotoResponse> photos
) {
    public static CustomizedSelectionResponse fromEntity(CustomizedSelection selection) {
        if (selection == null) return null;

        // Map Shirts
        List<CustomizedShirtResponse> shirtResponses = selection.getShirts().stream()
                .map(s -> new CustomizedShirtResponse(
                        s.getShirtIndex(),
                        s.getSize(),
                        s.getColorName(),
                        s.getColorCode(),
                        s.getColorImage()
                ))
                .collect(Collectors.toList());

        // Map Photos
        List<CustomizedPhotoResponse> photoResponses = null;
        if (selection.getPhotos() != null) {
            photoResponses = selection.getPhotos().stream()
                    .map(p -> new CustomizedPhotoResponse(
                            p.getSlotIndex(),
                            p.getPhotoUrl()
                    ))
                    .collect(Collectors.toList());
        }

        return new CustomizedSelectionResponse(
                selection.getId(),
                selection.getBagType(),
                selection.getBagColorName(),
                selection.getBagColorCode(),
                selection.getBagColorImage(),
                selection.getLetterContent(),
                selection.getPhotoboothTheme() != null ? selection.getPhotoboothTheme().getName() : null,
                selection.getTotalCustomPrice(),
                shirtResponses,
                photoResponses
        );
    }
}
