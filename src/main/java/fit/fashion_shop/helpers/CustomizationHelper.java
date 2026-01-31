/*
 * @ (#) CustomizationHelper.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.helpers;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.AddToCartCustomizedRequest;
import fit.fashion_shop.dtos.requests.ShirtSelectionRequest;
import fit.fashion_shop.entities.*;
import fit.fashion_shop.enums.BagType;
import fit.fashion_shop.enums.StepType;
import fit.fashion_shop.exceptions.OperationNotPermittedException;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.PhotoboothThemeRepository;
import fit.fashion_shop.repositories.ProductCustomizationConfigRepository;
import fit.fashion_shop.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomizationHelper {

    private final ProductCustomizationConfigRepository configRepository;
    private final PhotoboothThemeRepository photoboothThemeRepository;
    private final CloudinaryService cloudinaryService;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public CustomizedSelection processCustomization(
            Product product,
            AddToCartCustomizedRequest request,
            List<MultipartFile> photoFiles
    ) {
        double totalExtraPrice = 0.0;
        CustomizedSelection selection = CustomizedSelection.builder()
                .product(product)
                .bagType(request.bagType())
                .build();

        List<ProductCustomizationConfig> configs = configRepository.findByProductId(product.getId());

        // 1. XỬ LÝ BAG
        ProductCustomizationConfig bagConfig = findConfig(configs, StepType.BAG);
        if (bagConfig != null && bagConfig.isEnabled()) {
            totalExtraPrice += bagConfig.getExtraPrice();

            // Parse Config Data để lấy thông tin màu túi
            Map<String, Object> bagData = parseJson(bagConfig.getConfigJson());
            Map<String, String> selectedBagColor = findColorInConfig(bagData, request.bagColorName());

            selection.setBagColorName(selectedBagColor.get("name"));
            selection.setBagColorCode(selectedBagColor.get("code"));
            selection.setBagColorImage(selectedBagColor.get("image"));
        } else {
            throw new OperationNotPermittedException("Sản phẩm không hỗ trợ tùy chỉnh túi hoặc cấu hình chưa được bật.");
        }

        // 2. XỬ LÝ SHIRTS
        ProductCustomizationConfig shirtConfig = findConfig(configs, StepType.SHIRT);
        if (shirtConfig != null && shirtConfig.isEnabled()) {
            // Validate số lượng áo dựa trên loại túi
            int requiredShirts = (request.bagType() == BagType.TWO_SHIRT) ? 2 : 1;
            if (request.shirts() == null || request.shirts().size() != requiredShirts) {
                throw new OperationNotPermittedException("Loại túi " + request.bagType() + " yêu cầu chọn đúng " + requiredShirts + " áo.");
            }

            // Tính tiền: Giả sử giá extra là tính cho bước này (hoặc nhân theo số lượng áo tùy business rule)
            // Ở đây tôi cộng giá extra của config 1 lần (như thiết kế DB hiện tại)
            totalExtraPrice += shirtConfig.getExtraPrice();

            Map<String, Object> shirtData = parseJson(shirtConfig.getConfigJson());
            List<String> validSizes = (List<String>) shirtData.get("sizes");

            List<CustomizedSelectionShirt> selectionShirts = new ArrayList<>();
            for (int i = 0; i < request.shirts().size(); i++) {
                ShirtSelectionRequest shirtReq = request.shirts().get(i);

                // Validate Size
                if (!validSizes.contains(shirtReq.size())) {
                    throw new OperationNotPermittedException("Size " + shirtReq.size() + " không hợp lệ.");
                }

                // Get Color Info
                Map<String, String> shirtColor = findColorInConfig(shirtData, shirtReq.colorName());

                selectionShirts.add(CustomizedSelectionShirt.builder()
                        .selection(selection)
                        .shirtIndex(i + 1)
                        .size(shirtReq.size())
                        .colorName(shirtColor.get("name"))
                        .colorCode(shirtColor.get("code"))
                        .colorImage(shirtColor.get("image"))
                        .build());
            }
            selection.setShirts(selectionShirts);
        }

        // 3. XỬ LÝ LETTER
        ProductCustomizationConfig letterConfig = findConfig(configs, StepType.LETTER);
        if (letterConfig != null && letterConfig.isEnabled()) {
            if (request.letterContent() != null && !request.letterContent().isBlank()) {
                totalExtraPrice += letterConfig.getExtraPrice();

                Map<String, Object> letterData = parseJson(letterConfig.getConfigJson());
                Integer maxLength = (Integer) letterData.getOrDefault("maxLength", 500);

                if (request.letterContent().length() > maxLength) {
                    throw new OperationNotPermittedException("Lời nhắn quá dài. Tối đa " + maxLength + " ký tự.");
                }
                selection.setLetterContent(request.letterContent());
            }
        }

        // 4. XỬ LÝ PHOTOBOOTH
        ProductCustomizationConfig photoConfig = findConfig(configs, StepType.PHOTOBOOTH);

        if (photoFiles != null && !photoFiles.isEmpty()) {
            if (photoConfig == null || !photoConfig.isEnabled()) {
                throw new OperationNotPermittedException("Sản phẩm không hỗ trợ Photobooth.");
            }
            if (request.photoboothThemeId() == null) {
                throw new OperationNotPermittedException("Vui lòng chọn chủ đề Photobooth.");
            }

            // Validate số lượng file và số lượng cấu hình slot phải khớp nhau
            // Ví dụ: Upload 2 ảnh thì phải gửi 2 mảng slot tương ứng [[...], [...]]
            if (request.photoSlotIndices() == null || request.photoSlotIndices().size() != photoFiles.size()) {
                throw new OperationNotPermittedException("Số lượng ảnh upload và cấu hình vị trí (slots) không khớp nhau.");
            }

            PhotoboothTheme theme = photoboothThemeRepository.findById(request.photoboothThemeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chủ đề không tồn tại."));

            selection.setPhotoboothTheme(theme);
            totalExtraPrice += photoConfig.getExtraPrice();

            List<CustomizedSelectionPhoto> selectionPhotos = new ArrayList<>();

            // --- LOGIC MỚI: 1 ẢNH -> NHIỀU SLOT ---
            for (int i = 0; i < photoFiles.size(); i++) {
                MultipartFile file = photoFiles.get(i);
                List<Integer> slotsForThisPhoto = request.photoSlotIndices().get(i);

                if (slotsForThisPhoto == null || slotsForThisPhoto.isEmpty()) {
                    throw new OperationNotPermittedException("Ảnh thứ " + (i + 1) + " chưa được gán slot nào.");
                }

                // 1. Upload ảnh lên Cloudinary (Chỉ 1 lần cho mỗi file)
                String photoUrl = cloudinaryService.uploadFile(file, "customization/user-photos");

                // 2. Tạo Entity cho từng slot mà ảnh này chiếm giữ
                for (Integer slotIndex : slotsForThisPhoto) {
                    selectionPhotos.add(CustomizedSelectionPhoto.builder()
                            .selection(selection)
                            .slotIndex(slotIndex)
                            .photoUrl(photoUrl) // Dùng chung URL
                            .build());
                }
            }
            selection.setPhotos(selectionPhotos);
        }

        selection.setTotalCustomPrice(totalExtraPrice);
        return selection;
    }

    // --- Private Utility Methods ---

    private ProductCustomizationConfig findConfig(List<ProductCustomizationConfig> configs, StepType type) {
        return configs.stream()
                .filter(c -> c.getStepType() == type)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error("Error parsing customization config JSON", e);
            throw new RuntimeException("Lỗi hệ thống khi đọc cấu hình sản phẩm.");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> findColorInConfig(Map<String, Object> data, String colorName) {
        List<Map<String, String>> colors = (List<Map<String, String>>) data.get("colors");
        if (colors == null) throw new OperationNotPermittedException("Cấu hình không tìm thấy danh sách màu.");

        return colors.stream()
                .filter(c -> c.get("name").equals(colorName))
                .findFirst()
                .orElseThrow(() -> new OperationNotPermittedException("Màu '" + colorName + "' không hợp lệ hoặc không tồn tại trong cấu hình."));
    }
}
