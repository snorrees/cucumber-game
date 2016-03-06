# encoding: utf-8
# language: en
Feature: Entity creation and deletion

  An Entity is a container for related components, sharing a common entity id.

  Entities are created and deleted by the World.
  The World has to be processed before the entity is registered in the world.

  Scenario: An entity is registered in the world after processing
    Given an entity is created by the world
    When the world has been processed
    Then the entity count in the world is 1

  Scenario: An entity is not registered in the world,
            if created outside the processing loop
    Given an entity is created by the world
    Then the entity count in the world is 0

  Scenario: An entity which is deleted is not registered in the world after processing
    Given an entity is created by the world
    And it is deleted by the world
    When the world has been processed
    Then the entity count in the world is 0
