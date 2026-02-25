package Service;

import Strategy.AuthStrategy;
import model.Users;

public class StrategyService {

    private AuthStrategy strategy;

    public StrategyService() {}

    public boolean authenticate(Users user) {
        return strategy.authenticate(user);
    }

    public void setStrategy(AuthStrategy strategy) {
        this.strategy = strategy;
    }
}
