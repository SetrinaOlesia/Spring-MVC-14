package com.softserve.sprint14.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"task_id", "trainee_id"})})
public class Progress {

    public enum TaskStatus {
        NEW, PASS, FAIL, PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDate startDate;

    @UpdateTimestamp
    private LocalDate updateDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NEW;

    @ManyToOne
    @EqualsAndHashCode.Include
    private Task task;

    @ManyToOne
    @EqualsAndHashCode.Include
    private User trainee;
}
