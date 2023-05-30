class CreateProjectScreen


    def initialize
        @save_project_btn = Element.new(:id, 'save_project_btn')
        @title_fld = Element.new(:id, 'project_title_text')
        @desc_fld = Element.new(:id, 'project_desc_text')
        @title = (0...6).map { [('a'..'z'), ('A'..'Z'), ('0'..'9')].map(&:to_a).flatten.sample }.join
        @description = (0...20).map { [('a'..'z'), ('A'..'Z'), ('0'..'9')].map(&:to_a).flatten.sample }.join


    end

    def input_details
        @title_fld.send_keys(@title)
        @desc_fld.send_keys(@description)
    end


    def click_save_project
        @save_project_btn.click
    end

    def visible_save_project
        @save_project_btn.await_visible
    end

    def get_title
        @title
    end

    def get_description
        @description
    end



end