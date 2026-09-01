package repository;

import models.Phase;
import models.Project;
import models.Task;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    //Checks if a project exist with the given id, and returns the project if the project exist or null if the project does not exist.
    // public Project getProjectById(int projectId) {
    //     for (Project project : projects) {
    //         if (project.getId() == projectId) {
    //             return project;
    //         }
    //     }
    //     return null;
    // }

    //Updates the attributes of a project.
    // public void updateProject(Project project, String newProjectName, String newProjectDescription, double newProjectId,
    //                           String newProjectType, String newCreationDate,String newDeadline, String newStatus) {
    //     project.setProjectName(newProjectName);
    //     project.setProjectDescription(newProjectDescription);
    //     project.setProjectId(newProjectId);
    //     project.setProjectType(newProjectType);
    //     project.setCreationDate(newCreationDate);
    //     project.setDeadline(newDeadline);
    //     project.setStatus(newStatus);
    // }
    //Returns a list of all phases for a specific project.
    public List<Phase> getPhasesForProject(Project project) {
        return project.getPhases();
    }
    //Adds a phase to a project.
    public void addPhaseToProject(Project project, Phase phase) {
        project.getPhases().add(phase);
    }
    //Removes a phase from a project.
    public void removePhaseFromProject(Project project, Phase phase) {
        project.getPhases().remove(phase);
    }
    //Returns a list of all tasks that exist for a project.
    public List<Task> getTasksForProject(Project project) {
        List<Task> tasks = new ArrayList<>();
        for (Phase phase : project.getPhases()) {
            tasks.addAll(phase.getTasks());
        }
        return tasks;
    }
    //Returns a list of all incomplete tasks that exist for a project, by checking if task is marked as completed.
    public List<Task> getIncompleteTasksForProject(Project project) {
        List<Task> incompleteTasks = new ArrayList<>();
        for (Phase phase : project.getPhases()) {
            for (Task task : phase.getTasks()) {
                if (!task.isCompleted()) {
                    incompleteTasks.add(task);
                }
            }
        }
        return incompleteTasks;
    }
    // //Returns a list of all overdue tasks for a project, by checking if the deadling is passed before the task is removed.
    // public List<Task> getOverdueTasksForProject(Project project) {
    //     List<Task> overdueTasks = new ArrayList<>();
    //     for (Phase phase : project.getPhases()) {
    //         for (Task task : phase.getTasks()) {
    //             if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDateTime.now())) {
    //                 overdueTasks.add(task);
    //             }
    //         }
    //     }
    //     return overdueTasks;
    // }
    
    //Archieves a project by changing its status to "Archived".
    public void archiveProject(Project project){
        project.setStatus("Archived");
    }
    //Reactivates a project by changing its status to "Active".
    public void reactivateProject(Project project){
        project.setStatus("Active");
    }
}
