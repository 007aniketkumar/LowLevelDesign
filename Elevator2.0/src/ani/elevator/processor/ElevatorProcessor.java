package ani.elevator.processor;

import ani.elevator.entity.Elevator;
import ani.elevator.entity.ElevatorState;

import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class ElevatorProcessor implements Runnable {

    private final Elevator elevator;
    private volatile boolean running;

    public ElevatorProcessor(Elevator elevator, boolean running) {
        this.elevator = elevator;
        this.running = true;
    }


    public void stop() {
        running = false;
    }


    @Override
    public void run() {
        while (running) {
            processRequests();
        }

    }


    /**
     * check the current direction /idle , if up or idle , process up queue , else
     * process down queue
     */
    private void processRequests() {
        //process upQueue or downQueue
        if (!elevator.getUpQueue().isEmpty() ||
                !elevator.getDownQueue().isEmpty()) {
            if (!elevator.getUpQueue().isEmpty()) {
                elevator.setElevatorState(ElevatorState.UP);
                processQueue(elevator.getUpQueue()); //process upQueue
            } else {
                elevator.setElevatorState(ElevatorState.DOWN);
                processQueue(elevator.getDownQueue()); //processDownQueu
            }
        }
        elevator.setElevatorState(ElevatorState.IDLE);
        System.out.println("The elevator:" + elevator.getId() + "is IDLE on floor :" + elevator.getCurrentFloor());
    }


    /**
     * @param queue
     */

    private void processQueue(PriorityBlockingQueue<Integer> queue) {
        while (!queue.isEmpty()) {
            int floor = queue.poll();
            moveToFloor(floor);
        }

    }


    private void moveToFloor(int floor) {
        System.out.println("Elevator: " + elevator.getId() + "is moving from"
                + elevator.getCurrentFloor() + "to floor" + floor);
        elevator.setCurrentFloor(floor);
    }




}
