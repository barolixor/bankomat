import java.util.*;

public class Bankomat {

    public static void main(String[] args) {
        Bankomat atm = new Bankomat();
        atm.start();
    }
}

// --- Klasa konta użytkownika ---
class Account {
    private String cardNumber;
    private String pin;
    private double balance;

    public Account(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public boolean checkPin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

// --- Klasa główna bankomatu ---
class bankomat {
    private Scanner scanner = new Scanner(System.in);
    private List<Account> accounts = new ArrayList<>();
    private TreeMap<Integer, Integer> cashInventory = new TreeMap<>(Collections.reverseOrder());

    public bankomat() {
        // przykładowe konta
        accounts.add(new Account("1111-2222-3333-4444", "1234", 1500.0));
        accounts.add(new Account("9999-8888-7777-6666", "0000", 250.0));
        accounts.add(new Account("4444-3333-2222-1111", "4321", 5430.0));

        // zapas banknotów
        cashInventory.put(200, 10);
        cashInventory.put(100, 10);
        cashInventory.put(50, 20);
        cashInventory.put(20, 30);
        cashInventory.put(10, 30);
    }

    public void start() {
        System.out.println("--- Symulator Bankomatu ---");

        while (true) {
            System.out.println("\nWłóż kartę (numer karty) lub wpisz 'exit' aby zakończyć:");
            String card = scanner.nextLine().trim();

            if (card.equalsIgnoreCase("exit")) {
                System.out.println("Do widzenia!");
                break;
            }

            Account acc = findAccount(card);
            if (acc == null) {
                System.out.println("Nie rozpoznano karty.");
                continue;
            }

            System.out.print("Podaj PIN: ");
            String pin = scanner.nextLine().trim();

            if (!acc.checkPin(pin)) {
                System.out.println("Błędny PIN.");
                continue;
            }

            // zalogowany użytkownik
            loggedMenu(acc);
        }
    }

    private Account findAccount(String card) {
        for (Account a : accounts) {
            if (a.getCardNumber().equals(card)) return a;
        }
        return null;
    }

    private void loggedMenu(Account acc) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n1 - Sprawdź saldo");
            System.out.println("2 - Wypłata");
            System.out.println("3 - Wpłata");
            System.out.println("4 - Doładuj telefon");
            System.out.println("5 - Wyloguj");
            System.out.print("Wybór: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.printf("Saldo: %.2f zł%n", acc.getBalance());
                    break;
                case "2":
                    withdraw(acc);
                    break;
                case "3":
                    deposit(acc);
                    break;
                case "4":
                    topUp(acc);
                    break;
                case "5":
                    logout = true;
                    System.out.println("Wylogowano.");
                    break;
                default:
                    System.out.println("Nieprawidłowy wybór.");
            }
        }
    }

    private void withdraw(Account acc) {
        System.out.print("Podaj kwotę do wypłaty: ");
        try {
            int amount = Integer.parseInt(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Kwota musi być większa od 0.");
                return;
            }
            if (amount > acc.getBalance()) {
                System.out.println("Brak środków na koncie.");
                return;
            }

            Map<Integer, Integer> toDispense = computeCash(amount);
            if (toDispense == null) {
                System.out.println("Nie można wydać tej kwoty przy dostępnych nominałach.");
                return;
            }

            System.out.println("Bankomat wyda:");
            for (Map.Entry<Integer, Integer> e : toDispense.entrySet()) {
                System.out.println(e.getKey() + " zł x " + e.getValue());
                cashInventory.put(e.getKey(), cashInventory.get(e.getKey()) - e.getValue());
            }

            acc.withdraw(amount);
            System.out.printf("Wypłacono %d zł. Nowe saldo: %.2f zł%n", amount, acc.getBalance());

        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowy format kwoty.");
        }
    }

    private void deposit(Account acc) {
        System.out.print("Podaj kwotę wpłaty: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Kwota musi być większa od 0.");
                return;
            }
            acc.deposit(amount);
            System.out.printf("Wpłacono %.2f zł. Nowe saldo: %.2f zł%n", amount, acc.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowy format kwoty.");
        }
    }

    private void topUp(Account acc) {
        System.out.print("Podaj numer telefonu: ");
        String phone = scanner.nextLine();
        System.out.print("Podaj kwotę doładowania: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Kwota musi być większa od 0.");
                return;
            }
            if (amount > acc.getBalance()) {
                System.out.println("Brak wystarczających środków.");
                return;
            }
            acc.withdraw(amount);
            System.out.printf("Doładowano numer %s kwotą %.2f zł. Nowe saldo: %.2f zł%n",
                    phone, amount, acc.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowy format kwoty.");
        }
    }

    private Map<Integer, Integer> computeCash(int amount) {
        Map<Integer, Integer> result = new LinkedHashMap<>();
        int remaining = amount;

        for (Map.Entry<Integer, Integer> e : cashInventory.entrySet()) {
            int denom = e.getKey();
            int count = Math.min(remaining / denom, e.getValue());
            if (count > 0) {
                result.put(denom, count);
                remaining -= denom * count;
            }
        }

        if (remaining == 0) return result;
        return null;
    }
}
