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
        setTitle("Logowanie do Bankomatu");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));

        accounts.add(new Account("1111", "1234", 1500.0));
        accounts.add(new Account("2222", "0000", 250.0));
        accounts.add(new Account("3333", "4321", 5430.0));

        add(new JLabel("Numer karty:"));
        cardField = new JTextField();
        add(cardField);

        add(new JLabel("PIN:"));
        pinField = new JPasswordField();
        add(pinField);

        add(new JLabel(""));
        loginButton = new JButton("Zaloguj");
        add(loginButton);

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
