package ani.elevator.entity;

import java.util.ArrayList;
import java.util.List;

//elevator system has list of elevators

public class ElevatorSystemDTO {
    public List<Elevator> getElevators() {
        return elevators;
    }

    private final List<Elevator> elevators;
    private


    public ElevatorSystemDTO(int numOfElevators) {
        this.elevators = new ArrayList<>(numOfElevators);
    }
}
