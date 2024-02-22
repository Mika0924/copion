package notebook.model.repository.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import notebook.model.User;
import notebook.model.repository.GBRepository;
import notebook.util.UserValidator;
import notebook.util.mapper.impl.UserMapper;

public class UserRepository implements GBRepository {
    private UserMapper mapper;
    private UserRepository operation;
    private String fileName;

    public UserRepository(UserRepository operation, String fileName) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    public UserRepository(String fileName) {
        this.fileName = fileName;

        try {
            FileWriter writer = new FileWriter(fileName, true);

            try {
                writer.flush();
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            writer.close();
        } catch (IOException var7) {
            System.out.println(var7.getMessage());
        }

    }

    public List<String> readAll() {
        List<String> lines = new ArrayList();

        try {
            File file = new File(this.fileName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }

            while(line != null) {
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }

            fr.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return lines;
    }

    public void saveAll(List<String> data) {
        try {
            FileWriter writer = new FileWriter(this.fileName, false);

            try {
                Iterator var3 = data.iterator();

                while(true) {
                    if (!var3.hasNext()) {
                        writer.flush();
                        break;
                    }

                    String line = (String)var3.next();
                    writer.write(line);
                    writer.append('\n');
                }
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            writer.close();
        } catch (IOException var7) {
            System.out.println(var7.getMessage());
        }

    }

    public List<User> findAll() {
        List<String> lines = this.operation.readAll();
        List<User> users = new ArrayList();
        Iterator var3 = lines.iterator();

        while(var3.hasNext()) {
            String line = (String)var3.next();
            users.add(this.mapper.toOutput(line));
        }

        return users;
    }

    public User create(User user) {
        UserValidator validator = new UserValidator();
        user = validator.validate(user);
        List<User> users = this.findAll();
        long max = 0L;
        Iterator var6 = users.iterator();

        while(var6.hasNext()) {
            User u = (User)var6.next();
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }

        long next = max + 1L;
        user.setId(next);
        users.add(user);
        this.write(users);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    public Optional<User> update(Long userId, User update) {
        List<User> users = this.findAll();
        User editUser = (User)users.stream().filter((u) -> {
            return u.getId().equals(userId);
        }).findFirst().orElseThrow(() -> {
            return new RuntimeException("User not found");
        });
        if (update.getFirstName().isEmpty()) {
            editUser.setFirstName(update.getFirstName());
        }

        if (update.getLastName().isEmpty()) {
            editUser.setLastName(update.getLastName());
        }

        if (update.getPhone().isEmpty()) {
            editUser.setPhone(update.getPhone());
        }

        this.write(users);
        return Optional.of(update);
    }

    public boolean delete(Long id) {
        List<User> users = this.findAll();
        User deleted = (User)this.findById(id).orElseThrow(() -> {
            return new RuntimeException("User not found");
        });
        users.remove(deleted);
        System.out.println(deleted);
        System.out.println(users.size());
        this.write(users);
        return false;
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList();
        Iterator var3 = users.iterator();

        while(var3.hasNext()) {
            User u = (User)var3.next();
            lines.add(this.mapper.toInput(u));
        }

        this.operation.saveAll(lines);
    }
}
