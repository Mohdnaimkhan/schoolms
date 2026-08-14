package com.naim.school.timetable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.section.Section;
import com.naim.school.teacher.Teacher;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    // Weekly grid for one class/section in one session
    List<Timetable> findByAcademicSessionAndClassRoomAndSection(
            AcademicSession academicSession, ClassRoom classRoom, Section section);

    // Weekly grid for one class with no section
    List<Timetable> findByAcademicSessionAndClassRoomAndSectionIsNull(
            AcademicSession academicSession, ClassRoom classRoom);

    // All entries for a session (used for session-wise filter on the list page)
    List<Timetable> findByAcademicSession(AcademicSession academicSession);

    // A teacher's entries on a given day within a session (used for clash checking)
    List<Timetable> findByAcademicSessionAndTeacherAndWeekDay(
            AcademicSession academicSession, Teacher teacher, WeekDay weekDay);

    // A class/section's entries on a given day (used for clash checking)
    List<Timetable> findByAcademicSessionAndClassRoomAndSectionAndWeekDay(
            AcademicSession academicSession, ClassRoom classRoom, Section section, WeekDay weekDay);

}
