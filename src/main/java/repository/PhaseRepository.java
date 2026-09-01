package repository;

import models.Phase;
import models.Task;
import models.Meeting;

import java.util.ArrayList;
import java.util.List;

public class PhaseRepository {
    private List<Phase> phases;
    //private List<Task> tasks;


    public PhaseRepository() {
        this.phases = new ArrayList<>();
    }
    //Checks if a phase exist with the given id, and returns the phase if the phase exist or null if the phase does not exist.
    public Phase getPhaseById(String phaseId) {
        for (Phase phase : phases) {
            if (phase.getId() == phaseId) {
                return phase;
            }
        }
        return null;
    }
    //Returns a list of all taskes that are related to a specific phase.
    public List<Task> getTasksForPhase(Phase phase) {
        return phase.getTasks();
    }
    //Returns a list of all meetings that are related to a specific phase.
    public List<Meeting> getMeetingsForPhase(Phase phase) {
        return phase.getMeetings();
    }
    //Checks if a phase exist before updating its attributes.
    public void updatePhase(Phase phase, String name, String id) {
        if (phases.contains(phase)) {
            phase.setName(name);
            phase.setId(id);
        }
    }
    //Returns a list of all incomplete tasks in a phase, by checking if the tasks are marked as completed.
    public List<Task> getIncompleteTasksForPhase(Phase phase) {
        List<Task> incompleteTasks = new ArrayList<>();
        for (Task task : phase.getTasks()) {
            if (!task.isCompleted()) {
                incompleteTasks.add(task);
            }
        }
        return incompleteTasks;
    }

}
