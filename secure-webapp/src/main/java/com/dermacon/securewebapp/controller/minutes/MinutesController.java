package com.dermacon.securewebapp.controller.minutes;

import com.dermacon.securewebapp.data.Flatmate;
import com.dermacon.securewebapp.data.Task;
import com.dermacon.securewebapp.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MinutesController {

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping(value = "/minutes", method= RequestMethod.GET)
    public String displayGroceryList(Model model) {
        model.addAttribute("selectedDomain", "minutes");
        model.addAttribute("inputTask", new Task());

//        model.addAttribute("newItems", taskRepository);
//        model.addAttribute("oldItems", );
//        model.addAttribute("selectedItems", new SelectedItems());

        return "minutes";
    }

    @PostMapping("/minutes")
    public String addNewItem(@ModelAttribute("task") Task task) {
//        Task task = new Task();

        // todo builder mit parsing funktionalitaet

//        task.setTaskTitle();
//        task.setDescription(formInput.getDescription());
//        task.setPublishingDate(new Date(System.currentTimeMillis()));
//
//        LoggerSingleton.getInstance().info("add new task: " + task);

//        Long id = (long)300;
//        Set<Task> tasks = taskRepository.findAllByResponsibleFlatmates_flatmateId(id);


        return "redirect:/minutes";
    }

    private String highlightText(String text) {
        Set<Flatmate> foundFlatmates = parseNames(text);
        for (Flatmate fm : foundFlatmates) {
            String username = fm.getUser().getUsername();
            text = text.replaceAll(
                    "@" + username,
                    "<em>@" + username + "</em>"
            );
        }
        return text;
    }

    private Set<Flatmate> parseNames(String text) {
        Set<Flatmate> mentionedNames = new HashSet<>();

        String regex = "@(\\w*)";

        Matcher m = Pattern.compile(regex)
                .matcher(text);
        while (m.find()) {
            System.out.println(m.group());
        }

        return mentionedNames;
    }

}
