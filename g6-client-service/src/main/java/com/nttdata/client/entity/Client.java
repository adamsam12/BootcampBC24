package com.nttdata.client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    private String id;
    private String dni;
    private String lastName;
    private String firstName;
    private String names;
    private LocalDate dateBirth;
    private String address;
    private String nationality;
    private Integer clientType; //0 Personal; 1 Empresarial
    private String clientSubType; // "V" Vip; "P" PYME

}
