package org.example.rental_car.repository;

import org.example.rental_car.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review  r where r.car.id =:carId")
    Page<Review> findAllByCarId(@Param("carId") long carId, Pageable pageable);
    @Query("select r from Review  r where r.customer.id =:customerId")
    Page<Review> findAllByCustomerId(@Param("customerId") long customerId, Pageable pageable);
    @Query("select r from Review  r where r.customer.id =:customerId")
    List<Review> findAllByCustomerId1(@Param("customerId") long customerId);

    Long countByCarId(long id);

    List<Review> findByCarId(long carId);

    Optional<Review> findByCustomerIdAndCarId(long customerId, long carId);
}
