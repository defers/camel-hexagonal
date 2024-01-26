package com.defers.camel.infra.adapter.db.model;

public class UserModel {
    private int id;
    private String name;

    public UserModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}