package com.naim.school.feehead;

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
@Table(name = "fee_heads")
public class FeeHead extends BaseEntity {

    @NotBlank(message = "Fee Head Name is required")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

}
