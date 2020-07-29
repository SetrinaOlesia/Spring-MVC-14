package com.softserve.sprint14.controller;

import com.softserve.sprint14.dto.MarathonDTO;
import com.softserve.sprint14.dto.UserDTO;
import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.User;
import com.softserve.sprint14.service.MarathonService;
import com.softserve.sprint14.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    private MarathonService marathonService;

    private UserService userService;

    @Autowired
    public StudentController(MarathonService marathonService, UserService userService) {
        this.marathonService = marathonService;
        this.userService = userService;
    }

    @GetMapping("/{userId}/addMarathons/")
    public String addMarathonsToUsersForm(Model theModel, @PathVariable Long userId) {

        User user = userService.getUserById(userId);

        theModel.addAttribute("user", user);

        theModel.addAttribute("marathons", marathonService.getAll());

        return "students/add-marathons-to-students-form";
    }

    @PostMapping("/{userId}/add-marathons-to-student")
    public String addMarathonsToUser(
            @ModelAttribute("user") UserDTO user, @PathVariable Long userId,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "students/add-marathons-to-students-form";
        } else {
            User tempUser = userService.getUserById(userId);
            for (Long marathonId :
                    user.getMarathonIds()) {
                userService.addUserToMarathon(tempUser, marathonService.getMarathonById(marathonId));
            }
            return "redirect:/students";
        }
    }

    @GetMapping("/{marathonId}/add")
    public String addUsersForm(Model theModel, @PathVariable Long marathonId) {

        Marathon theMarathon = marathonService.getMarathonById(marathonId);

        theModel.addAttribute("marathon", theMarathon);

        theModel.addAttribute("users", userService.getAllByRole(User.Role.TRAINEE.toString()));

        return "students/add-students-form";
    }

    @PostMapping("/{marathonId}/add")
    public String addUsersToMarathon(
            @ModelAttribute("marathon") MarathonDTO marathon, @PathVariable Long marathonId,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "students/add-students-form";
        } else {
            Marathon tempMarathon = marathonService.getMarathonById(marathonId);
            for (Long userId :
                    marathon.getUserIds()) {
                userService.addUserToMarathon(userService.getUserById(userId), tempMarathon);
            }
            return "redirect:/students";
        }
    }

    @GetMapping
    public String listStudents(Model theModel) {
        theModel.addAttribute("students",
                userService.getAllByRole(User.Role.TRAINEE.toString()));
        return "students/list-students";
    }

    @GetMapping("/{marathonId}")
    public String listStudents(@PathVariable Long marathonId, Model theModel) {

        theModel.addAttribute("marathon", marathonService.getMarathonById(marathonId));

        theModel.addAttribute("students",
                userService.getAllByRole(User.Role.TRAINEE.toString()));

        return "students/list-students";
    }

    @GetMapping("/delete/{student_id}")
    public String delete(@PathVariable("student_id") Long id) {

        userService.deleteUser(userService.getUserById(id));

        return "redirect:/students";
    }

    @GetMapping("/remove/{student_id}/{marathon_id}")
    public String removeUserFromMarathon(@PathVariable("student_id") Long userId,
                                         @PathVariable("marathon_id") Long marathonId) {
        User user = userService.getUserById(userId);
        Marathon marathon = marathonService.getMarathonById(marathonId);

        userService.removeUserFromMarathon(user, marathon);

        return "redirect:/students";
    }

    @GetMapping("/add")
    public String showFormForAdd(Model Model) {
        User user = new User();

        user.setRole(User.Role.TRAINEE);

        Model.addAttribute("user", user);

        return "students/user-form";
    }

    @GetMapping("/edit/{student_id}")
    public String editStudent(@PathVariable("student_id") Long studentId,
                              Model model) {

        User User = userService.getUserById(studentId);

        model.addAttribute("user", User);

        return "students/user-form";
    }

    @PostMapping("/edit/save/")
    public String editStudent(@ModelAttribute("user") @Valid User user,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "students/user-form";
        } else {
            userService.createOrUpdateUser(user);
            return "redirect:/students";
        }
    }

    @GetMapping("/view/{studentId}")
    public String viewStudent(@PathVariable Long studentId, Model model) {

        User User = userService.getUserById(studentId);

        model.addAttribute("user", User);

        return "students/student-view";
    }
}
