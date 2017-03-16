import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by NamaK on 14.03.17.
 */
public class ClientRegistration extends JFrame {
    private static ClientRegistration instance;

    private ClientRegistration() {
        initGUI();
    }

    private void initGUI() {
        setTitle("Регистрация");
        setBounds(800, 250, 250, 150);
        setResizable(false);
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new Label("Логин:"));
        JTextField login = new JTextField();
        panel.add(login);
        panel.add(new Label("Пароль:"));
        JPasswordField password = new JPasswordField();
        panel.add(password);
        panel.add(new Label("Повторите пароль:"));
        JPasswordField confimPassword = new JPasswordField();
        panel.add(confimPassword);
        panel.add(new Label("Ник:"));
        JTextField nick = new JTextField();
        panel.add(nick);
        add(panel, BorderLayout.CENTER);
        JButton regButton = new JButton("Зарегистрироваться");
        add(regButton, BorderLayout.SOUTH);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                instance = null;
            }
        });
    }

    public static ClientRegistration getInstance() {
        if (instance == null) {
            instance = new ClientRegistration();
        }
        return instance;
    }
}
