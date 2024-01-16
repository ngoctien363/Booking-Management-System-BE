package com.tip.dg4.dc4.bookingmanagementsystem.repositories;

import com.tip.dg4.dc4.bookingmanagementsystem.models.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatus, UUID> {
    List<RoomStatus> findByRoomId(UUID roomId);

    boolean existsByRoomId(UUID roomId);
}
