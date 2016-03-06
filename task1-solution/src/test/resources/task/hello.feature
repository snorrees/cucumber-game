# encoding: utf-8
# language: en
Feature: The 'Hello World!' of Cucumber
  An example feature, showing of the basics in Cucumber.

  Scenario: Hello World! capitalized equals HELLO WORLD!
    Given the input text Hello World!
    When the text is transformed into all caps
    Then the transformed text is HELLO WORLD!

  Scenario Outline: Various text capitalized
    Given the input text <Input>
    When the text is transformed into all caps
    Then the transformed text is <Output>
    And it has length <Length>
    Examples:
      | Input        | Output       | Length |
      | Hello World! | HELLO WORLD! | 12     |
      | some text    | SOME TEXT    | 9      |
