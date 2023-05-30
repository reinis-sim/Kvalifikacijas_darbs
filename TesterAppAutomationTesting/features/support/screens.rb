class Screens
    def login_screen
        @login_screen ||= LoginScreen.new
    end
    def main_screen
        @main_screen ||= MainScreen.new(create_project_screen)
    end
    def create_project_screen
        @create_project_screen ||= CreateProjectScreen.new
    end
    def project_details_screen
        @project_details_screen ||= ProjectDetailsScreen.new(create_test_case_screen)
    end
    def create_test_case_screen
        @create_test_case_screen ||= CreateTestCaseScreen.new
    end
    def create_test_step_screen
        @create_test_step_screen ||= CreateTestStepScreen.new
    end
end