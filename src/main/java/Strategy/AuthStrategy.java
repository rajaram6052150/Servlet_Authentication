package Strategy;

import model.Users;

public interface AuthStrategy {
    public boolean authenticate (Users user);
}
