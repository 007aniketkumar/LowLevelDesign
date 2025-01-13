package ani.elevator.processor;

//Has elevatorsystem DTO
//will choose the best elevator given list of elevators from elevatorSystemDTO
//start the choosen elevator

import ani.elevator.entity.Elevator;
import ani.elevator.entity.ElevatorState;
import ani.elevator.entity.ElevatorSystemDTO;
import ani.elevator.exception.ElevatorSystemException;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ElevatorSystemProcessor {

    ElevatorSystemDTO elevatorSystemDTO; // this will give the reference of the list of elevators.


    //each thread to independently manage the elevators ,
    // without blocking the main thread
    //which is the elevatorSystem here.
    ExecutorService elevatorWorkers = Executors.newFixedThreadPool(5) ;


    ReentrantLock elevatorLock = new ReentrantLock();



    //start the elevator on their separate threads.
    public void start(){
        for(Elevator elevator :elevatorSystemDTO.getElevators()){
            elevatorWorkers.submit(new ElevatorProcessor(elevator,true));
        }
    }


    public void requestElevator(int requestFloor,String direction) throws ElevatorSystemException {
        if(requestFloor<0 || requestFloor>100){
            throw new ElevatorSystemException("Invalid floor");
        }
        //find the best elevator
        Elevator bestElevator = bestElevator(requestFloor,direction);
        bestElevator.get

        }
    }

    //shutdown
    public void stop(){
        elevatorWorkers.shutdown();
    }



    /**
     * Given a list of elevators , select the best elevator
     *
     */
    private Elevator bestElevator(int requestedFloor, String direction){
        int mincost=Integer.MAX_VALUE;
        Elevator desiredElevator = null;
        for(Elevator elevator :elevatorSystemDTO.getElevators()){
             if( cost(requestedFloor,direction, elevator )<mincost){
                 desiredElevator= elevator;
             }
        }
    return desiredElevator;
    }



    /**
     *
     * Given a floor , the elevator should be able to return the cost <Integer>
     *
     * </Integer>
     *
     */

    private int cost(int desiredFloor, String desiredDirection, Elevator elevator){
        if(elevator.getElevatorState().equals(ElevatorState.IDLE)){
            return desiredFloor;
        } else if(elevator.getElevatorState().equals(desiredDirection)) {
            return handleSameDirection(desiredDirection,desiredFloor, elevator);
        } else {
            return handleOppositeDirection(desiredDirection,desiredFloor, elevator);
        }
    }


    private int handleSameDirection(String desiredDirection, int desiredFloor, Elevator elevator){
        return Math.abs(desiredFloor- elevator.getCurrentFloor());
    }



    private int handleOppositeDirection(String desiredDirection, int desiredFloor, Elevator elevator) {
        int lastFloor = 0;
        if (desiredDirection.equals(ElevatorState.UP)) {
            //implies elevator is going down , find the lowest floor
            lastFloor = (int) elevator.getDownQueue().stream().min(Comparator.naturalOrder()).get();
        } else {
            lastFloor = (int) elevator.getDownQueue().stream().max(Comparator.naturalOrder()).get();
        }
        return desiredFloor + Math.abs((elevator.getCurrentFloor()) - lastFloor);
    }


    private static int maxFloor(PriorityBlockingQueue<Integer> queue){
        return queue.stream().max(Comparator.naturalOrder()).get();
    }


}
