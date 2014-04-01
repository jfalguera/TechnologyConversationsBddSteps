package com.technologyconversations.bdd.steps;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.codeborne.selenide.*;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.opera.core.systems.OperaDriver;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class WebSteps {

    // TODO Add methods that use selector with index
    // TODO Add methods that use findAll selector ($$)
    // TODO Add uploadFromClasspath
    // TODO Add options
    // TODO Add focused
    // TODO Add selected

    private final String selectorsInfo = "\nAny CSS selector can be used to locate the element. " +
            "Most commonly used selectors are:\n" +
            ".myClass: Matches any element with class equal to 'myClass'\n" +
            "#myId: Matches any element with ID equal to 'myId'\n" +
            "For more information on CSS selectors please consult http://www.w3.org/TR/CSS21/selector.html.\n" +
            "Additional selectors are:\n" +
            "text:my text: Matches any element with text equal to 'my text'";
    private final String caseInsensitive = "\nVerification is NOT case sensitive.";

    private Map<String, String> params;
    @BddParamsBean()
    public void setParams(Map<String, String> value) {
        params = value;
    }
    public Map<String, String> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    /*
    Supported drivers are: firefox (default), chrome (the fastest, recommended), htmlunit (headless browser),
    ie, opera (slow and unstable, not recommended), phantomjs (headless browser).
    Same effect can be accomplished with System parameter (i.e. -Dbrowser=chrome)
    TODO Test
    */
    private WebDriver webDriver;
    public void setWebDriver(String driver) {
        if (driver == null) {
            webDriver = null;
        } else {
            switch (driver.toLowerCase()) {
                case "firefox":
                    webDriver = new FirefoxDriver();
                    break;
                case "chrome":
                    webDriver = new ChromeDriver();
                    break;
                case "htmlunit":
                    HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_17);
                    htmlUnitDriver.setJavascriptEnabled(true);
                    webDriver = htmlUnitDriver;
                    break;
                case "ie":
                    webDriver = new InternetExplorerDriver();
                    break;
                case "opera":
                    webDriver = new OperaDriver();
                    break;
                case "phantomjs":
                    webDriver = new PhantomJSDriver();
                    break;
                default:
                    throw new RuntimeException(driver + " driver is currently not supported");
            }
            WebDriverRunner.setWebDriver(webDriver);
            setSize();
        }
    }
    @BddParam(value = "browser", description = "Supported drivers are: firefox (default), " +
            "chrome (the fastest, recommended), htmlunit (headless browser), ie, " +
            "opera (slow and unstable, not recommended), phantomjs (headless browser).")
    public void setWebDriver() {
        if (getWebDriver() == null) {
            String browser = "firefox";
            if (getParams().containsKey("browser")) {
                browser = getParams().get("browser");
            }
            setWebDriver(browser);
        }
    }
    protected WebDriver getWebDriver() {
        return webDriver;
    }

    // Given

    @BddDescription("Opens specified address.")
    @Given("Web address $url is opened")
    public void open(String url) {
        setWebDriver();
        Selenide.open(url);
    }

    @BddDescription("Opens address specified by webUrl parameter.")
    @BddParam(value = "url", description = "Web address used with the 'Given Web home page is opened' step.")
    @Given("Web home page is opened")
    public void open() {
        assertThat(getParams(), hasKey("url"));
        setWebDriver();
        Selenide.open(getParams().get("url"));
    }

    @BddDescription("Sets timeout used when operating with elements. Default value is 4 seconds.")
    @BddParam(value = "timeout", description = "Sets timeout used when operating with elements. Default value is 4 seconds.")
    @Given("Web timeout is $seconds seconds")
    public void setConfigTimeout(int seconds) {
        Configuration.timeout = seconds * 1000;
    }
    protected int getConfigTimeout() {
        Long value = Configuration.timeout / 1000;
        return value.intValue();
    }

    @BddDescription("Sets browser window size")
    @Given("Web window size is $width width and $height height")
    @BddParam(value = "widthHeight", description = "Sets window width and height. Values should be separated by comma (i.e. 1024, 768)")
    public void setSize(int width, int height) {
        getWebDriver().manage().window().setSize(new Dimension(width, height));
    }
    protected void setSize() {
        if (getParams().containsKey("widthHeight")) {
            String[] widthHeightArray = getParams().get("widthHeight").split(",");
            assertThat("widthHeight must contain two numbers separated by comma.", widthHeightArray.length, is(2));
            int width = Integer.parseInt(widthHeightArray[0].trim());
            int height = Integer.parseInt(widthHeightArray[1].trim());
            setSize(width, height);
        }
    }

    // When

    @BddDescription("Clicks the element." + selectorsInfo)
    @When("Web user clicks the element $selector")
    public void clickElement(String selector) {
        findElement(selector).click();
    }

    @BddDescription("Clears the text field and sets the specified value." + selectorsInfo)
    @When("Web user sets value $value to the element $selector")
    public void setElementValue(String value, String selector) {
        findElement(selector).setValue(value);
    }

    @BddDescription("Appends the specified value." + selectorsInfo)
    @When("Web user appends value $value to the element $selector")
    public void appendElementValue(String value, String selector) {
        findElement(selector).append(value);
    }

    @BddDescription("Presses enter key on a specified element" + selectorsInfo)
    @When("Web user presses the enter key in the element $selector")
    public void pressEnter(String selector) {
        findElement(selector).pressEnter();
    }

    @BddDescription("Select an option from dropdown list" + selectorsInfo)
    @When("Web user selects $text from the dropdown list $selector")
    public void selectOption(String text, String selector) {
        findElement(selector).selectOption(text);
    }

    // Then

    @BddDescription("Verifies that the title of the current page is as expected.")
    @Then("Web page title is $title")
    public void checkTitle(String title) {
        System.out.println(title() + " " + title);
        assertThat(title(), equalTo(title));
    }

    @BddDescription("Verifies that the element text contains the specified text." +
            caseInsensitive + selectorsInfo)
    @Then("Web element $selector should have text $text")
    public void shouldHaveText(String selector, String text) {
        findElement(selector).shouldHave(text(text));
    }

    @BddDescription("Verifies that the element text does NOT contain the specified text." +
            caseInsensitive + selectorsInfo)
    @Then("Web element $selector should NOT have text $text")
    public void shouldNotHaveText(String selector, String text) {
        findElement(selector).shouldNotHave(text(text));
    }

    @BddDescription("Verifies that the element text matches the specified regular expression." +
            " For example, 'Hello, .*, how are you!' uses '.*' to match any text." + selectorsInfo)
    @Then("Web element $selector should have matching text $regEx")
    public void shouldHaveMatchText(String selector, String regEx) {
        findElement(selector).shouldHave(matchText(regEx));
    }

    @BddDescription("Verifies that the element text does NOT match the specified regular expression." + selectorsInfo)
    @Then("Web element $selector should NOT have matching text $regEx")
    public void shouldNotHaveMatchText(String selector, String regEx) {
        findElement(selector).shouldNotHave(matchText(regEx));
    }

    @BddDescription("Verifies that the element text is exactly the same as the specified text." +
            caseInsensitive + selectorsInfo)
    @Then("Web element $selector should have exact text $text")
    public void shouldHaveExactText(String selector, String text) {
        findElement(selector).shouldHave(exactText(text));
    }

    @BddDescription("Verifies that the element text is NOT exactly the same as the specified text." +
            caseInsensitive + selectorsInfo)
    @Then("Web element $selector should NOT have exact text $text")
    public void shouldNotHaveExactText(String selector, String text) {
        findElement(selector).shouldNotHave(exactText(text));
    }

    @BddDescription("Verifies that the element value is the same as specified." + selectorsInfo)
    @Then("Web element $selector should have value $value")
    public void shouldHaveValue(String selector, String value) {
        findElement(selector).shouldHave(value(value));
    }

    @BddDescription("Verifies that the element value is NOT the same as specified." + selectorsInfo)
    @Then("Web element $selector should NOT have value $value")
    public void shouldNotHaveValue(String selector, String value) {
        findElement(selector).shouldNotHave(value(value));
    }

    @BddDescription("Verifies that the text of the selected dropdown list element is the same as text")
    @Then("Web dropdown list $selector has $text selected")
    public void shouldHaveSelectedOption(String selector, String text) {
        findElement(selector).getSelectedOption().shouldHave(text(text));
    }

    @BddDescription("Verifies that the text of the selected dropdown list element is NOT the same as text")
    @Then("Web dropdown list $selector does NOT have $text selected")
    public void shouldNotHaveSelectedOption(String selector, String text) {
        findElement(selector).getSelectedOption().shouldNotHave(text(text));
    }

    @BddDescription("Verifies that the element is visible (appears) and present (exists)")
    @Then("Web element $selector is visible")
    @Alias("Web element $selector appears")
    public void shouldBeVisible(String selector) {
        findElement(selector).shouldBe(visible);
    }

    @BddDescription("Verifies that the element is hidden (not visible, disappeared)")
    @Then("Web element $selector is hidden")
    @Aliases(values = {
            "Web element $selector disappeared",
            "Web element $selector is NOT visible"
    })
    public void shouldBeHidden(String selector) {
        findElement(selector).shouldBe(hidden);
    }

    @BddDescription("Verifies that the element is present (exists)")
    @Then("Web element $selector is present")
    @Alias("Web element $selector exists")
    public void shouldBePresent(String selector) {
        findElement(selector).shouldBe(present);
    }

    @BddDescription("Verifies that the element is read-only")
    @Then("Web element $selector is read-only")
    public void shouldBeReadOnly(String selector) {
        findElement(selector).shouldBe(readonly);
    }

    @BddDescription("Verifies that the element text (or value in case of input) is empty.")
    @Then("Web element $selector is empty")
    public void shouldBeEmpty(String selector) {
        findElement(selector).shouldBe(empty);
    }

    @BddDescription("Verifies that the element is enabled")
    @Then("Web element $selector is enabled")
    public void shouldBeEnabled(String selector) {
        findElement(selector).shouldBe(enabled);
    }

    @BddDescription("Verifies that the element is disabled")
    @Then("Web element $selector is disabled")
    public void shouldBeDisabled(String selector) {
        findElement(selector).shouldBe(disabled);
    }

    // Common methods

    protected SelenideElement findElement(String selector) {
        String byTextPrefix = "text:";
        if (selector.startsWith(byTextPrefix)) {
            return $(Selectors.byText(selector.substring(byTextPrefix.length())));
        } else {
            if (Character.isLetter(selector.charAt(0))) {
                selector = "#" + selector;
            }
            return $(selector);
        }
    }

    @BeforeStories
    public void beforeStoriesWebSteps() {
        if (getParams().containsKey("timeout")) {
            try {
                setConfigTimeout(Integer.parseInt(getParams().get("timeout")));
            } catch (NumberFormatException e) {
                // Do nothing
            }
        }
    }

    @AfterStories
    public void afterStoriesWebSteps() {
        WebDriverRunner.closeWebDriver();
        if (getWebDriver() != null) {
            getWebDriver().close();
            getWebDriver().quit();
        }
    }

}
