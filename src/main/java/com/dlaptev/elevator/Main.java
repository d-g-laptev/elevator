package com.dlaptev.elevator;

import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        CommandLine cmd = parseParams(args);
        if (cmd == null) {
            System.err.println("App start error: can't get params from command line");
            return;
        }

        float speed = Float.parseFloat(cmd.getOptionValue("speed"));
        int openDoorsInterval = Integer.parseInt(cmd.getOptionValue("open_doors_interval"));
        int maxFloor = Integer.parseInt(cmd.getOptionValue("max_floor"));
        float storeyHeight = Float.parseFloat(cmd.getOptionValue("storey_height"));

        Elevator elevator;
        try {
            elevator = new Elevator(speed, openDoorsInterval, maxFloor, storeyHeight);
        } catch (Exception e) {
            System.err.print("Initialization error: " + e.getMessage());
            return;
        }
        Thread t = new Thread(elevator);
        t.start();

        Thread t2 = new Thread(new Dashboard(elevator));
        t2.start();
    }

    private static CommandLine parseParams(String[] args) {
        Options options = new Options();

        Option speed = new Option("s", "speed", true, "elevator's speed (m/s)");
        speed.setRequired(true);
        options.addOption(speed);

        Option openDoorsInterval = new Option("i", "open_doors_interval", true, "open doors interval in milliseconds");
        openDoorsInterval.setRequired(true);
        options.addOption(openDoorsInterval);

        Option maxFloor = new Option("m", "max_floor", true, "elevator's max floor");
        maxFloor.setRequired(true);
        options.addOption(maxFloor);

        Option storeyHeight = new Option("h", "storey_height", true, "storey height in meters");
        storeyHeight.setRequired(true);
        options.addOption(storeyHeight);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("elevator", options);

            System.exit(1);
            return null;
        }

        return cmd;
    }
}
