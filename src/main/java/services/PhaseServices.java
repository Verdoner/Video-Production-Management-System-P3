package services;

import models.Phase;

import java.util.List;

public class PhaseServices {

    private List<Phase> phases;
    //Adds a phase to a list of all phases.
    public void addPhase(Phase phase) {
        phases.add(phase);
    }
    //Removes a phase from a list of all phases.
    public void removePhase(Phase phase) {
        phases.remove(phase);
    }
    //Returns a list of all phases.
    public List<Phase> getAllPhases() {
        return phases;
    }
}
