package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

    /*
     * @Transient - не сохраняет столбец в базу данных но делает его доступным для вычисления и
     * возврата значения
     */
    @Transient
    @Column(name = "age")
    private Long age;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "confirmed")
    private boolean confirmed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /*
     * Каскадное удаление задач если удаляется пользователь
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference      // убирает циклическую зависимость с TodoEntity друг на друга
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

    public void setDob(long year) {
        LocalDate currentDate = LocalDate.now();
        this.dob = currentDate.minusYears(year).withDayOfYear(1);
    }

    public LocalDate getDob() {
        return dob;
    }

    public Long getCurrentAge (LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        if ((dob != null)) {
            return (long) Period.between(dob, currentDate).getYears();
        } else {
            return null;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public Long getAge () {
        return age;
    }

    public void setAge (Long age) {
        this.age = age;
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
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

