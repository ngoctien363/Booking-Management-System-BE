package com.tip.dg4.dc4.bookingmanagementsystem.models;

import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "booking_details")
public class BookingDetail {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private UUID id;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private String convenient;

    private BigDecimal price;

    private int maxPersonNum;

    @OneToOne()
    @JoinColumn(name = "room_status_id", referencedColumnName = "id")
    private RoomStatus roomStatus;

    @ManyToOne()
    @JoinColumn(name = "booking_history_id", referencedColumnName = "id")
    @Cascade(CascadeType.ALL)
    private BookingHistory bookingHistory;
}
