package com.qxh.sb22.ioc;

public class StaticAnimalFactory {

    public static Animal getAnimal(String type) {
        if ("dog".equals(type)) {
            return new Dog();
        } else {
            return new Cat();
        }
    }

}
