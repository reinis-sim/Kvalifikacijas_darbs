class LoginScreen
    def initialize
        @email_fld = Element.new(:id, 'email_edit_text')
        @pwd_fld = Element.new(:id, 'password_edit_text')
        @login_btn = Element.new(:id, 'login_btn')
        @login_icon = Element.new(:id, 'login_icon')
    end

    def input_credentials(email,pwd)
        @email_fld.send_keys(email)
        @pwd_fld.send_keys(pwd)
    end


    def click_login
        @login_btn.click
    end

    def visible_login
        @login_icon.await_visible
    end

end