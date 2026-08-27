package com.lycheetech.movingapi.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {

    boolean existsByItemName(String itemName);

    List<InventoryItem> findAllByIsActiveTrue();
}

