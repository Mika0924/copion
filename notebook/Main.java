package notebook;

import notebook.controller.UserController;
import notebook.model.repository.GBRepository;
import notebook.model.repository.impl.UserRepository;
import notebook.util.DBConnector;
import notebook.view.UserView;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        DBConnector.createDB();
        UserRepository fileOperation = new UserRepository("db.txt");
        GBRepository repository = new UserRepository(fileOperation, "db.txt");
        UserController controller = new UserController(repository);
        UserView view = new UserView(controller);
        view.run();
    }
}