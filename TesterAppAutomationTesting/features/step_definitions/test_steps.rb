Given (/Login screen is open/) do
    @screens.login_screen.visible_login
end
And (/User enters e-mail and password/)do
    @screens.login_screen.input_credentials("reinissimsons@gmail.com","pass123")
end
And (/User taps on login/)do
    @screens.login_screen.click_login
end
Then (/Main screen is visible/)do
    @screens.main_screen.visible_title
end
And (/User taps on add project button/)do
    @screens.main_screen.click_add_project
end
Then (/Create project screen is visible/)do
    @screens.create_project_screen.visible_save_project
end
And (/User enters the title and description/)do
    @screens.create_project_screen.input_details
end
And (/User taps on add button/)do
    @screens.create_project_screen.click_save_project
end
Then (/Project is added/)do
    @screens.main_screen.visible_title
end
And (/User taps on the project/)do
    @screens.main_screen.click_added_project
end
Then (/Project details screen is visible/)do
    @screens.project_details_screen.visible_recycler
end
And (/User taps on add test case/)do
    @screens.project_details_screen.click_add_case
end
Then (/Add test case screen is open/)do
    @screens.create_test_case_screen.visible_title
end
And (/User fills the text fields/)do
    @screens.create_test_case_screen.input_details("Precondition", "12.9.3")
end
And (/User taps on add step/)do
    @screens.create_test_case_screen.click_add_step
end
Then (/Add step screen is visible/)do
    @screens.create_test_step_screen.visible_title
end
And (/User fills step details/)do
    @screens.create_test_step_screen.input_details
end
And (/User taps on contains SMS/)do
    @screens.create_test_step_screen.click_switch
end
And (/User fills sms fields/)do
    @screens.create_test_step_screen.input_msg
end
And (/User taps on save step button/)do
    @screens.create_test_step_screen.click_add_step
end
Then (/User is back in test case screen/)do
    @screens.create_test_case_screen.visible_title
end
And (/User taps on save test case button/)do
    @screens.create_test_case_screen.click_add_case
end
Then (/Test case appears in the project details screen/)do
    @screens.project_details_screen.visible_added_test_case
end

And (/User taps to open the project menu button the second time/)do
    @screens.project_details_screen.click_menu
end
And (/User taps on "Generate PDF"/)do
    @screens.project_details_screen.click_pdf
end
And (/User opens the menu/)do
    @screens.project_details_screen.click_menu
end
And (/User taps on "Delete project"/)do
    @screens.project_details_screen.click_dlt
end
And (/User taps on "OK"/)do
    @screens.project_details_screen.click_ok
end
Then (/User appears in the main screen/)do
    @screens.main_screen.visible_title
end
And (/User taps on the menu button/)do
    @screens.main_screen.click_menu
end
And (/User taps on Logout/)do
    @screens.main_screen.click_logout
end
Then (/User is in login screen/)do
    @screens.login_screen.visible_login
end

