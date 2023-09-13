package org.example.model;

public record User(String id, String name, String username, String email, Address address) {
}

record Address(String street, String suite, String city, String zipcode, Geo geo) {
}

record Geo(String lat, String lng) {
}