package com.ingsw.flyingdutchman.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Disabled
public class AuctionSeleniumTest {

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
    public void testAuction_GenericConversation() {
        driver.get("http://localhost:8080/");

        WebElement usernameField = driver.findElement(By.id("usernameLogin"));
        WebElement passwordField = driver.findElement(By.id("passwordLogin"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

        usernameField.sendKeys("Mario12");
        passwordField.sendKeys("pass");

        submitButton.click();

        driver.get("http://localhost:8080/auctionManagement/view");

        WebElement auctionButton = driver.findElement(By.id("auctionButton"));
        auctionButton.click();

        WebElement backButton = driver.findElement(By.id("backButton"));
        backButton.click();

        WebElement viewMyAuctionsButton = driver.findElement(By.id("viewMyAuctionsButton"));
        viewMyAuctionsButton.click();

        WebElement viewAuctionsButton = driver.findElement(By.id("viewAuctionsButton"));
        viewAuctionsButton.click();

        WebElement searchCategoryButton = driver.findElement(By.id("searchCategoryButton"));
        searchCategoryButton.click();

        WebElement searchText = driver.findElement(By.id("auctionName"));
        searchText.sendKeys("Racchetta da Ping Pong");
        WebElement searchButton = driver.findElement(By.id("searchButton"));
        searchButton.click();

        WebElement productDescription = driver.findElement(By.id("productDescription"));
        String productDescriptionText = productDescription.getText();

        assertEquals("Racchetta da ping pong", productDescriptionText);
    }

    @Test
    public void testInsertAuction() {
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

        driver.get("http://localhost:8080/auctionManagement/insert");

        WebElement submitButtonAuction = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButtonAuction.click();

        String message = driver.switchTo().alert().getText();
        assertEquals("Asta creata correttamente!", message);

        driver.switchTo().alert().accept();

        WebElement viewMyAuctionsButton = driver.findElement(By.id("viewMyAuctionsButton"));
        viewMyAuctionsButton.click();

        WebElement productPrice = driver.findElement(By.id("productPrice"));
        assertEquals("€15.00", productPrice.getText());
    }

}
