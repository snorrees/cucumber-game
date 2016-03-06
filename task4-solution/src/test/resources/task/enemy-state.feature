# encoding: utf-8
# language: en
@world
Feature: Enemy type changes with player interaction

  There are 3 types of enemies in the game: green, blue and red.

  The player attacks enemies by clicking on them.
  At any point in time, the player can only damage enemies of
  a single type.

  For example: If the attack type is green, only green enemies
  can be affected by the attack.

  A successful attack is performed, it will reduce the hp of the enemy by 1.
  Enemies with 0 hp will be removed from the world.

  Whenever an enemy takes damage, ALL enemies in the world changes
  enemy type. The type transition is as follows:

  green -> blue -> red -> green

  So when damage is inflicted, all blue enemies will become red, for instance.

  Additionally, when damage is inflicted, the player attack type also changes
  as follows:

  green -> red -> blue -> green

  Scenario: A green enemy is attacked, and all enemies changes type.
    Given a world with a player who can interact with green enemies
    And it contains enemies with the following state:
      | x      | y     | radius | Hit Points | Enemy type |
      | 400.0  | 500.0 | 10     | 2          | green      |
      | 800.0  | 800.0 | 10     | 2          | blue       |
      | 1000.0 | 200.0 | 10     | 2          | red        |
    When the user clicks at 400, 500
    And the world is processed
    Then the world should contain enemies with the following state:
      | x      | y     | Enemy type |
      | 400.0  | 500.0 | blue        |
      | 800.0  | 800.0 | red        |
      | 1000.0 | 200.0 | green      |

  Scenario: A green enemy is killed, and is removed from the game.
    Given a world with a player who can interact with green enemies
    And it contains enemies with the following state:
      | x      | y     | radius | Hit Points | Enemy type |
      | 400.0  | 500.0 | 10     | 1          | green      |
      | 800.0  | 800.0 | 10     | 1          | blue       |
      | 1000.0 | 200.0 | 10     | 1          | red        |
    When the user clicks at 400, 500
    And the world is processed
    Then the world should contain enemies with the following state:
      | x      | y     | Enemy type |
      | 800.0  | 800.0 | red        |
      | 1000.0 | 200.0 | green      |