package symulatorbankomatu;

import javax.swing.*;
import java.awt.*;

public class SymulatorBankomatu extends JFrame {
    private Account account;
    private JLabel saldoLabel;
    private JTextArea outputArea;
    private JButton withdrawButton, depositButton, topupButton, logoutButton;

    public SymulatorBankomatu(Account account) {
        this.account = account;

        setTitle("Symulator Bankomatu - konto: " + account.getCardNumber());
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout());
        saldoLabel = new JLabel("Saldo: " + account.getBalance() + " zł");
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(saldoLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        withdrawButton = new JButton("Wypłata");
        depositButton = new JButton("Wpłata");
        topupButton = new JButton("Doładowanie");
        logoutButton = new JButton("Wyloguj");
        buttonPanel.add(withdrawButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(topupButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        withdrawButton.addActionListener(e -> withdraw());
        depositButton.addActionListener(e -> deposit());
        topupButton.addActionListener(e -> topUp());
        logoutButton.addActionListener(e -> logout());
    }

    private void updateBalanceLabel() {
        saldoLabel.setText("Saldo: " + account.getBalance() + " zł");
    }

    private void withdraw() {
        String kwota = JOptionPane.showInputDialog(this, "Podaj kwotę do wypłaty:");
        if (kwota == null) return;
        try {
            double amount = Double.parseDouble(kwota);
            if (account.withdraw(amount)) {
                outputArea.append("Wypłacono " + amount + " zł\n");
            } else {
                outputArea.append("Brak środków!\n");
            }
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            outputArea.append("Nieprawidłowa kwota.\n");
        }
    }

    private void deposit() {
        String kwota = JOptionPane.showInputDialog(this, "Podaj kwotę do wpłaty:");
        if (kwota == null) return;
        try {
            double amount = Double.parseDouble(kwota);
            account.deposit(amount);
            outputArea.append("Wpłacono " + amount + " zł\n");
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            outputArea.append("Nieprawidłowa kwota.\n");
        }
    }

    private void topUp() {
        String phone = JOptionPane.showInputDialog(this, "Podaj numer telefonu:");
        if (phone == null) return;
        String kwota = JOptionPane.showInputDialog(this, "Podaj kwotę doładowania:");
        if (kwota == null) return;
        try {
            double amount = Double.parseDouble(kwota);
            if (account.withdraw(amount)) {
                outputArea.append("Doładowano numer " + phone + " kwotą " + amount + " zł\n");
            } else {
                outputArea.append("Brak środków na koncie!\n");
            }
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            outputArea.append("Nieprawidłowa kwota.\n");
        }
    }

    private void logout() {
        dispose();
        new Login().setVisible(true);
    }
}
