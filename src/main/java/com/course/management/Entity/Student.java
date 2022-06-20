package com.course.management.Entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name= "address")
    private String address;

    @Column(name = "zipcode")
    private int zipcode;

    @Column(name = "phone")
    private long phone;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "studentcourses",
            joinColumns = { @JoinColumn(name = "studentid") },
            inverseJoinColumns = { @JoinColumn(name = "courseid") })
    private Set<Course> courses = new HashSet<>();

    public Student(int id, String name, String address, int zipcode, long phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phone = phone;
    }

    public Student() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(long courseId) {
        Course course = this.courses.stream().filter(t -> t.getId() == courseId).findFirst().orElse(null);
        if (course != null) {
            this.courses.remove(course);
            course.getStudents().remove(this);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", zipcode=" + zipcode +
                ", phone=" + phone +
                '}';
    }


}
