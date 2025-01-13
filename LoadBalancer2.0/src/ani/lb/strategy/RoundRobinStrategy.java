package ani.lb.strategy;

import ani.lb.entity.Server;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RoundRobinStrategy implements LoadBalancingStrategy{

    Queue<Server> serverQueue = new ConcurrentLinkedQueue<>();
    private final int MAX_HEALTH_CHECK_COUNT = 10; // load it from application.properities.
    //initialise the queue - we can move it later to a different class

    public void initialise(List<Server> servers){
        serverQueue.addAll(servers);
    }

    @Override
    public Server selectServer(List<Server> servers) {
        if(servers.isEmpty()){
            System.out.println(" No active servers are present");
            return null; // handle it in exception class .
        }
        // check for all the servers
        int attempts = serverQueue.size();

        while(attempts-- >0){
            Server server = serverQueue.poll();
            if(server.isHealthy()){
                serverQueue.add(server);// add it back to the queue
                return server;
            }
            //else add it back - if the server is currently unhealthy but  healthCheck count is <10
            if(server.getHealthCheckCount().get()<MAX_HEALTH_CHECK_COUNT){
                System.out.println("Server returned back to the pool:" + server.getServerName());
                serverQueue.add(server);
            }
        }
        //if we could not get any server
        System.out.println("No servers are available");
        return null; // handle in the processor

    }
}
