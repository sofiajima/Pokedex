package com.example.pokedex.models;

public class Pokemon {


    private int number;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        String[] urlSplited = url.split("/");
        return Integer.parseInt(urlSplited[urlSplited.length - 1]);
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
