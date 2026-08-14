package com.naim.school.timetable;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionRepository;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.classroom.ClassRoomRepository;
import com.naim.school.section.Section;
import com.naim.school.section.SectionRepository;
import com.naim.school.subject.SubjectRepository;
import com.naim.school.teacher.Teacher;
import com.naim.school.teacher.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public List<Timetable> getAll() {

        return timetableRepository.findAll();

    }

    public List<Timetable> getByAcademicSession(Long sessionId) {

        return timetableRepository.findByAcademicSession(
                academicSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new RuntimeException("Academic session not found.")));

    }

    public Timetable getById(Long id) {

        Timetable tt = timetableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timetable entry not found."));

        if (tt.getAcademicSession() != null) {
            tt.setAcademicSessionId(tt.getAcademicSession().getId());
        }

        if (tt.getClassRoom() != null) {
            tt.setClassRoomId(tt.getClassRoom().getId());
        }

        if (tt.getSection() != null) {
            tt.setSectionId(tt.getSection().getId());
        }

        if (tt.getSubject() != null) {
            tt.setSubjectId(tt.getSubject().getId());
        }

        if (tt.getTeacher() != null) {
            tt.setTeacherId(tt.getTeacher().getId());
        }

        return tt;

    }

    // Weekly grid entries for one class/section within one session
    public List<Timetable> getWeeklyGrid(Long sessionId, Long classRoomId, Long sectionId) {

        AcademicSession session = academicSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Academic session not found."));

        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new RuntimeException("Class not found."));

        if (sectionId == null) {

            return timetableRepository.findByAcademicSessionAndClassRoomAndSectionIsNull(session, classRoom);

        }

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found."));

        return timetableRepository.findByAcademicSessionAndClassRoomAndSection(session, classRoom, section);

    }

    @Transactional
    public Timetable save(Timetable timetable) {

        if (timetable.getAcademicSessionId() == null) {
            throw new RuntimeException("Academic session is required.");
        }

        AcademicSession academicSession = academicSessionRepository.findById(timetable.getAcademicSessionId())
                .orElseThrow(() -> new RuntimeException("Academic session not found."));

        if (timetable.getClassRoomId() == null) {
            throw new RuntimeException("Class is required.");
        }

        ClassRoom classRoom = classRoomRepository.findById(timetable.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("Class not found."));

        Section section = timetable.getSectionId() == null
                ? null
                : sectionRepository.findById(timetable.getSectionId())
                        .orElseThrow(() -> new RuntimeException("Section not found."));

        if (timetable.getSubjectId() == null) {
            throw new RuntimeException("Subject is required.");
        }

        if (timetable.getTeacherId() == null) {
            throw new RuntimeException("Teacher is required.");
        }

        Teacher teacher = teacherRepository.findById(timetable.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found."));

        if (timetable.getWeekDay() == null) {
            throw new RuntimeException("Day is required.");
        }

        if (timetable.getPeriodNumber() == null) {
            throw new RuntimeException("Period number is required.");
        }

        // Clash check 1: this class/section already has a DIFFERENT subject
        // scheduled for the same day + period.
        for (Timetable existing : timetableRepository.findByAcademicSessionAndClassRoomAndSectionAndWeekDay(
                academicSession, classRoom, section, timetable.getWeekDay())) {

            boolean samePeriod = existing.getPeriodNumber() != null
                    && existing.getPeriodNumber().equals(timetable.getPeriodNumber())
                    && !existing.getId().equals(timetable.getId());

            if (samePeriod) {

                throw new RuntimeException(
                        "This class already has a period " + timetable.getPeriodNumber()
                                + " scheduled on " + timetable.getWeekDay() + ".");

            }

        }

        // Clash check 2: this teacher is already teaching a DIFFERENT
        // class/section at the same day + period.
        for (Timetable existing : timetableRepository.findByAcademicSessionAndTeacherAndWeekDay(
                academicSession, teacher, timetable.getWeekDay())) {

            boolean samePeriod = existing.getPeriodNumber() != null
                    && existing.getPeriodNumber().equals(timetable.getPeriodNumber())
                    && !existing.getId().equals(timetable.getId());

            if (samePeriod) {

                throw new RuntimeException(
                        "Teacher " + teacher.getTeacherName()
                                + " is already scheduled for period " + timetable.getPeriodNumber()
                                + " on " + timetable.getWeekDay() + " in another class.");

            }

        }

        timetable.setAcademicSession(academicSession);
        timetable.setClassRoom(classRoom);
        timetable.setSection(section);
        timetable.setSubject(subjectRepository.findById(timetable.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found.")));
        timetable.setTeacher(teacher);

        return timetableRepository.save(timetable);

    }

    public void delete(Long id) {

        timetableRepository.deleteById(id);

    }

}
