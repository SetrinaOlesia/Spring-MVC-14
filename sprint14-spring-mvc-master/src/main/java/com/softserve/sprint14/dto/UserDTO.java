package com.softserve.sprint14.dto;

import com.softserve.sprint14.entity.Progress;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String password;
    private String[] marathons;
    private List<Progress> progressList;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName,
                   String email, String role, String password, String[] marathons, List<Progress> progressList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.password = password;
        this.marathons = marathons;
        this.progressList = progressList;
    }

    public List<Long> getMarathonIds() {
        return Arrays.stream(marathons).map(s -> {
            Pattern pattern = Pattern.compile("(?<=id=).+(?=, isClosed=)");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                return Long.parseLong(matcher.group(0));
            }
            pattern = Pattern.compile("(?<=id=).+");
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                return Long.parseLong(matcher.group(0));
            }
            return null;
        }).distinct().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getMarathons() {
        return marathons;
    }

    public void setMarathons(String[] marathons) {
        this.marathons = marathons;
    }

    public List<Progress> getProgressList() {
        return progressList;
    }

    public void setProgressList(List<Progress> progressList) {
        this.progressList = progressList;
    }
}
