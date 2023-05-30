class CreateTestStepScreen
    def initialize
        @create_title_txt = Element.new(:id, 'step_title_lay')
        @add_step_btn = Element.new(:id, 'add_step_btn')
        @sms_switch = Element.new(:id, 'sms_switch')
        @title_fld = Element.new(:id, 'step_title_text')
        @data_fld = Element.new(:id, 'test_data_text')
        @result_fld = Element.new(:id, 'expected_result_text')
        @sender_id_fld = Element.new(:id, 'sms_sender_text')
        @msg_fld = Element.new(:id, 'sms_msg_text')
        @step = (0...6).map { [('a'..'z'), ('A'..'Z'), ('0'..'9')].map(&:to_a).flatten.sample }.join
        @data = (0...6).map { [('a'..'z'), ('A'..'Z'), ('0'..'9')].map(&:to_a).flatten.sample }.join
        @result = (0...6).map { [('a'..'z'), ('A'..'Z'), ('0'..'9')].map(&:to_a).flatten.sample }.join
        @sender = "Facebook"
        @msg = "Hello!"
    end


    def visible_title
        @create_title_txt.await_visible
    end
    def input_details
        @title_fld.send_keys(@step)
        @data_fld.send_keys(@data)
        @result_fld.send_keys(@result)
    end
    def input_msg
        @sender_id_fld.send_keys(@sender)
        @msg_fld.send_keys(@msg)
    end
    def click_add_step
        @add_step_btn.click
    end
    def click_switch
        @sms_switch.click
    end





end