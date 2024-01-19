package com.tip.dg4.dc4.bookingmanagementsystem.dto.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to create the object that contains the info to update the info for the User
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/13/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateDto {
    @Nullable
    private String surname;

    @Nullable
    private String name;

    @Nullable
    @Pattern(regexp = "^\\d{10}$", message = "Phone number has 10 numbers!")
    private String phone;

	@JsonProperty(value = "isActive")
    private boolean isActive;
}
