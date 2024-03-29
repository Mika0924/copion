package notebook.model.repository;

import java.util.List;
import java.util.Optional;
import notebook.model.User;

public interface GBRepository {
    List<User> findAll();

    User create(User var1);

    Optional<User> findById(Long var1);

    Optional<User> update(Long var1, User var2);

    boolean delete(Long var1);

    List<String> readAll();

    void saveAll(List<String> var1);

    void write(List<User> users);
}