package symulatorbankomatu;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Login extends JFrame {
    private JTextField cardField;
    private JPasswordField pinField;
    private JButton loginButton;
    private java.util.List<Account> accounts = new ArrayList<>();

    public Login() {
        setTitle("Logowanie - Bankomat Java");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("BANKOMAT JAVA", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        form.setBackground(panel.getBackground());

        form.add(new JLabel("Numer karty:", JLabel.RIGHT));
        cardField = new JTextField();
        form.add(cardField);

        form.add(new JLabel("PIN:", JLabel.RIGHT));
        pinField = new JPasswordField();
        form.add(pinField);

        form.add(new JLabel(""));
        loginButton = new JButton("Zaloguj");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        form.add(loginButton);

        panel.add(form, BorderLayout.CENTER);
        add(panel);

        accounts.add(new Account("1111", "1234", 1500.0));
        accounts.add(new Account("2222", "0000", 250.0));
        accounts.add(new Account("3333", "4321", 5430.0));

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String card = cardField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        for (Account acc : accounts) {
            if (acc.getCardNumber().equals(card) && acc.checkPin(pin)) {
                JOptionPane.showMessageDialog(this, "Zalogowano pomyślnie!");
                new SymulatorBankomatu(acc).setVisible(true);
                dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Błędny numer karty lub PIN!", "Błąd", JOptionPane.ERROR_MESSAGE);
    }
}
