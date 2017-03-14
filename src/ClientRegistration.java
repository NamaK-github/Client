import javax.swing.*;
import java.awt.*;

/**
 * Created by NamaK on 14.03.17.
 */
public class ClientRegistration extends JFrame {

    public ClientRegistration() {
        initGUI();
    }

    private void initGUI() {
        setTitle("Регистрация");
        setBounds(800, 250, 200, 150);
        JButton regButton = new JButton("Зарегистрироваться");
        add(regButton,BorderLayout.SOUTH);
        setVisible(true);
    }

}
