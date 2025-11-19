import chess.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Services and Utils
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("♕ 240 Chess Client ♕");

        System.out.println("Enter Your Name: ");
        System.out.print(">>> ");

        String name = inputScanner.next();

        System.out.println("Welcome " + name);
    }
}