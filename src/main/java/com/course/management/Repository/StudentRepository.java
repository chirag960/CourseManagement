package com.course.management.Repository;

import com.course.management.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findStudentsByCoursesId(Integer courseId);
}
