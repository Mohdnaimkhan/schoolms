package com.naim.school.schools;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.naim.school.sms.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SQLDelete(sql = "UPDATE schools SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted = false")
@Entity
@Table(name = "schools")
public class School extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String schoolCode;

    @Column(nullable = false, length = 150)
    private String schoolName;

    @Column(length = 50)
    private String shortName;

    @Column(length = 255)
    private String slogan;

    @Column(length = 50)
    private String board;

    @Column(length = 50)
    private String affiliationNo;

    @Column(length = 50)
    private String registrationNo;

    private Integer establishedYear;

    @Column(length = 100)
    private String principalName;

    @Column(length = 15)
    private String principalMobile;

    @Column(length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 15)
    private String alternatePhone;

    @Column(length = 100)
    private String website;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String district;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String country;

    @Column(length = 10)
    private String pinCode;

    private String logo;

    private String principalSignature;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private Boolean active = true;

}