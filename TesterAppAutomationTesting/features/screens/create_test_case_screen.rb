class CreateTestCaseScreen
    def initialize
        @create_title_txt = Element.new(:id, 'create_title')
        @title_fld = Element.new(:id, 'page_title_text')
        @precon_fld = Element.new(:id, 'page_precon_text')
        @version_fld = Element.new(:id, 'page_version_text')
        @add_step_btn = Element.new(:id, 'add_step_btn')
        @title = (0...6).map { [('a'..'z'), ('A'..'Z'), ('0'..'9')].map(&:to_a).flatten.sample }.join
        @add_case_btn = Element.new(:id, 'add_test_case_btn')


    end


    def visible_title
        @create_title_txt.await_visible
    end
    def input_details(precon,version)
        @title_fld.send_keys(@title)
        @precon_fld.send_keys(precon)
        @version_fld.send_keys(version)
    end
    def click_add_step
        @add_step_btn.click
    end

    def get_title
        @title
    end

    def click_add_case
        @add_case_btn.click
    end





end