package com.softserve.sprint14.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ToString(exclude = {"sprints", "users"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Marathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isClosed = false;

    @NotBlank(message = "Marathon title cannot be empty")
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "marathon_user",
            joinColumns = @JoinColumn(name = "marathon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "marathon_id")
    private List<Sprint> sprints = new ArrayList<>();

//    @PreRemove
//    public void checkSprintAssociationBeforeRemoval() {
//        if (!this.sprints.isEmpty()) {
//            throw new CannotDeleteOwnerWithElementsException("Can't remove a marathon that has sprints.");
//        }
//    }
}