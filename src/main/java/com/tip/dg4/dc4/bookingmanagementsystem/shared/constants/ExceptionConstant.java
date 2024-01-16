package com.tip.dg4.dc4.bookingmanagementsystem.shared.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionConstant {
    //  Common
    public static final String COMMON_E001 = "Some fields are not allowed to be empty";

    //  Hotel
    public static final String HOTEL_E001 = "Hotel already exists with name and address";
    public static final String HOTEL_E002 = "Hotel not found with id: ";
    public static final String HOTEL_E003 = "Hotel is living in history";

    //  Room
    public static final String ROOM_E001 = "Room already exists with room number in this hotel";
    public static final String ROOM_E002 = "Room not found with that id";
    public static final String ROOM_E003 = "Room type is invalid";
    public static final String ROOM_E004 = "Room is scheduled for use";
    public static final String ROOM_E005 = "Check-in or check-out date time is invalid";
    public static final String ROOM_E006 = "Room number is invalid";

    //  User
    public static final String USER_E001 = "This user does not exist!";
    public static final String USER_E002 = "Wrong user name or password!";
    public static final String USER_E003 = "Wrong password!";
    public static final String USER_E004 = "This email is existing!";
    public static final String USER_E005 = "Wrong email!";
    public static final String USER_E006 = "Wrong OTP!";
    public static final String USER_E007 = "Wrong Token!";
    public static final String USER_E008 = "The expiration of OTP is out!";
    public static final String USER_E009 = "Can not send email!";
    public static final String USER_E010 = "No permission!";
    public static final String USER_E011 = "Page index or page size is invalid!";
    public static final String USER_E012 = "The expiration of token is out!";
    public static final String USER_E013 = "The token is invalid!";
    public static final String USER_E014 = "Fields can not be null!";

    //  Booking history
    public static final String BHISTORY_E001 = "No more rooms available for room: ";
    public static final String BHISTORY_E002 = "Booking history does not found by id: ";
    public static final String BHISTORY_E003 = "Room has been resolved";
    public static final String BHISTORY_E004 = "Room is in use";
}
