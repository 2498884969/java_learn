package com.qxh.sb22.ioc;

public class AnimalFactory {

    public Animal getAnimal(String type) {
        if ("dog".equals(type)) {
            return new Dog();
        } else {
            return new Cat();
        }
    }

}
