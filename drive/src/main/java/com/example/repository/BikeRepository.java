package com.example.repository;

import com.example.entity.Bike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long>{
    Page<Bike> findAll(Pageable pageable);

    Page<Bike> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("""
    SELECT b FROM Bike b
    WHERE (:hot IS NULL OR b.hot = :hot)
      AND (:minPrice IS NULL OR b.price >= :minPrice)
      AND (:maxPrice IS NULL OR b.price <= :maxPrice)
      AND (:maxSpeed IS NULL OR b.maxSpeed = :maxSpeed)
      AND (:manufactureYear IS NULL OR b.manufactureYear = :manufactureYear)
      AND (:model IS NULL OR LOWER(b.model) LIKE LOWER(CONCAT('%', :model, '%')))
      AND (:brand IS NULL OR LOWER(b.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
      AND (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:state IS NULL OR LOWER(b.state) LIKE LOWER(CONCAT('%', :state, '%')))
""")
    Page<Bike> filterBike(
            @Param("hot") Integer hot,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("maxSpeed") Integer maxSpeed,
            @Param("manufactureYear") Integer manufactureYear,
            @Param("model") String model,
            @Param("brand") String brand,
            @Param("name") String name,
            @Param("state") String state,
            Pageable pageable
    );

}
