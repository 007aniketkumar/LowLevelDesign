package ani.elevator.entity;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Properties of an individual Elevator.
 * <p>
 * has id , state @see ElevatorState ,
 * queue-up , queue-down
 * currentfloor
 * <p>
 *    todo: capacity
 */

public class Elevator {

    private int id;

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

    private ElevatorState elevatorState;

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    private int currentFloor;
    private PriorityBlockingQueue<Integer> upQueue;
    private PriorityBlockingQueue<Integer> downQueue;


    public Elevator(int id, ElevatorState elevatorState, int currentFloor,
                    PriorityBlockingQueue upQueue,
                    PriorityBlockingQueue downQueue, int floors) {
        this.id = id;
        this.elevatorState = elevatorState;
        this.currentFloor = currentFloor;
        this.upQueue = new PriorityBlockingQueue<>(); //ascending priority queue
        this.downQueue = new PriorityBlockingQueue<>(floors, Comparator.reverseOrder()); //descending - reason being if someone has pressed 7 , 8

    }


    public int getId() {
        return id;
    }

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public PriorityBlockingQueue getUpQueue() {
        return upQueue;
    }

    public PriorityBlockingQueue getDownQueue() {
        return downQueue;
    }
}
