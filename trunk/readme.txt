The code is hosted at: https://neatpacman.googlecode.com/svn/

1. Our code resides mainly in 2 places: package com.anji.neatpacman in project anji_2_01-choe, and project PacMan.

2. For the anji part, the main class for evolving is named NeatPacmanEvolver. Note that it requires 3 .properties files to run: one for our own properties to control the evolving process, one for evolving pacman, and one for evolving ghosts. It also requires 2 data files (distances.txt, junctions.txt).

3. To run evolving, you can set the working directory to /pacman_runtime and run NeatPacmanEvolver. We have .properties files and data files there. We also have a launch file in the project folder to do this.

4. For simulating the gamewe modified existing code (Project PacMan) to calculate shortest distances and retrieve game state. The class com.anji.neatpacman.simulate.Simulator connects the anji part to the game part.

5. To replay a game, set the working directory to /replay and run com.anji.neatpacman.replay.Replayer. It accepts 2 arguments: one for the pacman chromosome and one for the ghost chromosome. It also requires a .properties file and the 2 data files.

6. Note that even if you start the game with the same chromosomes, the process of the game might be different since we kept some randomness from the original game, e.g. the ghosts could choose a sub-optimal path.

Please contact us if you have problems running it.
