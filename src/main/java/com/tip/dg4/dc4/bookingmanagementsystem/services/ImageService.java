package com.tip.dg4.dc4.bookingmanagementsystem.services;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    List<String> createImages(List<String> imageURLs, UUID objectId);

    List<String> getImagesByObjectId(UUID objectId);

    List<String> updateImages(List<String> imageURLs, UUID objectId);

    void deleteImagesByObjectId(UUID objectId);
}
