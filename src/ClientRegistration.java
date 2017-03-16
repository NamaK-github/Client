import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

/**
 * Created by NamaK on 14.03.17.
 */
public class ClientRegistration extends JFrame {
    private static ClientRegistration instance;
    private static ClientChat clientChat;

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
        //Регистрация
        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (login.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ведите логин.");
                } else if (password.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(null, "Введите пароль.");
                } else if (!Arrays.equals(password.getPassword(), confimPassword.getPassword())) {
                    JOptionPane.showMessageDialog(null, "Пароли не совпадают.");
                } else if (nick.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Введите ник.");
                } else {
                    clientChat.registration(login.getText(), password.getPassword().toString(), nick.getText());
                }
            }
        });
        //Закрытие
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                instance = null;
            }
        });
        setVisible(true);
    }

    public static ClientRegistration getInstance(ClientChat cc) {
        clientChat = cc;
        if (instance == null) {
            instance = new ClientRegistration();
        }
        return instance;
    }
}
