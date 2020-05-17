package com.qxh.sb22.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HelloService {

    private Student student;

    @Autowired
    @Qualifier("monkey")
    private Animal animal;

    public Student getStudent() {
        return student;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String hello(){
        return "hello";
    }

    public String hello2() {
        return animal.getName();
    }
}
