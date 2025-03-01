package com.example.demo.model;

import com.example.demo.entity.TodoEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo {
    private Long id;
    private String title;
    private Boolean completed;

    public Todo() {}

    public static Todo toModelMapper (TodoEntity entity) {
        Todo model = new Todo();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setCompleted(entity.getCompleted());
        return model;
    }

    public void setCompleted(boolean status) {
        this.completed = status ;

    }    public void setTitle(String title) {
        this.title = title ;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @Override
//    public String toString() {
//        return "User{id=" + id + ", name='" + username + "'}";
//    }

}
