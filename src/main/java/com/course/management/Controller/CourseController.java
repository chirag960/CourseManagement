package com.course.management.Controller;

import ch.qos.logback.core.net.SyslogConstants;
import com.course.management.Entity.Course;
import com.course.management.Entity.Student;
import com.course.management.Repository.CourseRepository;
import com.course.management.Repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses(){

        try{
            List<Course> courses = (List<Course>) courseRepository.findAll();
            if(courses.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.info("Course Size " + courses.size());
            return new ResponseEntity<>(courses, HttpStatus.OK);
        }
        catch (Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") int id){

        try {
            Optional<Course> course = courseRepository.findById(id);
            if(course.isPresent()){
                return new ResponseEntity<>(course.get(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> addCourse(@RequestBody Course course){
        try{
            Course _course = courseRepository.save(course);
            return new ResponseEntity<>(_course, HttpStatus.CREATED);
        }catch (Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/courses")
    public ResponseEntity<Course> updateStudent(@RequestBody Course course) {
        Optional<Course> studentData = courseRepository.findById(course.getId());
        if (studentData.isPresent()) {
            Course _course = studentData.get();
            _course.setId(course.getId());
            _course.setDepartment(course.getDepartment());
            _course.setTitle(course.getTitle());
            _course.setSemesterOffered(course.getSemesterOffered());
            return new ResponseEntity<>(courseRepository.save(_course), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") int id) {
        try {
            courseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/courses")
    public ResponseEntity<HttpStatus> deleteAllCourses() {
        try {
            courseRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<List<Course>> getAllCoursesByStudentId(@PathVariable(value = "studentId") Integer studentId) {
        try{
            if (!studentRepository.existsById(studentId)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<Course> courses = courseRepository.findCoursesByStudentsId(studentId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        }
        catch(Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<Student>> getAllStudentsByCourseId(@PathVariable(value = "courseId") Integer courseId) {
        try{
            if (!courseRepository.existsById(courseId)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<Student> students = studentRepository.findStudentsByCoursesId(courseId);
            return new ResponseEntity<>(students, HttpStatus.OK);
        }catch(Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/students/{studentId}/courses")
    public ResponseEntity<Course> addCourse(@PathVariable(value = "studentId") Integer studentId, @RequestBody Course courseRequest) {
        try{
            Optional<Student> student = studentRepository.findById(studentId);
            if(!student.isPresent()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Optional<Course> course = courseRepository.findById(courseRequest.getId());
            if(!course.isPresent()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Student _student = student.get();
            _student.addCourse(course.get());
            studentRepository.save(_student);
            return new ResponseEntity<>(course.get(), HttpStatus.CREATED);
        }catch(Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<HttpStatus> deleteCourseFromStudent(@PathVariable(value = "studentId") Integer studentId, @PathVariable(value = "courseId") Integer courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        Student _student = student.get();
        _student.removeCourse(courseId);
        studentRepository.save(_student);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
