import chess.*;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Services and Utils
        boolean isRunning = true;
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("♕ 240 Chess Client ♕");

        while (isRunning) {
            System.out.print(">>> ");

            String input = inputScanner.next();

            if (Objects.equals(input, "quit")) {
                isRunning = false;
                continue;
            }

            System.out.println(input);
        }
    }
}