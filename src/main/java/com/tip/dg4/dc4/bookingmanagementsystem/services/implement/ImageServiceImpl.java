package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.models.Image;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.ImageRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public List<String> createImages(List<String> imageURLs, UUID objectId) {
        List<Image> savedImages = imageURLs.stream()
                .map(imageURL -> Image.builder().objectId(objectId).url(imageURL).build())
                .toList();

        return imageRepository.saveAll(savedImages).stream().map(Image::getUrl).toList();
    }

    @Override
    public List<String> getImagesByObjectId(UUID objectId) {
        return imageRepository.findByObjectId(objectId).stream().map(Image::getUrl).toList();
    }

    @Override
    public List<String> updateImages(List<String> imageURLs, UUID objectId) {
        this.deleteImagesByObjectId(objectId);

        return this.createImages(imageURLs, objectId);
    }

    @Override
    public void deleteImagesByObjectId(UUID objectId) {
        List<Image> images = imageRepository.findByObjectId(objectId);
        if (!CollectionUtils.isEmpty(images)) {
            imageRepository.deleteAll(images);
        }
    }
}
