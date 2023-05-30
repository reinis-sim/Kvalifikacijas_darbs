module Helpers

    def debug(text)
        puts "#{Time.now.strftime('%Y-%m-%d %H:%M:%S')}: #{text}"
    end

    def add_screenshot(scenario_name)
        scenario_name.tr!(' ', '_')
        local_name = "reports/screenshot-#{scenario_name}-#{Time.now.to_i}.png"
        $driver.screenshot(local_name)
        attach(local_name, 'image/png')
    end

end