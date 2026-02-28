package com.example.demo.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessServiceRequest {
    private String name;
    private Integer displayOrder;
}