package org.example.domain;

import java.util.List;

class Country {

    private String name;
    private List<String> cities;

    public Country() {
    }

    public Country(String name, List<String> cities) {
        this.name = name;
        this.cities = cities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
