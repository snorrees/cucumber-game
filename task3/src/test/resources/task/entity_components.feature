# encoding: utf-8
# language: en
Feature: Entity components

  An Entity is a container for related components, sharing a common entity id.

  Components can be added to the entity after the entity has been created.

  The components can be retrieved from the entity by type.
  Components represent the state of an entity.

  Background:
    Given an entity is created by the world

  Scenario: An entity has no components when created
    When the world has been processed
    Then the entity should have 0 components

  Scenario: An added position component gives the entity position
    Position represents the x and y coordinate of the entity in the World.
    Given that position is added
    When the world has been processed
    Then the entity should have position 0, 0
    And it should have 1 component

  Scenario: An added velocity component gives the entity velocity
    Velocity is a vector which expresses how many pixels
    entity position should per second in the x and y direction.
    Velocity is expressed as dx, dy.
    Given that velocity is added
    When the world has been processed
    Then the entity should have velocity 0, 0
    And it should have 1 component

  Scenario: Position and velocity components are added to an entity
    Given that position is added
    And velocity is added
    When the world has been processed
    Then the entity should have position 0, 0
    And it should have velocity 0, 0
    And it should have 2 component

  Scenario: Position can be changed
    Given that position is added
    And position is set to 2, 5
    When the world has been processed
    Then the entity should have position 2, 5

  Scenario: Velocity can be changed
    Given that velocity is added
    And velocity is set to 1, 3
    When the world has been processed
    Then the entity should have velocity 1, 3

