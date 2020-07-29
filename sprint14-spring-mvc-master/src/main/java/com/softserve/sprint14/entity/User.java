package com.softserve.sprint14.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    public enum Role {
        MENTOR, TRAINEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    @EqualsAndHashCode.Include
    private String email;

    @NotBlank(message = "First name cannot be empty")
    @EqualsAndHashCode.Include
    private String firstName;

    @NotNull
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
    @EqualsAndHashCode.Include
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private Role role;

    @NotNull
    @Size(min = 6, max = 20, message = "Password must be between 6 and 12 characters")
    @ToString.Exclude
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "marathon_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "marathon_id"))
    @ToString.Exclude
    private List<Marathon> marathons = new ArrayList<>();

    @OneToMany(mappedBy = "trainee")
    @ToString.Exclude
    private List<Progress> progressList = new ArrayList<>();

}
