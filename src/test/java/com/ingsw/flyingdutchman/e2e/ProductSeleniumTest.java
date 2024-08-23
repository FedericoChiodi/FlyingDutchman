package com.ingsw.flyingdutchman.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Tag("e2e")
public class ProductSeleniumTest {

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
    public void testInserisciProdotto() {
        driver.get("http://localhost:8080/");

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("Mario12");
        passwordField.sendKeys("pass");

        submitButton.click();

        driver.get("http://localhost:8080/productManagement/insertView");

        WebElement description = driver.findElement(By.id("description"));
        WebElement min_price = driver.findElement(By.id("min_price"));
        WebElement starting_price = driver.findElement(By.id("starting_price"));
        WebElement image = driver.findElement(By.cssSelector("input[type=file]"));
        WebElement submitButtonProduct = driver.findElement(By.cssSelector("input[type='submit']"));

        description.sendKeys("test_description");
        min_price.sendKeys("5");
        starting_price.sendKeys("15");
        image.sendKeys("/home/sanpc/uploads/default.png");

        submitButtonProduct.click();

        String expectedUrl = "http://localhost:8080/Upload";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        WebElement product = driver.findElement(By.id("current_price"));
        assertEquals("€15.00", product.getText());
    }

}
