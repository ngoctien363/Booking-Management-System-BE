package com.tip.dg4.dc4.bookingmanagementsystem.repositories;

import com.tip.dg4.dc4.bookingmanagementsystem.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findByObjectId(UUID objectId);

    boolean existsByObjectId(UUID objectId);
}
