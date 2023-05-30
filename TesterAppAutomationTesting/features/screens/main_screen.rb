class MainScreen
    def initialize(create_project_screen)
        @main_title_txt = Element.new(:id, 'page_title')
        @add_project_btn = Element.new(:id, 'add_project_btn')
        @added_project = Element.new(:xpath, "//android.widget.TextView[@text='#{create_project_screen.get_title}']")

        @menu_btn = Element.new(:id,'menu_btn')
        @logout_btn = Element.new(:xpath, '//android.widget.TextView[@text="Logout"]')

    end

    def click_add_project
        @add_project_btn.click
    end

    def click_added_project
        @added_project.click
    end

    def click_menu
        @menu_btn.click
    end

    def click_logout
        @logout_btn.click
    end

    def visible_title
        @main_title_txt.await_visible
    end

end