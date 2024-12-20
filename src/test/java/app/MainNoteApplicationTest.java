package app;

import data_access.InMemoryUserDAO;
import data_access.grade_api.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

class MainNoteApplicationTest {

    private JFrame app;

    @BeforeEach
    public void setUp() {

        // create the data access and inject it into our builder!
        final UserRepository userDAO = new InMemoryUserDAO();

        final AppBuilder builder = new AppBuilder(true);
        app = builder.addUserDAO(userDAO)
                .addNoteView()
                .addNoteUseCase().build();

        app.setVisible(true);

    }

    /**
     * This is an example of an end-to-end test with a mocked database.
     * <p>The code creates the application and directly tests to see that the GUI
     * is updated as expected when the buttons and UI elements are interacted with.
     * </p>
     * You can run the test to visually see what happens.
     */
    @Test
    void testEndToEnd() {

        Component[] components =  ((JPanel)app.getRootPane().getContentPane().getComponents()[0]).getComponents();
        JTextArea textArea = null;
        for (Component component : components) {
            if (component instanceof JTextArea) {
                textArea = (JTextArea) component;
                Assertions.assertEquals("test", textArea.getText());

            }
        }

        textArea.setText("test test");


        JButton save = null;
        JButton load = null;
        for (Component component : components) {
            if (component instanceof JPanel) {
                for (Component c : ((JPanel) component).getComponents()) {
                    if (c instanceof JButton) {
                        if (save != null) {
                            load = (JButton) c;
                        }
                        else {
                            save = (JButton) c;
                        }
                    }
                }
            }
        }

        save.doClick();
        Assertions.assertEquals("test test", textArea.getText());
        textArea.setText("");

        System.out.println("cleared text; about to refresh...");
        // pause execution for a bit so we can visually see the changes on the screen
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        load.doClick();
        Assertions.assertEquals("test test", textArea.getText());

        System.out.println("after refresh!");

        // pause execution for a bit so we can visually see the changes on the screen
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}