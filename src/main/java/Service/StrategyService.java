package Service;

import Strategy.AuthStrategy;
import model.Users;

public class StrategyService {

    AuthStrategy strategy;
    public StrategyService(AuthStrategy authStrategy) {
        this.strategy = authStrategy;
    }

    public boolean authenticate(Users user) {
        return strategy.authenticate(user);
    }
}
