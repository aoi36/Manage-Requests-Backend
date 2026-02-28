package com.example.demo.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessServiceResponse {
    private UUID id;
    private String name;
    private Integer displayOrder;
}