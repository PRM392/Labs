package com.example.lab11;

public class Trainee {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String gender;

    public Trainee() {
    }

    public Trainee(String name, String email, String phone, String gender) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // Override toString để hiển thị đẹp trên ListView và Spinner
    @Override
    public String toString() {
        return "ID: " + id + " - " + name + " (" + email + ")";
    }
}
