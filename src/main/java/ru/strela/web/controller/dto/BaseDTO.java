package ru.strela.web.controller.dto;

public class BaseDTO {

    protected int id;

    public BaseDTO() {}

    public BaseDTO(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
