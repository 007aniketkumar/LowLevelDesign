package ani.lb.strategy;

import ani.lb.entity.Server;

import java.util.List;

public interface LoadBalancingStrategy {

    Server selectServer(List<Server> servers);

    public void initialise(List<Server> servers);

}
