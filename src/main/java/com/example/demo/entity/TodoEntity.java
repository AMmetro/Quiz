package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Boolean completed;

    @ManyToOne                        //много todo  к одному user
    @JoinColumn(name = "user_id")     // внешний ключ по которому связаны таблицы
    @JsonBackReference                // убирает циклическую зависимость с UserEntity друг на друга
    private UserEntity user;          // user совпадает с mappedBy = "user" в UserEntity

    public TodoEntity(){
    }
//test
    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean status) {
        this.completed = status;
    }

    public UserEntity getUser() {
        System.out.println();
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "TodoEntity{" +
                "title='" + title + '\'' +
                ", completed='" + completed + '\'' +  ", id='" + id + '\'' +
                '}';

    }

//    public String getDescription() {
//        return description;
//    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}




