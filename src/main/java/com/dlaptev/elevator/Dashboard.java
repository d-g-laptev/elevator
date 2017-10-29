package com.dlaptev.elevator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Dashboard implements Runnable {
    private final Elevator elevator;

    Dashboard(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void run() {
        while (true) {
            String str;
            try {
                str = readStringFromCmd();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
                return;
            }

            processInput(str);

            synchronized (elevator) {
                elevator.notifyAll();
            }
        }
    }

    private void processInput(String str) {
        String[] parts = str.split("-");

        try {
            switch (parts.length) {
                case 1:
                    Integer to = Integer.parseInt(parts[0]);
                    elevator.callFloor(to);
                    return;
                case 2:
                    Integer from = Integer.parseInt(parts[0]);
                    to = Integer.parseInt(parts[1]);
                    elevator.callFloor(to, from);
                    return;
                default:
                    System.err.println("Wrong format!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong format!");
        }
    }

    private static String readStringFromCmd() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.err.println("Call elevator, format:");
        System.err.println("1. Floor from-to (11-1, 12-20, etc...)");
        System.err.println("2. Inside elevator: floor to (5, 10, etc...)");
        return br.readLine();
    }
}