package ani.lb.entity;

import ani.lb.strategy.LoadBalancingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * load balancer will list of servers  @todo handle the creation in main loading from prop file,/map
 * strategy
 * different threads run
 * take care
 */
public class LoadBalancer {
    private List<Server> severs = new ArrayList<>();
    private LoadBalancingStrategy loadbalacingStrategy; //@impleent

    //assing each server task to a thread.
    private ExecutorService requestExectuor;

    private static LoadBalancer loadBalancer;

    //so that the main thread is not blocked
    private ExecutorService healthMontior;


    private int healthcheck_interval_in_seconds = 5;
    private boolean isMonitoring;

    private LoadBalancer(LoadBalancingStrategy loadBalancingStrategy, int threads, boolean isMonitoring) {
        this.loadbalacingStrategy = loadBalancingStrategy;
        requestExectuor = Executors.newFixedThreadPool(threads);
        healthMontior = Executors.newFixedThreadPool(1); // to start with can be made configurable
        isMonitoring = true; // default
    }

    //singleton lb
    public static synchronized LoadBalancer getInstance(LoadBalancingStrategy loadBalancingStrategy,
                                                        int threads, boolean isMonitoring) {
        if (loadBalancer == null) {
            loadBalancer = new LoadBalancer(loadBalancingStrategy, threads, isMonitoring);
        }
        return loadBalancer;
    }

    //add servers - pass this from map
    public void addServers(Server server ) {
        severs.add(server);
    }

    public void handleRequest() {
        requestExectuor.submit(() -> {
            Server server = loadbalacingStrategy.selectServer(severs);
            if (server != null && server.handleRequest()) {
                System.out.println("Request successfully handled by server :-> " + server.getServerName());
            } else {
                System.out.println("All the healthly servers are occupied , Request failed as no server is avaialble");
            }
        });
    }

    public void initialiseSystem(LoadBalancingStrategy loadBalancing){
        loadBalancing.initialise(severs);
    }

    //health monoriting

    public void startHealthMonitoring() {
        healthMontior.submit(() -> {
            while (isMonitoring) {
                for (Server server : severs) {
                    // Simulate health check logic (e.g., ping server, HTTP GET, etc.)

                    boolean healthStatus = server.isHealthy();//// Placeholder for actual health check
                    server.setHealthy(healthStatus);
                    server.resetHealthCheckCount();
                    if (!healthStatus ) {
                        server.incrementHealthCheckCount(); // this will be used to count against the value of 10
                        System.out.println("Server : " + server.getServerName() + "is not healthy");
                    }
                }
                try {
                    Thread.sleep(healthcheck_interval_in_seconds);
                } catch (InterruptedException e) {
                    System.out.println("The health check has failed");
                }
            }
        });

        //shutdown the executors

    }


    public void cleanUp() {
        requestExectuor.shutdown();
        healthMontior.shutdown();
    }
}
