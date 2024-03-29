package notebook.util;

import notebook.model.User;

public class UserValidator {
    public UserValidator() {
    }

    public User validate(User user) {
        if (!this.isValid(user)) {
            throw new IllegalArgumentException("First name cannot be empty");
        } else {
            user.setFirstName(user.getFirstName().replaceAll(" ", "").trim());
            user.setFirstName(user.getLastName().replaceAll(" ", "").trim());
            user.setFirstName(user.getPhone().replaceAll(" ", "").trim());
            return user;
        }
    }

    private boolean isValid(User user) {
        return !user.getFirstName().isEmpty() || !user.getLastName().isEmpty() || !user.getPhone().isEmpty();
    }
}