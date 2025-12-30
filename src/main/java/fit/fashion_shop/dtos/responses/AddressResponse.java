/*
 * @ (#) AddressResponse.java    1.0    31/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.entities.Address;

public record AddressResponse(
        Long id,
        String recipientName,
        String phoneNumber,
        String street,
        String city,
        String district,
        String ward,
        boolean defaultAddress
) {
    public static AddressResponse fromAddress(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getRecipientName(),
                address.getPhoneNumber(),
                address.getStreet(),
                address.getCity(),
                address.getDistrict(),
                address.getWard(),
                address.isDefaultAddress()
        );
    }
}
