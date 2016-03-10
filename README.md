Workshop code for exploing the test-framework Cucumber.

#Requirements
The project requires the following to be installed:

* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3](https://maven.apache.org/)
* [Git](https://git-scm.com/downloads)

# Getting started
Fork this git-repository and build it.

If you are unable to access the code from the web for whatever reason, a "physical" copy should be available at the workshop-location.

##Build without solutions
`mvn clean install`

##Build with solutions
The `cucumber-solutions` module is not built by default, as not to spoilt the tasks in build-log.   
However, to build with solutions, run:

`mvn clean install -Psolution`

# Tasks & Structure
The tasks for the workshop are described in the [project wiki](https://github.com/snorrees/cucumber-game/wiki/Cucumber-Workshop).

The `cucumber-tasks` is the intended modificationpoint for the workshop. Every task has a java package (task1, task2...) and correcsponding
 resource directory (task1, task2...). 

The task directories are located under `<project-root>/cucumber-tasks/src/test/java` and 
`<project-root>/cucumber-tasks/src/test/resources` 
 
For each task, fill the task directories with whatever files and modifications you need to complete the task.

# The game
The game code is located in the `core`, while the `desktop` module contains the desktop application launcher. 
  
To run the game using the command line, run:
  
`mvn clean install -Prun`

###Run from the IDE
Run the main method in `com.cucumber.workshop.DesktopLauncher` in the `desktop` module,
with the classpath of the `desktop` and the `desktop` module as the working directory.

## Frameworks
The game is build using [LibGdx](https://libgdx.badlogicgames.com/) for rendering and [Artemis-odb](https://github.com/junkdog/artemis-odb)
for entity-component design.

## Running the game: OpenGL required
To run the game, your graphics driver must support OpenGL 2.0+.
Running the game is not a requirement to complete the Cucumber workshop, however.

## Playing the game

* The player is located in the center.
* Everyone else are deadly enemies.
* Enemies in greyscale cannot be attacked.
* Enemies with a color can be attacked by clicking them.
* When an enemy is attacked, the attacktype of the cursor will change AND the enemy type of all enemies will change.
* Hit F5 to resstart the game.
* Hit F2 for debug overlay.


Yupp - its very bare-bones ;-)


