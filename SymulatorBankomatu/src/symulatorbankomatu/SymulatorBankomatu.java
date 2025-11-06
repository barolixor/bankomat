package symulatorbankomatu;

import javax.swing.*;
import java.awt.*;

public class SymulatorBankomatu extends JFrame {
    private Account account;
    private JLabel saldoLabel;
    private JTextArea outputArea;
    private JButton withdrawButton, depositButton, topupButton, logoutButton, blikDepositButton, blikWithdrawButton;

    public SymulatorBankomatu(Account account) {
        this.account = account;

        setTitle("Symulator Bankomatu - Konto: " + account.getCardNumber());
        setSize(530, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 249, 255));

        JLabel header = new JLabel("Witaj w Bankomacie Java", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(0, 102, 204));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        saldoLabel = new JLabel("Saldo: " + account.getBalance() + " zł", JLabel.CENTER);
        saldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saldoLabel.setForeground(new Color(34, 139, 34));
        mainPanel.add(saldoLabel, BorderLayout.BEFORE_FIRST_LINE);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        buttonPanel.setBackground(mainPanel.getBackground());

        withdrawButton = makeButton("Wypłata", new Color(255, 102, 102));
        depositButton = makeButton("Wpłata", new Color(102, 204, 102));
        topupButton = makeButton("Doładowanie", new Color(255, 178, 102));
        blikDepositButton = makeButton("Wpłata BLIK", new Color(102, 204, 255));
        blikWithdrawButton = makeButton("Wypłata BLIK", new Color(153, 102, 255));
        logoutButton = makeButton("Wyloguj", new Color(102, 153, 255));

        buttonPanel.add(withdrawButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(topupButton);
        buttonPanel.add(blikDepositButton);
        buttonPanel.add(blikWithdrawButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBorder(BorderFactory.createTitledBorder("Historia operacji"));
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        add(mainPanel);

        withdrawButton.addActionListener(e -> withdraw());
        depositButton.addActionListener(e -> deposit());
        topupButton.addActionListener(e -> topUp());
        blikDepositButton.addActionListener(e -> blikDeposit());
        blikWithdrawButton.addActionListener(e -> blikWithdraw());
        logoutButton.addActionListener(e -> logout());
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return b;
    }

    private void updateBalanceLabel() {
        saldoLabel.setText("Saldo: " + account.getBalance() + " zł");
    }

    private void withdraw() {
        String kwota = JOptionPane.showInputDialog(this, "Podaj kwotę do wypłaty:");
        if (kwota == null) return;
        try {
            double amount = Double.parseDouble(kwota);
            if (account.withdraw(amount)) outputArea.append("Wypłacono " + amount + " zł\n");
            else outputArea.append("Brak środków!\n");
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
            if (account.withdraw(amount)) outputArea.append("Doładowano numer " + phone + " kwotą " + amount + " zł\n");
            else outputArea.append("Brak środków na koncie!\n");
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            outputArea.append("Nieprawidłowa kwota.\n");
        }
    }

    private void blikDeposit() {
        String code = JOptionPane.showInputDialog(this, "Podaj kod BLIK wpłaty:");
        if (code == null) return;
        String kwota = JOptionPane.showInputDialog(this, "Kwota wpłaty:");
        if (kwota == null) return;
        try {
            double amount = Double.parseDouble(kwota);
            account.deposit(amount);
            outputArea.append("Wpłata BLIK: " + amount + " zł (kod " + code + ")\n");
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            outputArea.append("Nieprawidłowa kwota.\n");
        }
    }

    private void blikWithdraw() {
        String code = JOptionPane.showInputDialog(this, "Podaj kod BLIK wypłaty:");
        if (code == null) return;
        String kwota = JOptionPane.showInputDialog(this, "Kwota wypłaty:");
        if (kwota == null) return;
        try {
            double amount = Double.parseDouble(kwota);
            if (account.withdraw(amount)) outputArea.append("Wypłata BLIK: " + amount + " zł (kod " + code + ")\n");
            else outputArea.append("Brak środków!\n");
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
