require 'appium_lib'
require 'cucumber'
require_relative 'helpers'

include Helpers

opts = {

    caps:{
        platformName: "Android",
        avd: "Pixel_C_API_28",
        noReset: false,
        autoGrantPermissions: true,
        automationName: "UiAutomator2",
        deviceName: "Pixel C",
        app: "/Users/reinis.simsons/Downloads/app-debug.apk"
    },
    appium_lib: {
        server_url: 'http://localhost:4723/wd/hub'
    }
}

$driver = Appium::Driver.new(opts,true)


Before do
    $driver.start_driver
    @screens = Screens.new
end

After do |scenario|

    begin
        add_screenshot(scenario.name) if scenario.failed?
    rescue => e
        debug "Failed to add screenshot:\n#{e.message}"
    end
    
    $driver.driver_quit
end