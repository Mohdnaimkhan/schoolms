package com.naim.school.subject.entity;

import com.naim.school.classroom.entity.ClassRoom;
import com.naim.school.sms.entity.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends BaseEntity {

    @NotBlank(message = "Subject Name is required")
    @Column(nullable = false)
    private String subjectName;

    @Column(unique = true)
    private String subjectCode;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    @Column(length = 500)
    private String description;

    @Builder.Default
    private Boolean active = true;

}