package com.naim.school.classroom;

import com.naim.school.sms.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "class_rooms")
public class ClassRoom extends BaseEntity {

    @NotBlank(message = "Class Name is required")
    @Column(nullable = false, unique = true, length = 30)
    private String className;

    @Column
    private Integer displayOrder;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

}