/*
 * @ (#) PhotoboothService.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.PhotoboothThemeRequest;
import fit.fashion_shop.dtos.responses.PhotoboothThemeResponse;

public interface PhotoboothService {
    PhotoboothThemeResponse createTheme(PhotoboothThemeRequest request);
}
