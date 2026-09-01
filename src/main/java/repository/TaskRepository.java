package repository;

import models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private List<Task> tasks;

    //Checks if a task exist with the given id, and returns the task if the task exist or null if the task does not exist.
    public Task getTaskById(String id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
    //Returns a list of all incomplete tasks, by checking is a task is marked as completed.
    public List<Task> getIncompleteTasks() {
        List<Task> incompleteTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                incompleteTasks.add(task);
            }
        }
        return incompleteTasks;
    }
    //Marks a task as completed.
    public void markTaskAsCompleted(Task task) {
        task.setCompleted(true);
    }
    //Updates the attributes of a task.
    public void updateTask(Task task, String newTaskName, String newTaskDescription, String newTaskId) {
        task.setName(newTaskName);
        task.setDescription(newTaskDescription);
        task.setId(newTaskId);
    }
    //Assigns a task to a specific employee.
    public void assignTaskTo(Task task, String employeeId){
        task.setEmployeeId(employeeId);
    }
}
