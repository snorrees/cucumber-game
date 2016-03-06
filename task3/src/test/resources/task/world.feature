# encoding: utf-8
# language: en
@world
Feature: The World

  The World is an isolated container for entities, systems and components.

  All systems are registered during World creation.

  The World is processed continuously, using delta as measurement of how much time
  has passed in seconds since the last update.

  During each process cycle, every system in the world is processed once.

  Scenario Outline: Delta is accumulated by the elapsed time system
    Given a delta of <delta>
    When the world has been processed <n> times
    Then the elapsed time system has accumulated <elapsed time> seconds
    Examples:
      | delta | n | elapsed time |
      | 1     | 0 | 0            |
      | 1     | 1 | 1            |
      | 1     | 2 | 2            |
      | 0.13  | 3 | 0.39         |
      | 0     | 1 | 0            |