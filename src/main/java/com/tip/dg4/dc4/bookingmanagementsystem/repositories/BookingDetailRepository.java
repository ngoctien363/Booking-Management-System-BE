package com.tip.dg4.dc4.bookingmanagementsystem.repositories;

import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingDetail;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, UUID> {
    List<BookingDetail> findByBookingHistory(BookingHistory bookingHistory);
}
