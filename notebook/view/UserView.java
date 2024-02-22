package notebook.view;

import java.util.Scanner;
import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        while(true) {
            String command = this.prompt("Введите команду: ");
            Commands com = Commands.valueOf(command);
            if (com == Commands.EXIT) {
                return;
            }

            switch (com) {
                case CREATE:
                    User u = this.createUser();
                    this.userController.saveUser(u);
                    break;
                case READ:
                    String id = this.prompt("Идентификатор пользователя: ");

                    try {
                        User user = this.userController.readUser(Long.parseLong(id));
                        System.out.println(user);
                        System.out.println();
                        break;
                    } catch (Exception var7) {
                        throw new RuntimeException(var7);
                    }
                case LIST:
                    System.out.println(this.userController.readAll());
                    break;
                case DELETE:
                    String userIdToDelete = prompt("Идентификатор пользователя для удаления: ");
                    try {
                        Long userIdLong = Long.parseLong(userIdToDelete);
                        boolean deleted = userController.delete(userIdLong);
                        if (deleted) {
                            System.out.println("Пользователь успешно удален.");
                        } else {
                            System.out.println("Пользователь с указанным идентификатором не найден.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный формат идентификатора пользователя.");
                    }
                    break;
                case UPDATE:
                    String userId = this.prompt("Enter user id: ");
                    this.userController.updateUser(userId, this.createUser());
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }

    private User createUser() {
        String firstName = this.prompt("Имя: ");
        String lastName = this.prompt("Фамилия: ");
        String phone = this.prompt("Номер телефона: ");
        return new User(firstName, lastName, phone);
    }
}