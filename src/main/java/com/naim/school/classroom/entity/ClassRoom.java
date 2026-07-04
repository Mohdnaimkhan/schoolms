package com.naim.school.classroom.entity;

import com.naim.school.sms.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "class_rooms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom extends BaseEntity {

    @NotBlank(message = "Class Name is required")
    @Column(nullable = false, length = 30)
    private String className;

    @NotBlank(message = "Section is required")
    @Column(nullable = false, length = 10)
    private String section;

    @Column(unique = true, length = 30)
    private String classCode;

    @Column(length = 30)
    private String roomNumber;

    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;

    @Column(length = 500)
    private String description;

    @Builder.Default
    private Boolean active = true;

}