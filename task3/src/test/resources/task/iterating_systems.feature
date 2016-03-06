# encoding: utf-8
# language: en
Feature: Iterating systems

  Systems in the World encapsulate game logic, typically operation on a
  family of entities.

  Iterating systems iterate over all entities which matches a predefined
  component pattern. These system typically process certain aspect of
  entities.

  For example, the velocity system is only interested position and velocity.
  Therefore, it only iterates over entities which has both position and velocity
  components - the position and velocity aspect of the entity.

  Scenario: The velocity system iterates over entities with position and velocity
    Given an entity with the following components: position, velocity
    When the world has been processed
    Then the velocity system should have 1 entity registered

  Scenario: The velocity system ignores entities without both position and velocity
    Given an entity with the following components: position
    And another entity with the following components: velocity
    When the world has been processed
    Then the velocity system should have 0 entities registered
