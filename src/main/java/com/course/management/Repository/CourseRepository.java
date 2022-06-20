package com.course.management.Repository;

import com.course.management.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findCoursesByStudentsId(Integer studentId);
}
