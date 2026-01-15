package com.example.controller;

import com.example.Dto.BikeDto;
import com.example.entity.Bike;
import com.example.repository.BikeRepository;
import com.example.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bike")
public class BikeController {

    private final BikeRepository bikeRepository;
    private final ImageService imgService;

    @GetMapping("/getAll")
    public ResponseEntity<Page<Bike>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Bike> result = bikeRepository.findAll(pageable);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Bike>> searchByName(@RequestBody JsonNode body) {

        String name = body.path("name").asText("");
        int page = body.path("page").asInt(0);
        int size = body.path("size").asInt(30);
        String sortBy = body.path("sortBy").asText("id");
        String direction = body.path("direction").asText("desc");
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Bike> result = bikeRepository
                .findByNameContainingIgnoreCase(name, pageable);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<Bike>> filterBike(@RequestBody JsonNode body) {
        // ===== hot (nullable) =====
        Integer hot = null;
        JsonNode hotNode = body.get("hot");
        if (hotNode != null && !hotNode.isNull()) {
            hot = hotNode.asInt();
        }

        // ===== price =====
        Long minPrice = null;
        JsonNode minPriceNode = body.get("minPrice");
        if (minPriceNode != null && !minPriceNode.isNull()) {
            minPrice = minPriceNode.asLong();
        }

        Long maxPrice = null;
        JsonNode maxPriceNode = body.get("maxPrice"); // ❗ sửa lại đúng key
        if (maxPriceNode != null && !maxPriceNode.isNull()) {
            maxPrice = maxPriceNode.asLong();
        }

        // ===== maxSpeed (<=) =====
        Integer maxSpeed = null;
        JsonNode maxSpeedNode = body.get("maxSpeed");
        if (maxSpeedNode != null && !maxSpeedNode.isNull()) {
            maxSpeed = maxSpeedNode.asInt();
        }

        // ===== manufactureYear (<=) =====
        Integer manufactureYear = null;
        JsonNode manufactureYearNode = body.get("manufactureYear");
        if (manufactureYearNode != null && !manufactureYearNode.isNull()) {
            manufactureYear = manufactureYearNode.asInt();
        }

        // ===== model (ignore case) =====
        String model = body.path("model").asText("");

        // ===== brand (ignore case) =====
        String brand = body.path("brand").asText("");

        // ===== name (ignore case) =====
        String name = body.path("name").asText("");

        // ===== state (ignore case) =====
        String state = body.path("state").asText("");

        // ===== paging & sort =====
        int page = body.path("page").asInt(0);
        int size = body.path("size").asInt(10);
        String sortBy = body.path("sortBy").asText("id");
        String direction = body.path("direction").asText("desc");

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Bike> result = bikeRepository.filterBike(
                hot,
                minPrice,
                maxPrice,
                maxSpeed,
                manufactureYear,
                model,
                brand,
                name,
                state,
                pageable
        );


        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBikeById(@PathVariable Long id) {
        Optional<Bike> bk = bikeRepository.findById(id);
        if(bk == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy xe");
        }
        return ResponseEntity.ok(bk);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBike (  @RequestPart("bike") BikeDto bike,
                                           @RequestPart("avatarFile") MultipartFile avatarFile) throws IOException {
        String urlAvatar = imgService.uploadImage(avatarFile);
        Bike bk = new Bike(bike.getName(), bike.getColor(), bike.getBrand(),bike.getModel(), bike.getPrice(),
                bike.getState(), bike.getManufactureYear(), bike.getOtherTech(), bike.getTraveledKm(), bike.getMachineCondition(), bike.getDescription()
        ,bike.getAdvantage(),bike.getMaxSpeed(), urlAvatar, LocalDateTime.now(),LocalDateTime.now(), bike.getHot());
        try{
            Bike saveBike = bikeRepository.save(bk);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveBike);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tạo thất bại hãy thử lại!");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBikeById (@PathVariable Long id){
        try{
            bikeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Xóa thành công!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Xóa thất bại hãy thử lại!");
        }
    }

    @DeleteMapping("/delete/more")
    public ResponseEntity<?> deleteMoreBikeById (@RequestBody JsonNode body){
        List<Long> bikeIds = new ArrayList<>();

        JsonNode idsNode = body.path("ids");
        if (idsNode.isArray()) {
            for (JsonNode idNode : idsNode) {
                bikeIds.add(idNode.asLong());
            }
        }
        try{
            for(Long i: bikeIds){
                bikeRepository.deleteById(i);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Xóa thành công!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Xóa thất bại hãy thử lại!");
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBikeById(@RequestPart("bike") BikeDto bike,
                                            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) throws IOException {
        Bike bk = bikeRepository.getById(bike.getId());
        bk.setName(bike.getName());
        bk.setColor(bike.getColor());
        bk.setBrand(bike.getBrand());
        bk.setModel(bike.getModel());
        bk.setPrice(bike.getPrice());
        bk.setState(bike.getState());
        bk.setManufactureYear(bike.getManufactureYear());
        bk.setOtherTech(bike.getOtherTech());
        bk.setTraveledKm(bike.getTraveledKm());
        bk.setMachineCondition(bike.getMachineCondition());
        bk.setDescription(bike.getDescription());
        bk.setAdvantage(bike.getAdvantage());
        bk.setMaxSpeed(bike.getMaxSpeed());
        bk.setHot(bike.getHot());
        if(avatarFile != null && !avatarFile.isEmpty()){
            bk.setAvatar(imgService.uploadImage(avatarFile));
        }
        try{
            bikeRepository.save(bk);
            return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cập nhật thất bại hãy thử lại!");
        }
    }
}