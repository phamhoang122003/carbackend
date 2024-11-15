package com.spring.carbackend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UserEditDTO {
    private Integer id;
    private String name;
    private LocalDate dateOfBirth;
    private String nationalIdNo;
    private String phoneNo;
    private String email;
    private String address;
    private String drivingLicense;
    private BigDecimal wallet;
    // Chúng ta không nên gửi password trực tiếp trong DTO, nhưng nếu bạn cần, có thể thêm:
    private String password;
}
