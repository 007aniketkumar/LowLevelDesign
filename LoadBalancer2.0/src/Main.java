import ani.lb.entity.LoadBalancer;
import ani.lb.entity.Server;
import ani.lb.strategy.LoadBalancingStrategy;
import ani.lb.strategy.RoundRobinStrategy;


//backend_servers" :["host1.com","host2.com", "host3.com"],
public class Main {
    public static void main(String[] args) {


        LoadBalancingStrategy strategy = new RoundRobinStrategy();
        LoadBalancer loadBalancer = LoadBalancer.getInstance(strategy, 3, true);

        loadBalancer.addServers(new Server("host1.com", true,5));
        loadBalancer.addServers(new Server("host2.com", true,3));
        loadBalancer.addServers(new Server("host3.com", true,2)) ;

        loadBalancer.startHealthMonitoring();
        loadBalancer.initialiseSystem( strategy);

       // simluate the working
                for(int i=0;i<10;i++){
                    loadBalancer.handleRequest();
                } try{
                    Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        loadBalancer.cleanUp();


    }
}