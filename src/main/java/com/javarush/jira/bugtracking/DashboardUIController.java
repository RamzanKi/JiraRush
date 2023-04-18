package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapperImpl;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.to.SprintTo;
import com.javarush.jira.bugtracking.to.TaskTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping
public class DashboardUIController {

    private TaskService taskService;

    @GetMapping("/") // index page
    public String getAll(Model model) {
        List<TaskTo> tasks = taskService.getAll();
        Map<SprintTo, List<TaskTo>> taskMap = tasks.stream()
                .collect(Collectors.groupingBy(TaskTo::getSprint));
        model.addAttribute("taskMap", taskMap);
        return "index";
    }

    @PostMapping("/task/{ID}")
    public ResponseEntity<TaskTo> addTag(@PathVariable("ID") long id, @RequestBody Set<String> tags) {
        Task task = taskService.addTags(id, tags);

        TaskMapperImpl taskMapper = new TaskMapperImpl();
        TaskTo taskTo = taskMapper.toTo(task);

        return ResponseEntity.ok(taskTo);
    }

    @GetMapping("/subscribe/{ID}")
    public ResponseEntity<?> taskSubscription(@PathVariable("ID") long id) {
        UserBelong userBelong = taskService.taskSubscription(id);
        if (userBelong != null) {
            return ResponseEntity.ok(userBelong);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }

    @GetMapping("/unsubscribe/{ID}")
    public ResponseEntity<?> taskUnSubscription(@PathVariable("ID") long id) {
        int delId = taskService.taskUnSub(id);
        System.out.println();
        if (delId > 0) {
            return ResponseEntity.status(HttpStatus.OK).body("unsub from task");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }

//    @GetMapping("/backlog")
//    public String getBacklog(Model model) {
//        List<TaskTo> tasks = taskService.findBySprintIdIsNull();
//
//        Map<String, List<TaskTo>> taskMap = tasks.stream()
//                .collect(Collectors.groupingBy(TaskTo::getPriorityCode));
//        model.addAttribute("backlog", taskMap);
//
////        model.addAttribute("backlog", tasks);
//        return "backlog";
//    }

    @GetMapping("/backlog")
    public String getBacklog(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        List<TaskTo> tasks = taskService.findBySprintIdIsNull();

        int totalPages = (int) Math.ceil((double) tasks.size() / size);
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, tasks.size());

        tasks = tasks.subList(startIndex, endIndex);
        Map<String, List<TaskTo>> taskMap = tasks.stream()
                .collect(Collectors.groupingBy(TaskTo::getPriorityCode));
        model.addAttribute("backlog", taskMap);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "backlog";
    }
}
