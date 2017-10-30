package com.dlaptev.elevator;

abstract class AbstractElevator implements Runnable {

    int currentFloor = 1;
    Direction direction = Direction.UP;

    final int maxFloor;

    private final long openDoorInterval; // milliseconds
    private final long intervalBetweenFloors; // milliseconds


    AbstractElevator(float speed, long openDoorInterval, int maxFloor, float storeyHeight) throws Exception {
        if (speed <= 0) {
            throw new Exception("Speed must be more than 0");
        }
        if (maxFloor > ElevatorConfig.maxFloor) {
            throw new Exception("Elevator's max floor can't be more than " + ElevatorConfig.maxFloor);
        }
        if (maxFloor < ElevatorConfig.minFloor) {
            throw new Exception("Elevator's max floor can't be less than " + ElevatorConfig.minFloor);
        }
        if (storeyHeight < 0) {
            throw new Exception("StoreyHeight must be more than 0");
        }

        this.openDoorInterval = openDoorInterval;
        this.maxFloor = maxFloor;

        this.intervalBetweenFloors = (long) ((storeyHeight / speed) * 1000f);

        System.out.println("Elevator params: ");
        System.out.println("Storey height: " + storeyHeight + " meters");
        System.out.println("Max floor: " + maxFloor);
        System.out.println("Speed: " + speed + " m/s");
        System.out.println("Open door interval: " + openDoorInterval + " milliseconds");
        System.out.println("Interval between floor: " + intervalBetweenFloors + " milliseconds\n\n");
    }

    abstract void callFloor(int toFloor);

    abstract void callFloor(int toFloor, int fromFloor);

    void goToNextFloor(int fl) throws InterruptedException {
        Thread.sleep(intervalBetweenFloors);
        currentFloor += getDelta(fl);
        System.out.println("Current floor:" + currentFloor);
    }

    void stopOnFloor() throws InterruptedException {
        System.out.println("Open doors on floor:" + currentFloor);
        Thread.sleep(openDoorInterval);
        System.out.println("Close doors");
    }

    @SuppressWarnings("UseCompareMethod")
    private int getDelta(int fl) {
        return (fl > currentFloor) ? 1 : ((fl == currentFloor) ? 0 : -1);
    }

    void switchDirection() {
        direction = (direction == Direction.DOWN) ? Direction.UP : Direction.DOWN;
    }
}
