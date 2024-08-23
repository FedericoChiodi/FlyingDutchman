package com.ingsw.flyingdutchman.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Tag("e2e")
public class UserSeleniumTest {

    WebDriver driver;

    @BeforeEach
    void setup() {
        driver = WebDriverManager.chromiumdriver().create();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLogin_successo() {
        driver.get("http://localhost:8080/");

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("Mario12");
        passwordField.sendKeys("pass");

        submitButton.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        String expectedUrl = "http://localhost:8080/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        WebElement welcomeMessage = driver.findElement(By.id("welcomeMessage"));
        assertEquals("Benvenuto Mario12, su Flying Dutchman!", welcomeMessage.getText());
    }

    @Test
    public void testLogin_fallimento_password() {
        driver.get("http://localhost:8080/");

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("Mario12");
        passwordField.sendKeys("wrong!");

        submitButton.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        String error = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();

        String expectedUrl = "http://localhost:8080/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        assertEquals("Invalid username or password", error);
    }

    @Test
    public void testLogin_fallimento_user() {
        driver.get("http://localhost:8080/");

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("wrong!");
        passwordField.sendKeys("wrong!");

        submitButton.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        String error = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();

        String expectedUrl = "http://localhost:8080/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        assertEquals("Could not find that User", error);
    }

    @Test
    public void testModify_success() {
        driver.get("http://localhost:8080/");

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("Mario12");
        passwordField.sendKeys("pass");

        submitButton.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        String expectedUrl = "http://localhost:8080/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        driver.get("http://localhost:8080/userManagement/view");

        WebElement modifyUserButton = driver.findElement(By.id("modifyUserButton"));
        modifyUserButton.click();

        WebElement city_field = driver.findElement(By.id("city"));
        city_field.clear();
        city_field.sendKeys("Roma");

        WebElement submitButton2 = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButton2.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        String expectedUrl2 = "http://localhost:8080/userManagement/modify";
        String actualUrl2 = driver.getCurrentUrl();
        assertEquals(expectedUrl2, actualUrl2);

        WebElement modifyUserButton2 = driver.findElement(By.id("modifyUserButton"));
        modifyUserButton2.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement city_field2 = driver.findElement(By.id("city"));
        assertEquals("Roma", city_field2.getAttribute("value"));
    }

    @Test
    public void testRegister() {
        driver.get("http://localhost:8080/userManagement/insert");

        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement firstname = driver.findElement(By.id("firstname"));
        WebElement surname = driver.findElement(By.id("surname"));
        WebElement birthdate = driver.findElement(By.id("birthdate"));
        WebElement address = driver.findElement(By.id("address"));
        WebElement civic_number = driver.findElement(By.id("civic_number"));
        WebElement cap = driver.findElement(By.id("cap"));
        WebElement city = driver.findElement(By.id("city"));
        WebElement state = driver.findElement(By.id("state"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement cel_number = driver.findElement(By.id("cel_number"));

        List<WebElement> submitButton = driver.findElements(By.cssSelector("input[type='submit']"));

        username.sendKeys("test_user");
        password.sendKeys("pass");
        firstname.sendKeys("test_firstname");
        surname.sendKeys("test_surname");
        birthdate.sendKeys("01-01-1990");
        address.sendKeys("test_address");
        civic_number.sendKeys("1");
        cap.sendKeys("1345");
        city.sendKeys("test_city");
        state.sendKeys("test_state");
        email.sendKeys("test_email@email.com");
        cel_number.sendKeys("12345567");

        submitButton.get(1).click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        String message = driver.switchTo().alert().getText();
        assertEquals("Account creato correttamente!", message);

        driver.switchTo().alert().accept();

        String expectedUrl = "http://localhost:8080/userManagement/register";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButtonField = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("test_user");
        passwordField.sendKeys("pass");

        submitButtonField.click();

        WebElement welcomeMessage = driver.findElement(By.id("welcomeMessage"));
        assertEquals("Benvenuto test_user, su Flying Dutchman!", welcomeMessage.getText());
    }

    @Test
    public void testBan() {
        testRegister();

        driver.get("http://localhost:8080/userManagement/ban");

        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("test_user");

        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButton.click();

        String messageBan = driver.switchTo().alert().getText();
        assertEquals("Bannato: test_user", messageBan);

        driver.switchTo().alert().accept();

        driver.get("http://localhost:8080/logout");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        driver.get("http://localhost:8080/");

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButton1 = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("test_user");
        passwordField.sendKeys("pass");

        submitButton1.click();

        String message = driver.switchTo().alert().getText();
        assertEquals("Could not find that User", message);
    }

}
