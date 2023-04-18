package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {

    @Autowired
    private UserBelongRepository userBelongRepository;


    public TaskService(TaskRepository repository, TaskMapper mapper) {
        super(repository, mapper);
    }

    public List<TaskTo> getAll() {
        List<Task> tasks = repository.getAll();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSprint() == null){
                iterator.remove();
            }
        }
        return mapper.toToList(tasks);
    }

    public List<TaskTo> findBySprintIdIsNull() {
        List<Task> all = repository.findAll();
        Iterator<Task> iterator = all.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSprint() != null){
                iterator.remove();
            }
        }
        return mapper.toToList(all);
    }

    public Task addTags(Long taskId, Set<String> tags) {
        Optional<Task> tsk = repository.findById(taskId);

        if (tsk.isPresent()) {
            Task task = tsk.get();
            task.setTags(tags);
            return repository.save(task);
        }
        return null;
    }

    public UserBelong taskSubscription(Long id) {
        AuthUser authUser = AuthUser.safeGet();

        Optional<Task> task = repository.findById(id);

        if (task.isPresent()) {

            if (authUser != null) {
                UserBelong userBelong = userBelongRepository.findUserBelongByObjectId(id);
                if (userBelong == null) {
                    Role role = authUser.getUser().getRoles().stream().findFirst().get();
                    userBelong = new UserBelong();
                    userBelong.setUserId(authUser.id());
                    userBelong.setObjectId(id);
                    userBelong.setObjectType(ObjectType.TASK);
                    userBelong.setUserTypeCode(role.toString());


                    return userBelongRepository.save(userBelong);
                }
            }
        }
        return null;
    }

    public int taskUnSub(Long id) {
        UserBelong userBelong = userBelongRepository.findUserBelongByObjectId(id);
        if (userBelong == null) {
            return 0;
        }
        int delete = userBelongRepository.delete(userBelong.id());
        return delete;
    }
}
