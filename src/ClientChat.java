import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by NamaK on 15.02.17.
 */
public class ClientChat extends JFrame {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private final String serverHost;
    private final int serverPort;
    private JPanel authPanel;
    private JPanel messagePanel;
    private JTextField loginField;
    private JTextArea chatHistoryArea;
    private Thread thread;

    public ClientChat(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        initConnection();
        initGUI();
        initServerListener();
    }

    private void initConnection() {
        try {
            socket = new Socket(serverHost, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initGUI() {
        setTitle("Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(700, 200, 300, 400);
        //Текст чата
        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
        add(scrollPane, BorderLayout.CENTER);
        //Панель аутентификации
        authPanel = new JPanel(new GridLayout(2, 3));
        loginField = new JTextField();
        loginField.setToolTipText("Введите логин");
        authPanel.add(new JLabel("  Логин:"));
        authPanel.add(loginField);
        JButton signInButton = new JButton("Вход");
        authPanel.add(signInButton);
        JTextField passField = new JTextField();
        loginField.setToolTipText("Введите пароль");
        authPanel.add(new JLabel("  Пароль:"));
        authPanel.add(passField);
        add(authPanel, BorderLayout.NORTH);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendAuth(loginField.getText(), passField.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        //Панель отправки сообщений
        messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        JTextField messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(messageField);
            }
        });
        messagePanel.add(messageField, BorderLayout.CENTER);
        JButton sendButton = new JButton();
        sendButton.setText("Отправить");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(messageField);
                messageField.grabFocus();
            }
        });
        messagePanel.add(sendButton, BorderLayout.EAST);
        add(messagePanel, BorderLayout.SOUTH);
        messagePanel.setVisible(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.writeUTF("end___session___client");
                    out.flush();
                    out.close();
                    in.close();
                    socket.close();
                } catch (IOException e1) {
                }
            }
        });
        setVisible(true);
    }

    private void sendAuth(String login, String password) throws IOException {
        out.writeUTF("auth___" + login + "___" + password);
        out.flush();
    }

    private void sendMessage(JTextField jTextField) {
        if (!jTextField.getText().trim().isEmpty()) {
            try {
                out.writeUTF(jTextField.getText());
                out.flush();
                jTextField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initServerListener() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = in.readUTF();
                        if (message.equalsIgnoreCase("end___session___server")) {
                            break;
                        } else if (message.equalsIgnoreCase("signin___successfull")) {
                            JOptionPane.showMessageDialog(null, "Вы вошли в чат.");
                            authPanel.setVisible(false);
                            messagePanel.setVisible(true);
                            setTitle(loginField.getText());
                        } else if (message.equalsIgnoreCase("signin___fail")) {
                            JOptionPane.showMessageDialog(null, "Ошибка авторизации. Попробуйте ещё раз.");
                        } else chatHistoryArea.append(message + "\n");
                    }
                } catch (IOException e) {
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
