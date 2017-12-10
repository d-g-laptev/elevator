[![Build Status](https://travis-ci.org/d-g-laptev/elevator.svg?branch=master)](https://travis-ci.org/d-g-laptev/elevator)

# Elevator simulator

# Build:
`./gradlew shadowJar`

# Run:
`java -jar build/libs/elevator-1.0.0.jar`

# Usage:
In one console tab you can run elevator's app:
`java -jar build/libs/elevator-1.0.0.jar -h 3 -i 1000 -m 20 -s 3 3 > /tmp/elevatorStdOut`

In another you can check elevator's output:
`tail -f /tmp/elevatorStdOut` 
