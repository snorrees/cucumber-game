# encoding: utf-8
# language: en
Feature: The Velocity System

  The velocity system should change the position of an entity every time the world is processed according to the following formula:
  new x-position = current x-position * delta
  new y-position = current y-position * delta

  Scenario: The velocity system changes the position of an entity
    Given a delta of 1
    And an entity is created in the world
    And position is added
    And velocity is added
    And position is set to 0, 0
    And velocity is set to 1, 2
    When the world has been processed
    Then it should have position 1, 2

  Scenario Outline: The velocity system changes the position of an entity
    Given a delta of <delta>
    And an entity is created in the world
    And position is added
    And velocity is added
    And position is set to 0, 0
    And velocity is set to <dx>, <dy>
    When the world has been processed
    Then it should have position <final x>, <final y>
    Examples:
      | delta | dx | dy | final x | final y |
      | 0     | 0  | 0  | 0       | 0       |
      | 1     | 1  | 0  | 1       | 0       |
      | 1     | 0  | 1  | 0       | 1       |
      | 1     | 1  | 1  | 1       | 1       |
      | 0.5   | 1  | 1  | 0.5     | 0.5     |