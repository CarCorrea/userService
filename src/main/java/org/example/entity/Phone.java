package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Phone {

    @Id
    @GeneratedValue
    private Long id;

    private Long phoneNumber;

    private int cityCode;

    private int countryCode;

}
