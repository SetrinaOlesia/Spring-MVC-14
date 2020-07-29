package com.softserve.sprint14.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDate createDate;

    @UpdateTimestamp
    private LocalDate updateDate;

    @NotBlank(message = "Task title cannot be empty")
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sprint_id")
    @EqualsAndHashCode.Include
    @ToString.Exclude
    private Sprint sprint;

    @OneToMany(mappedBy = "task")
    @ToString.Exclude
    private List<Progress> progressList;

//    @PreRemove
//    public void checkProgressAssociationBeforeRemoval() {
//        if (!this.progressList.isEmpty()) {
//            throw new CannotDeleteOwnerWithElementsException("Can't remove a task that has progress entities.");
//        }
//    }
}
