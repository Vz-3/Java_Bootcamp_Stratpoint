package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Setup & Variables
            boolean exitApp = false;
            boolean firstRun = false;

            int ctr = 0;

            Scanner scn = new Scanner(System.in);
            Calculator calculator = null;


            System.out.println("""
                Calculator Configurations: \

                [1] - Change app name.\

                [2] - Change app name & close keyword.\
                
                [3] - Change algorithm.\
                
                [4] - Change algorithm & app name\
                
                [5] - Change algorithm, app name, & close keyword\
                
                [quit] - Exit application.\
                """);
            String calculatorConfig = scn.nextLine();
            String newAppName = null;
            String newCloseKeyword = null;
            switch (calculatorConfig) {
                case "1":
                    System.out.println("Enter app name: ");
                    newAppName = scn.nextLine();
                    calculator = new Calculator(newAppName);
                    break;
                case "2":
                    System.out.println("Enter app name: ");
                    newAppName = scn.nextLine();
                    do {
                        System.out.println("Enter close keyword: ");
                        newCloseKeyword = scn.nextLine();
                    } while (newCloseKeyword.length()<4 || newCloseKeyword.matches("-?\\d+(\\.\\d+)?"));
                    calculator = new Calculator(newAppName, newCloseKeyword);
                    break;
                case "3":
                    calculator = new CalculatorV2();
                    break;
                case "4":
                    System.out.println("Enter app name: ");
                    newAppName = scn.nextLine();
                    calculator = new CalculatorV2(newAppName);
                    break;
                case "5":
                    System.out.println("Enter app name: ");
                    newAppName = scn.nextLine();
                    do {
                        System.out.println("Enter close keyword: ");
                        newCloseKeyword = scn.nextLine();
                    } while (newCloseKeyword.length()<4 || newCloseKeyword.matches("-?\\d+(\\.\\d+)?"));
                    calculator = new CalculatorV2(newAppName, newCloseKeyword);
                    break;
                case "quit":
                    exitApp = true;
                    break;
                default:
                    calculator = new Calculator();
                    break;
            }

            // Code
            while (!exitApp || ctr > 10000) {
                // Welcome Text Here
                if (!firstRun) {
                    System.out.println("Welcome to "+calculator.getAppName());
                    System.out.println("To exit, use the keyword '"+calculator.getCloseKeyword()+"'.");
                    firstRun = true;
                }

                System.out.print("\n>");
                String userInput = scn.nextLine();
                if (userInput.toLowerCase().equals(calculator.getCloseKeyword())) {
                    exitApp = true;
                }

                // Clear whitespaces and Ignore case
                userInput = userInput.replace(" ", "");

                // Logic
                calculator.setExpression(userInput);
                calculator.calculate();
                System.out.println("= "+calculator.getAnswer());
                // Debugging System.out.println("Expression:"+userInput);

                ctr++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Programmed by: Vz-3 | 06/21/24");
        }
    }
}