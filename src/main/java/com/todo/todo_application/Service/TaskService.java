package com.todo.todo_application.Service;

import com.todo.todo_application.Ecxception.TaskNotFoundException;
import com.todo.todo_application.Entity.Task;
import com.todo.todo_application.Repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public Page<Task> getPaginatedTasks(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        logger.info("Fetching paginated tasks, page: {}, size: {}", page, size);
        return taskRepository.findAll(pageable);
    }
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks");
        return taskRepository.findAll();
    }
    public Task createTask(Task task) {
        logger.info("Creating a new task with title: {}", task.getTitle());
        return taskRepository.save(task);
    }
    public Task updateTask(Task task) {
        logger.info("Updating task with ID: {}", task.getId());
        return taskRepository.save(task);
    }
    public Task getTaskById(Long id) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() ->
                    new TaskNotFoundException("Task not found with ID: " + id)
            );
            logger.info("Retrieved task with ID: {}", id);
            return task;
        } catch (TaskNotFoundException e) {
            logger.error("Failed to retrieve task with ID {}: {}", id, e.getMessage());  // Correct usage of getMessage()
            throw e;
        }
    }
    public void deleteTask(Long id) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() ->
                    new TaskNotFoundException("Task not found with ID: " + id)
            );
            taskRepository.delete(task);
            logger.info("Successfully deleted task with ID: {}", id);
        } catch (TaskNotFoundException e) {
            logger.error("Failed to delete task with ID {}: {}", id, e.getMessage());  // Correct usage of getMessage()
            throw e;
        }
    }
    @Transactional
    public void saveTask(Task task) {
        logger.info("Saving task with title: {}", task.getTitle());
        taskRepository.save(task);
    }
}
