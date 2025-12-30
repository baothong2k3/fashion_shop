/*
 * @ (#) AddressRepository.java    1.0    31/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void resetDefaultAddress(Long userId);
}
