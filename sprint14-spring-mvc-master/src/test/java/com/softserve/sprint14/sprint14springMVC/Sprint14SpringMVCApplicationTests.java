package com.softserve.sprint14.sprint14springMVC;

import com.softserve.sprint14.entity.*;
import com.softserve.sprint14.exception.CannotDeleteOwnerWithElementsException;
import com.softserve.sprint14.exception.EntityNotFoundByIdException;
import com.softserve.sprint14.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.sprint14.entity.Progress.TaskStatus.FAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;

;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sprint14SpringMVCApplicationTests {

    private static boolean setUpIsDone = false;

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Autowired
    ProgressService progressService;
    @Autowired
    SprintService sprintService;
    @Autowired
    MarathonService marathonService;

    @Autowired
    public Sprint14SpringMVCApplicationTests(UserService userService, TaskService taskService,
                                             ProgressService progressService, SprintService sprintService,
                                             MarathonService marathonService) {
        this.userService = userService;
        this.taskService = taskService;
        this.progressService = progressService;
        this.sprintService = sprintService;
        this.marathonService = marathonService;
    }

    @BeforeEach
    private void fillDataBase() {
        if(!setUpIsDone) {
            try {
                Marathon marathon = new Marathon();
                marathon.setTitle("Marathon1");
                marathonService.createOrUpdateMarathon(marathon);

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

                for (int i = 0; i < 2; i++) {
                    Sprint sprint = new Sprint();
                    sprint.setTitle("Sprint" + i);
                    sprint.setStartDate(LocalDate.now());
                    sprint.setFinishDate(LocalDate.now().plusMonths(3));
                    sprintService.createOrUpdateSprint(sprint);
                    sprintService.addSprintToMarathon(sprint, marathon);

                    for (int j = 0; j < 2; j++) {
                        Task task = new Task();
                        task.setTitle("Task" + i + j);
                        taskService.createOrUpdateTask(task);
                        taskService.addTaskToSprint(task, sprint);

                        List<User> trainees = userService.getAllByRole("TRAINEE");
                        for (User trainee :
                                trainees) {
                            progressService.addTaskForStudent(task, trainee);
                        }
                    }
                }
            } catch (ConstraintViolationException |
                    CannotDeleteOwnerWithElementsException |
                    EntityNotFoundByIdException e) {
                System.out.println(e.getMessage());
            }
            setUpIsDone = true;
        }
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(userService);
        Assertions.assertNotNull(taskService);
        Assertions.assertNotNull(progressService);
        Assertions.assertNotNull(sprintService);
        Assertions.assertNotNull(marathonService);
    }

    @Test
    @Order(1)
    public void checkGetAllUsersByRole() {
        List<User> actualMentors = userService.getAllByRole(User.Role.MENTOR.toString());
        List<User> actualTrainees = userService.getAllByRole(User.Role.TRAINEE.toString());

        actualMentors.sort(Comparator.comparing(User::getId));
        actualTrainees.sort(Comparator.comparing(User::getId));

        List<User> expectedMentors = new ArrayList<>();
        List<User> expectedTrainees = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            User mentor = new User();
            mentor.setEmail("mentoruser" + i + "@dh.com");
            mentor.setFirstName("MentorName" + i);
            mentor.setLastName("MentorSurname" + i);
            mentor.setPassword("qwertyqwerty" + i);
            mentor.setRole(User.Role.MENTOR);
            expectedMentors.add(mentor);

            User trainee = new User();
            trainee.setEmail("traineeUser" + i + "@dh.com");
            trainee.setFirstName("TraineeName" + i);
            trainee.setLastName("TraineeSurname" + i);
            trainee.setPassword("qwerty^qwerty" + i);
            trainee.setRole(User.Role.TRAINEE);
            expectedTrainees.add(trainee);
        }

        assertEquals(expectedMentors, actualMentors, "checkGetAllMentors()");
        assertEquals(expectedTrainees, actualTrainees, "checkGetAllTrainees()");
    }

    @Test
    @Order(1)
    public void checkAllProgressByUserIdAndMarathonId() {
        List<Long> expected = Arrays.asList(1L, 3L, 5L, 7L);
        List<Long> actual = progressService.allProgressByUserIdAndMarathonId(2L, 1L).stream()
                .map(Progress::getId).collect(Collectors.toList());
        assertEquals(expected, actual, "checkAllProgressByUserIdAndMarathonId()");
    }

    @Test
    @Order(1)
    public void checkAllProgressByUserIdAndSprintId() {
        List<Long> expected = Arrays.asList(1L, 3L);
        List<Long> actual = progressService.allProgressByUserIdAndSprintId(2L, 1L).stream()
                .map(Progress::getId).collect(Collectors.toList());
        assertEquals(expected, actual, "checkAllProgressByUserIdAndSprintId()");
    }

    @Test
    @Order(1)
    public void checkSetStatus() {
        progressService.setStatus(FAIL, progressService.getProgressById(1L));
        Progress.TaskStatus actual = progressService.getProgressById(1L).getStatus();
        assertEquals(FAIL, actual, "checkSetStatus()");
    }

    @Test
    @Order(2)
    public void checkCreateOrUpdateUser() {
        User mentor1 = userService.getUserById(1L);
        User mentor2 = userService.getUserById(3L);
        User trainee1 = userService.getUserById(2L);
        List<User> actual = new ArrayList<>();
        actual.add(mentor1);
        actual.add(mentor2);
        actual.add(trainee1);
        mentor1.setFirstName("ChangedMentor1");
        mentor2.setFirstName("ChangedMentor2");
        trainee1.setFirstName("ChangedTrainee1");
        userService.createOrUpdateUser(mentor1);
        userService.createOrUpdateUser(mentor2);
        userService.createOrUpdateUser(trainee1);

        List<User> expected = new ArrayList<>();

        User user1 = new User();
        user1.setEmail("mentoruser0@dh.com");
        user1.setFirstName("ChangedMentor1");
        user1.setLastName("MentorSurname0");
        user1.setPassword("qwertyqwerty0");
        user1.setRole(User.Role.MENTOR);
        expected.add(user1);

        User user2 = new User();
        user2.setEmail("mentoruser1@dh.com");
        user2.setFirstName("ChangedMentor2");
        user2.setLastName("MentorSurname1");
        user2.setPassword("qwertyqwerty1");
        user2.setRole(User.Role.MENTOR);
        expected.add(user2);

        User user3 = new User();
        user3.setEmail("traineeUser0@dh.com");
        user3.setFirstName("ChangedTrainee1");
        user3.setLastName("TraineeSurname0");
        user3.setPassword("qwerty^qwerty0");
        user3.setRole(User.Role.TRAINEE);
        expected.add(user3);

        assertEquals(expected, actual, "checkUpdateUsers()");
    }

    @Test
    @Order(2)
    public void checkCreateOrUpdateProgress() {
        Progress progress = progressService.getProgressById(1L);
        progress.setTask(taskService.getTaskById(3L));
        progress.setTrainee(userService.getUserById(1L));
        Progress actual = progressService.createOrUpdateProgress(progress);
        actual = progressService.getProgressById(actual.getId());
        assertEquals(progress, actual, "checkSetStatus()");
    }

    @Test
    @Order(2)
    public void checkCreateOrUpdateMarathon() {
        Marathon marathon = marathonService.getMarathonById(1L);
        marathon.setTitle("MarathonNew");
        Marathon actual = marathonService.createOrUpdateMarathon(marathon);
        actual = marathonService.getMarathonById(actual.getId());
        assertEquals(marathon, actual, "checkCreateOrUpdateMarathon()");
    }

    @Test
    @Order(2)
    public void checkCreateOrUpdateSprint() {
        Sprint sprint = sprintService.getSprintById(1L);
        sprint.setTitle("newSprint");
        Sprint actual = sprintService.createOrUpdateSprint(sprint);
        actual = sprintService.getSprintById(actual.getId());
        assertEquals(sprint, actual, "checkCreateOrUpdateSprint()");
    }

    @Test
    @Order(2)
    public void checkCreateOrUpdateTask() {
        Task task = taskService.getTaskById(1L);
        task.setTitle("newTask");
        Task actual = taskService.createOrUpdateTask(task);
        assertEquals(task, actual, "checkCreateOrUpdateTask()");
    }


    @Test
    @Order(4)
    public void checkDeleteUser() {
        Throwable e = new Throwable();
        userService.deleteUser(userService.getUserById(1L));
        try {
            userService.getUserById(1L);
        } catch (EntityNotFoundByIdException ex) {
            e = ex;
        }
        assertEquals(EntityNotFoundByIdException.class, e.getClass(), "checkDeleteUser()");
    }

    @Test
    @Order(3)
    public void checkDeleteProgress() {
        Throwable e = new Throwable();
        progressService.deleteProgress(progressService.getProgressById(1L));
        try {
            progressService.getProgressById(1L);
        } catch (EntityNotFoundByIdException ex) {
            e = ex;
        }
        assertEquals(EntityNotFoundByIdException.class, e.getClass(), "checkDeleteProgress()");
    }

    @Test
    @Order(5)
    public void checkDeleteTaskFail() {
        Throwable e = new Throwable();
        try {
            taskService.deleteTask(taskService.getTaskById(1L));
        } catch (RuntimeException ex) {
            e = ex;
        }
        assertEquals("Can't remove a task that has progress entities.", e.getMessage(), "checkDeleteTaskFail()");
    }

    @Test
    @Order(7)
    public void checkDeleteTaskSuccess() {
        Throwable e = new Throwable();
        Task task = taskService.getTaskById(1L);
        List<Progress> progressList = progressService.getAllProgressesOfTask(task);
        for (Progress p :
                progressList) {
            progressService.deleteProgress(p);
        }
        taskService.deleteTask(task);
        try {
            taskService.getTaskById(1L);
        } catch (EntityNotFoundByIdException ex) {
            e = ex;
        }
        assertEquals(EntityNotFoundByIdException.class, e.getClass(), "checkDeleteTaskSuccess()");
    }

    @Test
    @Order(5)
    public void checkDeleteSprintFail() {
        Throwable e = new Throwable();
        try {
            sprintService.deleteSprint(sprintService.getSprintById(1L));
        } catch (RuntimeException ex) {
            e = ex;
        }
        assertEquals("Can't remove a sprint that has tasks.", e.getMessage(), "checkDeleteSprintFail()");
    }

    @Test
    @Order(8)
    public void checkDeleteSprintSuccess() {
        Throwable e = new Throwable();
        Sprint sprint = sprintService.getSprintById(1L);
        Task task = taskService.getTaskById(2L);
        List<Progress> progressList = progressService.getAllProgressesOfTask(task);
        for (Progress p :
                progressList) {
            progressService.deleteProgress(p);
        }
        taskService.deleteTask(task);
        sprintService.deleteSprint(sprint);
        try {
            sprintService.getSprintById(1L);
        } catch (EntityNotFoundByIdException ex) {
            e = ex;
        }
        assertEquals(EntityNotFoundByIdException.class, e.getClass(), "checkDeleteSprintSuccess()");
    }

    @Test
    @Order(5)
    public void checkDeleteMarathonFail() {
        Throwable e = new Throwable();
        try {
            marathonService.deleteMarathonById(1L);
        } catch (CannotDeleteOwnerWithElementsException ex) {
            e = ex;
        }
        assertEquals("Can't remove a marathon that has sprints.", e.getMessage(), "checkDeleteMarathonFail()");
    }

    @Test
    @Order(9)
    public void checkDeleteMarathonSuccess() {
        Throwable e = new Throwable();
        Sprint sprint = sprintService.getSprintById(2L);
        for (Long i = 3L; i < 5; i++) {
            Task task = taskService.getTaskById(i);
            List<Progress> progressList = progressService.getAllProgressesOfTask(task);
            for (Progress p :
                    progressList) {
                progressService.deleteProgress(p);
            }
            taskService.deleteTask(task);
        }
        sprintService.deleteSprint(sprint);
        marathonService.deleteMarathonById(1L);
        try {
            marathonService.getMarathonById(1L);
        } catch (EntityNotFoundByIdException ex) {
            e = ex;
        }
        assertEquals(EntityNotFoundByIdException.class, e.getClass(), "checkDeleteMarathonSuccess()");
    }
}
