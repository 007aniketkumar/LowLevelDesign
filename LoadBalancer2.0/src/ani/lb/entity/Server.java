package ani.lb.entity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * {
 * "backend_servers" :["host1.com","host2.com", "host3.com"],
 * "service_timeout_in_ms": 300,
 * "healthcheck_interval_in_seconds": 5,
 * “healthcheck_url”: “/”
 * “healthcheck_port”: 8080
 * "failure_threshold_times": 10
 * }
 *
 *
 *
 */
public class Server {

    private final AtomicInteger activeConnections = new AtomicInteger(0);

    public String getServerName() {
        return serverName;
    }

    private String serverName  ; // severName or id
    private  int maxConnections = 0; // maximum number of connections a server can handle , essentially maximum requests at givne time
    public void setServer(String serverName) {
        this.serverName = serverName;
    }

    public AtomicInteger getHealthCheckCount() {
        return healthCheckCount;
    }

    public void setHealthCheckCount(AtomicInteger healthCheckCount) {
        this.healthCheckCount = healthCheckCount;
    }

    public  AtomicInteger healthCheckCount = new AtomicInteger(0);

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void incrementHealthCheckCount() {
        healthCheckCount.incrementAndGet();
    }

    public void resetHealthCheckCount() {
        healthCheckCount.set(0);
    }

    private boolean healthy;

    public Server(String serverName, boolean healthy, int maxConnections) {
        this.serverName = serverName;
        this.healthy = healthy;
        this.maxConnections = maxConnections;
    }


    // server can hande requests
    public synchronized boolean handleRequest(){

        if(!isHealthy() || activeConnections.get() == maxConnections){
            System.out.println("Server cannot handle the request");
            return false; // request rejected by the current server
        }

        activeConnections.incrementAndGet();
        try{
            System.out.println("Server-->" + serverName + " is handling the request");
            //simluating processing
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            activeConnections.decrementAndGet(); // return back the connection to be used later
        }

        return true;
    }

}
