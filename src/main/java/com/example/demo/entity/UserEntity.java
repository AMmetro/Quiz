
package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity                             //(name = "user_table") - кастомное имя можно указать
@Table(name = "user_entity_table")  // или так - без этого таблица создается с именем равным именем класса
public class UserEntity {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName="user_sequence",
            allocationSize = 5
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, columnDefinition = "TEXT", unique = true)
    private String username;

    private String password;

    /*
     * Каскадное удаление задач если удаляется пользователь
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<TodoEntity> todos;

    public UserEntity() {
    }

    public List<TodoEntity> getTodos() {
        return todos;
    }

    public void setTodos(List<TodoEntity> todos) {
        this.todos = todos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';

    }

}

