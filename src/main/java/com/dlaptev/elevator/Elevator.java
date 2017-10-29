package com.dlaptev.elevator;

import java.util.*;

public class Elevator extends AbstractElevator {

    private static class Job {
        List<Integer> toFloors = new ArrayList<>();

        Job(Integer toFloor) {
            toFloors.add(toFloor);
        }
    }

    private SortedSet<Integer> upList = Collections.synchronizedSortedSet(new TreeSet<Integer>());
    private Map<Integer, Job> upJobList = Collections.synchronizedMap(new HashMap<>());
    private SortedSet<Integer> downList = Collections.synchronizedSortedSet(
            new TreeSet<Integer>((x, y) -> Integer.compare(x, y) * -1)
    );
    private Map<Integer, Job> downJobList = Collections.synchronizedMap(new HashMap<>());

    Elevator(float speed, int openDoorInterval, int maxFloor, float storeyHeight) throws Exception {
        super(speed, openDoorInterval, maxFloor, storeyHeight);
    }

    @Override
    public void run() {
        while (true) {
            SortedSet<Integer> list = getCurrentList();

            if (list.isEmpty()) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }

            while (list.iterator().hasNext()) {
                Integer fl = list.iterator().next();

                while (!fl.equals(currentFloor)) {
                    try {
                        goToNextFloor(fl);
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                try {
                    stopOnFloor();
                    removeCurrentFloorFromList();
                    checkJobs();
                } catch (InterruptedException e) {
                    return;
                }
            }

            switchDirection();
        }
    }

    private void removeCurrentFloorFromList() {
        SortedSet<Integer> currentList = getCurrentList();
        currentList.remove(currentFloor);
    }

    private SortedSet<Integer> getCurrentList() {
        return (direction == Direction.DOWN) ? downList : upList;
    }

    private Map<Integer, Job> getCurrentJobsList() {
        return (direction == Direction.DOWN) ? downJobList : upJobList;
    }

    private void checkJobs() {
        Map<Integer, Job> jobsList = getCurrentJobsList();

        if (hasNoJobsOnCurrentFloor() && getCurrentList().isEmpty()) {
            jobsList = (direction == Direction.DOWN) ? upJobList : downJobList;
        }

        if (!jobsList.containsKey(currentFloor)) return;

        Job j = jobsList.get(currentFloor);

        for (Integer nextFloor : j.toFloors) {
            callFloor(nextFloor);
        }

        j.toFloors = new ArrayList<>();
        jobsList.put(currentFloor, j);
    }

    private boolean hasNoJobsOnCurrentFloor() {
        Map<Integer, Job> jobsList = getCurrentJobsList();
        return (!jobsList.containsKey(currentFloor) || jobsList.get(currentFloor).toFloors.isEmpty());
    }

    @Override
    public synchronized void callFloor(int toFloor, int fromFloor) {
        callFloor(fromFloor);

        Map<Integer, Job> jobList = getCurrentJobsList();
        Job j = jobList.getOrDefault(fromFloor, new Job(toFloor));

        j.toFloors.add(toFloor);
        jobList.put(fromFloor, j);
    }

    @Override
    public void callFloor(int toFloor) {
        if (toFloor < 1 || toFloor > maxFloor) {
            System.err.println("Floor is our of range");
            return;
        }

        SortedSet<Integer> list = getCurrentList();
        list.add(toFloor);
    }

}
