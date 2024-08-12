package ru.marat.springcourse.models;

import jakarta.persistence.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters")
    //@Pattern(regexp = "[А-Я]\\w+, [А-Я]\\w+, [А-Я]\\w+", message = "Your ФИО should be in this format: Фамилия, Имя, Отчество")
    private String fio;

    @Min(value = 1900, message = "Min value is 1900")
    @Max(value = 2024, message = "Max value is 2024")
    private int year_of_birth;


    public Person(int id, String fio, int year_of_birth) {
        this.id = id;
        this.fio = fio;
        this.year_of_birth = year_of_birth;
        this.books = new ArrayList<>();
    }

    @OneToMany(mappedBy = "person")
    private List<Book> books;

    public Person() {
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getYear_of_birth() {
        return year_of_birth;
    }

    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", year_of_birth=" + year_of_birth +
                ", books=" + books +
                '}';
    }
}
