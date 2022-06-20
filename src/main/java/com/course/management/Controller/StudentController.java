package com.course.management.Controller;

import ch.qos.logback.core.net.SyslogConstants;
import com.course.management.Entity.Student;
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
public class StudentController {

    @Autowired
    StudentRepository studentRepository;
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(){

        try{
            List<Student> students = (List<Student>) studentRepository.findAll();
            if(students.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.info("Student Size " + students.size());
            return new ResponseEntity<>(students, HttpStatus.OK);
        }
        catch (Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") int id){

        try {
            Optional<Student> student = studentRepository.findById(id);
            if(student.isPresent()){
                return new ResponseEntity<>(student.get(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student){
        try{
            Student _student = studentRepository.save(student);
            return new ResponseEntity<>(_student, HttpStatus.CREATED);
        }catch (Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/students")
    public ResponseEntity<Student> updateTutorial(@RequestBody Student student) {
        Optional<Student> tutorialData = studentRepository.findById(student.getId());
        if (tutorialData.isPresent()) {
            Student _student = tutorialData.get();
            _student.setId(student.getId());
            _student.setAddress(student.getAddress());
            _student.setPhone(student.getPhone());
            _student.setZipcode(student.getZipcode());
            return new ResponseEntity<>(studentRepository.save(_student), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") int id) {
        try {
            studentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/students")
    public ResponseEntity<HttpStatus> deleteAllStudents() {
        try {
            studentRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
