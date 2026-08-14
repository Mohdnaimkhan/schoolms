package com.naim.school.section;

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
@Table(name = "sections")
public class Section extends BaseEntity {

    @NotBlank(message = "Section Name is required")
    @Column(nullable = false, unique = true, length = 20)
    private String sectionName;

    @Column
    private Integer displayOrder;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(length = 500)
    private String description;

}