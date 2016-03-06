# encoding: utf-8
# language: en
@world
Feature: Enemy type changes with player interaction

  Scenario: Initial state
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