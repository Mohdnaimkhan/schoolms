package com.naim.school.session;


import com.naim.school.sms.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "academic_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicSession extends BaseEntity {

    @NotBlank(message = "Session Name is required")
    @Column(nullable = false, unique = true)
    private String sessionName;

    @Builder.Default
    private Boolean active = false;

    @Column(length = 300)
    private String description;

}