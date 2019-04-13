Feature: Version
  Permits the parsing and comparing of semantic version numbers.

  Scenario Outline: A developer supplies a version number
  The human readable version is parsed and split into its respective segments.

    Given a version string "<version>"
    Then I expect the major bit to be "<major>"
    And minor to be "<minor>"
    And patch to be "<patch>"
    And extra to be "<extra>"
    And metadata to be "<metadata>"
    And it to be <stability>

    Examples:
      | version                | major | minor | patch | extra   | metadata   | stability         |
      | 1.0.0-alpha+git-abcdef | 1     | 0     | 0     | alpha   | git-abcdef | ALPHA             |
      | 1.1.0-alpha+git-012345 | 1     | 1     | 0     | alpha   | git-012345 | ALPHA             |
      | 2.0.1-beta.12+svn-42   | 2     | 0     | 1     | beta.12 | svn-42     | BETA              |
      | 3.0.1-rc+svn-42        | 3     | 0     | 1     | rc      | svn-42     | RELEASE_CANDIDATE |
      | 3.0.1-rc.4+svn-42      | 3     | 0     | 1     | rc.4    | svn-42     | RELEASE_CANDIDATE |
      | 3.1.42                 | 3     | 1     | 42    |         |            | STABLE            |

  Scenario Outline: Resolving a dependency
  The extension version is compared against a given minimal version number

    Given a version string "<old>"
    And a version string "<new>"
    Then expect new to be more recent than old
    And new to be less recent than old

    Examples:
      | old           | new           |
      | 1.0.0         | 2.0.0         |
      | 2.0.0         | 2.1.0         |
      | 2.1.0         | 2.1.1         |
      | 2.1.0-alpha   | 2.1.0         |
      | 2.1.0-alpha   | 2.1.0-beta    |
      | 2.1.0-beta    | 2.1.0-rc      |
      | 2.1.0-alpha   | 2.1.0-alpha.1 |
      | 2.1.0-alpha.1 | 2.1.0-alpha.2 |
