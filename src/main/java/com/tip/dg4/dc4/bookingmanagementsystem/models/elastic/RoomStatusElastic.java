package com.tip.dg4.dc4.bookingmanagementsystem.models.elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(indexName = "room_status_elastic")
public class RoomStatusElastic {
    @Id
    @Field(type = FieldType.Text)
    private UUID id;
    private LocalDate checkOutDate;

    private int personNumber;

    private UUID roomId;
}
