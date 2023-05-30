Feature: Main Screen



    @TC1
    Scenario: User adds and deletes a test case
        Given Login screen is open
        And User enters e-mail and password
        And User taps on login
        Then Main screen is visible
        And User taps on add project button
        Then Create project screen is visible
        And User enters the title and description
        Then Project is added
        And User taps on add button
        And User taps on the project
        Then Project details screen is visible
        And User taps on add test case
        Then Add test case screen is open
        And User fills the text fields
        And User taps on add step
        Then Add step screen is visible
        And User fills step details
        And User taps on contains SMS
        And User fills sms fields
        And User taps on save step button
        Then User is back in test case screen
        And User taps on save test case button
        Then Test case appears in the project details screen
        And User opens the menu
        And User taps on "Generate PDF"
        And User taps to open the project menu button the second time
        And User taps on "Delete project"
        And User taps on "OK"
        Then User appears in the main screen
        And User taps on the menu button
        And User taps on Logout
        Then User is in login screen






