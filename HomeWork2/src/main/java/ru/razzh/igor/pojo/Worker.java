package ru.razzh.igor.pojo;

public class Worker {
    private String name;
    private int age;
    private String position;

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Worker(String name, int age, String post) {
        this.name = name;
        this.age = age;
        this.position = post;
    }
}
