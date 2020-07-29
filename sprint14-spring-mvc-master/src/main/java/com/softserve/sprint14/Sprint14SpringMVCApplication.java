package com.softserve.sprint14;

import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.Sprint;
import com.softserve.sprint14.entity.User;
import com.softserve.sprint14.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Sprint14SpringMVCApplication implements CommandLineRunner {

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Autowired
    MarathonService marathonService;
    @Autowired
    SprintService sprintService;
    @Autowired
    ProgressService progressService;

    public static void main(String[] args) {
        SpringApplication.run(Sprint14SpringMVCApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running Spring Boot Application");
        Marathon marathon = new Marathon();
        marathon.setTitle("Marathon1");
        marathonService.createOrUpdateMarathon(marathon);
        Marathon marathon2 = new Marathon();
        marathon2.setTitle("Marathon2");
        marathonService.createOrUpdateMarathon(marathon2);
        for (int i = 0; i < 2; i++) {
            Sprint sprint = new Sprint();
            sprint.setTitle("Sprint" + i);
            sprint.setStartDate(LocalDate.now());
            sprint.setFinishDate(LocalDate.now().plusMonths(3));
            sprint.setMarathon(marathon);
            sprintService.createOrUpdateSprint(sprint);
            sprintService.addSprintToMarathon(sprint, marathon);
            Sprint sprint2 = new Sprint();
            sprint2.setTitle("Sprint" + (i + 2));
            sprint2.setStartDate(LocalDate.now());
            sprint2.setFinishDate(LocalDate.now().plusMonths(6));
            sprint2.setMarathon(marathon2);
            sprintService.createOrUpdateSprint(sprint2);
            sprintService.addSprintToMarathon(sprint2, marathon2);
        }
        for (int i = 0; i < 2; i++) {
            User mentor = new User();
            mentor.setEmail("mentoruser" + i + "@dh.com");
            mentor.setFirstName("MentorName" + i);
            mentor.setLastName("MentorSurname" + i);
            mentor.setPassword("qwertyqwerty" + i);
            mentor.setRole(User.Role.MENTOR);
            userService.createOrUpdateUser(mentor);
            userService.addUserToMarathon(mentor, marathon);

            User trainee = new User();
            trainee.setEmail("traineeUser" + i + "@dh.com");
            trainee.setFirstName("TraineeName" + i);
            trainee.setLastName("TraineeSurname" + i);
            trainee.setPassword("qwerty^qwerty" + i);
            trainee.setRole(User.Role.TRAINEE);
            userService.createOrUpdateUser(trainee);
            userService.addUserToMarathon(trainee, marathon);
        }
    }
}
