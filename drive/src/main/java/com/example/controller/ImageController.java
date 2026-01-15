package com.example.controller;

import com.example.entity.Bike;
import com.example.entity.Image;
import com.example.repository.BikeRepository;
import com.example.repository.ImageRepository;
import com.example.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.aot.hint.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final BikeRepository bikeRepository;

    @PostMapping("/upload")
    public Image upload(@RequestParam("bikeId") Long bikeId,
                        @RequestParam("file") MultipartFile file) throws Exception {

        Bike bike = (Bike) bikeRepository.findById(bikeId)
                .orElseThrow(() -> new RuntimeException("Xe không tồn tại"));

        String url = imageService.uploadImage(file);

        Image image = new Image();
        image.setUrl(url);
        image.setBike(bike);

        return imageRepository.save(image);
    }

    @PostMapping("/upload-multi")
    public List<Image> uploadMultiple(
            @RequestParam("bikeId") Long bikeId,
            @RequestParam("files") List<MultipartFile> files
    ) throws Exception {

        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new RuntimeException("Xe không tồn tại"));

        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = imageService.uploadImage(file);

            Image image = new Image();
            image.setUrl(url);
            image.setBike(bike);

            images.add(imageRepository.save(image));
        }

        return images;
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImageBikeById(@PathVariable Long id){
        try{
            imageRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Xóa ảnh xe thành công!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Xóa ảnh thất bại!");
        }
    }

    @DeleteMapping("/delete/more")
    public ResponseEntity<?> deleteImageBikeById(@RequestBody JsonNode body) {
        List<Long> fileIds = new ArrayList<>();

        JsonNode idsNode = body.path("ids");
        if (idsNode.isArray()) {
            for (JsonNode idNode : idsNode) {
                fileIds.add(idNode.asLong());
            }
        }
        try{
            for(long i: fileIds){
                imageRepository.deleteById(i);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Xóa ảnh xe thành công!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Xóa ảnh thất bại!");
        }
    }
}
