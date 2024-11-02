package com.Conversor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Scan variables and load currencies lists
        try (Scanner scanner = new Scanner(System.in)) {
            String[] currencies = Conversor.getCurrencies();
            List<String> currenciesFromFile = loadCurrenciesFromFile();

            // Display menu for source currency
            System.out.println("Select the currency to convert from:");
            displayCurrencyMenu(currencies);
            int fromChoice = getValidChoice(scanner, currencies.length);
            String fromCurrency = currencies[fromChoice - 1];
            fromCurrency = checkValidCurrenciesFromFile(scanner, currenciesFromFile, fromCurrency, fromChoice);

            // Display menu for target currency
            System.out.println("\nSelect the currency to convert to:");
            displayCurrencyMenu(currencies);
            int toChoice = getValidChoice(scanner, currencies.length);
            String toCurrency = currencies[toChoice - 1];
            toCurrency = checkValidCurrenciesFromFile(scanner, currenciesFromFile, toCurrency, toChoice);

            // Get amount to convert
            System.out.println("\nEnter the amount to convert:");
            double amount = getValidAmount(scanner);

            // Perform conversion
            double convertedAmount = Conversor.convertCurrency(fromCurrency, toCurrency, amount);

            // Display result
            if (convertedAmount != -1) {
                System.out.printf("%.2f %s is equal to %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
            } else {
                System.out.println("Error performing currency conversion.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static List<String> loadCurrenciesFromFile() throws IOException {
        List<String> currencies = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("currencies"));
        String line;
        while ((line = br.readLine()) != null) {
            currencies.add(line.trim());
        }
        return currencies;
    }

    private static void displayCurrencyMenu(String[] currencies) {
        for (int i = 0; i < currencies.length; i++) {
            System.out.printf("%d. %s%n", (i + 1), currencies[i]);
        }
    }

    private static int getValidChoice(Scanner scanner, int maxChoice) {
        while (true) {
            System.out.print("Enter your choice (1-" + maxChoice + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= maxChoice) {
                    return choice;
                }
                System.out.println("Please enter a number between 1 and " + maxChoice);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    private static double getValidAmount(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {

                System.out.println("Please enter a valid number amount:");
            }
        }
    }

    public static String checkValidCurrenciesFromFile(Scanner scanner, List<String> currenciesFromFile, String Currency, int Choice) {
        if (Choice == 7) {

            // Select currency manually
            System.out.print("Enter the full name of the currency: ");
            String manualCurrency = scanner.nextLine().trim();
            boolean isValid = false;
            for (String curr : currenciesFromFile) {
                if (curr.equalsIgnoreCase(manualCurrency)) {
                    Currency = curr;
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                System.out.print("Enter valid full currency next time ");
                Currency = checkValidCurrenciesFromFile(scanner, currenciesFromFile, Currency, Choice); // Recursive call
            }
        }
        return Currency;
    }
}