package com.tip.dg4.dc4.bookingmanagementsystem.repositories;

import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    boolean existsByNameAndAddress(String name, String address);
}
