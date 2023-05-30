class Element

  BASE_TIMEOUT = 15
  BASE_CHECK_INTERVAL = 0.2

  def initialize(type, value)
    @type = type
    @value = value
    @elem = { @type => @value }
  end

  # wrapper to handle wait and rescue for different element methods
  private def waiter_wrapper(timeout, interval)
    $driver.wait({ timeout: timeout, interval: interval }) { yield }
  rescue Appium::Core::Wait::TimeoutError
    raise "Exceeded #{timeout}s during element search - locator: #{@elem}"
  rescue => error
    raise "Encountered error - locator: #{@elem}: #{error.inspect}"
  end

  # Finds the element, or if there are multiple, only the first one
  def find(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Searching for #{timeout}s - locator: #{@elem}")
    waiter_wrapper(timeout, interval) do
      element = $driver.find_element(@type, @value)
      debug('Element found!')
      return element
    end
  end

  # Finds all elements matching the locator
  def find_all(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Searching for all for #{timeout}s - locator: #{@elem}")
    waiter_wrapper(timeout, interval) do
      elements = $driver.find_elements(@type, @value)
      raise Exception if elements.empty?

      debug('Elements found!')
      return elements
    end
  end

  # Waits until element is visible
  def await_visible(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Waiting for #{timeout}s until visible - locator: #{@elem}")
    waiter_wrapper(timeout, interval) do
      raise Exception unless $driver.find_element(@type, @value).displayed?

      debug('Element visible!')
      return true
    end
  end
  
    # Waits until element is selected
    def await_selected(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
      debug("Waiting for #{timeout}s until selected - locator: #{@elem}")
      waiter_wrapper(timeout, interval) do
        raise Exception unless $driver.find_element(@type, @value).selected?
  
        debug('Element selected!')
        return true
      end
    end

  # Waits until element is not visible
  def await_not_visible(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Waiting for #{timeout}s until not visible - locator: #{@elem}")
    waiter_wrapper(timeout, interval) do
      raise Exception if $driver.find_element(@type, @value).displayed?

      debug('Element not visible!')
      return true
    end
  end

  # Clicks the element
  def click(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Searching for #{timeout}s and clicking - locator: #{@elem}")
    waiter_wrapper(timeout, interval) do
      $driver.find_element(@type, @value).click
      debug('Element clicked!')
    end
  end

  # Clicks on the element using its middle point coordinates
  def click_on_coords(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Searching for #{timeout}s and clicking over coordinates - locator: #{@elem}")
    waiter_wrapper(timeout, interval) {
      element = $driver.find_element(@type, @value)
      x_coord = element.location.x + element.size.width * 0.5
      y_coord = element.location.y + element.size.height * 0.5
      debug("Clicking on (#{x_coord};#{y_coord})")
      $driver.action.move_to_location(x_coord, y_coord).pointer_down(:left).release.perform
      debug('Element coordinates clicked!')
    }
  end

  # Sends keys to the element
  def send_keys(text, timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Searching for #{timeout}s and sending keys '#{text}' - locator: #{@elem}")
    waiter_wrapper(timeout, interval) do
      $driver.find_element(@type, @value).send_keys(text)
      $driver.hide_keyboard
      debug('Keys sent!')
    end
  end

  def get_text(timeout = BASE_TIMEOUT, interval = BASE_CHECK_INTERVAL)
    debug("Getting text from element - locator: #{@elem}")
    waiter_wrapper(timeout, interval) do
      $driver.find_element(@type, @value).text
    end
  end
end