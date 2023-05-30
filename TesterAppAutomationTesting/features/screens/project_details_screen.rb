class ProjectDetailsScreen
    def initialize(create_test_case_screen)
        @cases_recycler = Element.new(:id,'recycler_view_cases')
        @add_case_btn = Element.new(:id,'add_test_case_btn')
        @added_test_case = Element.new(:xpath, "//android.widget.TextView[@text='#{create_test_case_screen.get_title}']")

        @menu_btn = Element.new(:id,'proj_menu_btn')
        @menu_btn_1 = Element.new(:id, 'proj_menu_btn')
        @pdf_btn = Element.new(:xpath, '//android.widget.TextView[@text="Print PDF"]')
        @dlt_btn = Element.new(:xpath, '//android.widget.TextView[@text="Delete project"]')
        @ok_btn = Element.new(:xpath, '//android.widget.Button[@text="OK"]')


    end


    def visible_recycler
        @cases_recycler.await_visible
    end

    def click_add_case
        @add_case_btn.click
    end

    def click_menu
        @menu_btn.click
    end

    def click_menu_1
        @menu_btn_1.click
    end

    def click_pdf
        @pdf_btn.click
    end

    def click_dlt
        @dlt_btn.click
    end

    def click_ok
        @ok_btn.click
    end

    def visible_added_test_case
        @added_test_case.await_visible
    end



end