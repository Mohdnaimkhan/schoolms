package com.naim.school.subject;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


import com.naim.school.classroom.ClassRoom;
import com.naim.school.sms.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@SQLDelete(sql = "UPDATE subjects SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted = false")
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