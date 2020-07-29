package com.softserve.sprint14.controller;


import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.User;
import com.softserve.sprint14.service.MarathonService;
import com.softserve.sprint14.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/marathons")
@AllArgsConstructor
public class MarathonController {

    private MarathonService marathonService;

    private UserService userService;

    @GetMapping
    public String listMarathons(Model model) {
        List<Marathon> Marathons = marathonService.getAll();

        model.addAttribute("marathons", Marathons);

        model.addAttribute("showClosed", false);

        return "marathons/list-marathons";
    }

    @GetMapping("/{userId}")
    public String listMarathons(@PathVariable Long userId, Model theModel) {

        User user = userService.getUserById(userId);

        theModel.addAttribute("showClosed", false);

        theModel.addAttribute("user", user);

        theModel.addAttribute("marathons",
                marathonService.getAll());

        return "marathons/list-marathons";
    }

    @GetMapping("/add")
    public String showFormForAdd(Model model) {
        Marathon Marathon = new Marathon();

        model.addAttribute("marathon", Marathon);

        return "marathons/marathon-form";
    }

    @GetMapping("/edit/{marathonId}")
    public String showFormForUpdate(@PathVariable("marathonId") Long id,
                                    Model model) {

        Marathon Marathon = marathonService.getMarathonById(id);

        model.addAttribute("marathon", Marathon);

        return "marathons/marathon-form";
    }

    @PostMapping("/save")
    public String saveMarathon(
            @ModelAttribute("marathon") @Valid Marathon marathon,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "marathons/marathon-form";
        } else {
            marathonService.createOrUpdateMarathon(marathon);
            return "redirect:/marathons";
        }
    }

    @GetMapping("/close/{marathonId}")
    public String close(@PathVariable("marathonId") Long id) {

        marathonService.closeMarathonById(id);

        return "redirect:/marathons";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        marathonService.deleteMarathonById(id);

        return "redirect:/marathons";

    }

    @GetMapping("/view/{marathonId}")
    public String viewMarathon(@PathVariable Long marathonId, Model model) {

        Marathon Marathon = marathonService.getMarathonById(marathonId);

        model.addAttribute("marathon", Marathon);

        return "marathons/marathon-view";
    }

    @GetMapping("/closed")
    public String listClosedMarathons(Model theModel) {

        theModel.addAttribute("showClosed", true);

        theModel.addAttribute("marathons",
                marathonService.getAll());

        return "marathons/list-marathons";
    }

    @GetMapping("/open/{marathonId}")
    public String open(@PathVariable Long marathonId) {

        marathonService.openMarathonById(marathonId);

        return "redirect:/marathons";
    }
}
