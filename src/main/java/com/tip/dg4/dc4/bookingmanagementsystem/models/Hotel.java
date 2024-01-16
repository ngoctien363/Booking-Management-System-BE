package com.tip.dg4.dc4.bookingmanagementsystem.models;

import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.Destination;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "hotels")
public class Hotel {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String address;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private Destination destination;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isActive;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<BookingHistory> bookingHistories;
}
