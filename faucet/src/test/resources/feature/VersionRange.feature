Feature: Version Range
  Permits parsing and matching based on a human readable range.

  Scenario Outline: Dependency Resolving
    Given a version range "<range>"
    And a version string "<version>"
    Then I expect the version to be part of this range

    Examples:
      | range         | version                   |
      | 1.0.0         | 1.0.0                     |
      | 1.0.0         | 1.0.0+git-abcdef          |
      | [1.0.0        | 1.0.0                     |
      | [1.0.0        | 1.1.0                     |
      | [1.0.0        | 1.1.1                     |
      | [1.0.0        | 2.0.0                     |
      | [1.0.0        | 1.1.0-alpha.42            |
      | 2.0.0)        | 1.1.0                     |
      | 2.0.0)        | 1.9.3                     |
      | [1.0.0,2.0.0) | 1.1.0                     |
      | [1.0.0,2.0.0) | 1.42.3-alpha.3+git-abcdef |

  Scenario Outline: Dependency Resolving
    Given a version range "<range>"
    And a version string "<version>"
    Then I expect the version to not be part of this range

    Examples:
      | range         | version |
      | 1.0.0         | 1.3.0   |
      | 1.0.0         | 2.0.0   |
      | [1.0.0        | 0.9.0   |
      | (1.0.0        | 1.0.0   |
      | 2.0.0]        | 2.1.0   |
      | 2.0.0)        | 2.0.0   |
      | [1.0.0,2.0.0) | 0.9.0   |
      | [1.0.0,2.0.0) | 2.0.0   |
