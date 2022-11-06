package actions;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;

import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;

public class Actions {

    public WebDriver driver;
    public WebDriverWait wait;

    private MainPage mainPage;
    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeEach
    public void start() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        mainPage = new MainPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
    }

//    @AfterEach
    public void stop() {
        driver.quit();
        driver = null;
    }

    protected boolean isElementPresent(WebDriver driver, By locator) {
        return driver.findElements(locator).size() > 0;
    }

    public ExpectedCondition<String> anyWindowOtherThan(Set<String> oldWindows) {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver driver) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(oldWindows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }

    public void openMain() {
        driver.get("http://localhost/litecart/en/");
    }

    public void addProductToCart(int quantity) {
        for (int i = 1; i <= quantity; i++) {
            mainPage.product.click();
            if (isElementPresent(driver, By.cssSelector("[name='options[Size]']"))) {
                Select size = new Select(productPage.sizeSelector);
                size.selectByIndex(1);
            }
            productPage.btnAddToCart.click();
            wait.until(attributeContains(mainPage.cartQuantity,"innerText", Integer.toString(i)));
            driver.navigate().back();
        }
    }

    public void removeAllProductsFromCart() {
        while (this.isElementPresent(driver, By.cssSelector(".shortcut"))) {
            cartPage.shortcut.click();
            cartPage.btnRemove.click();
            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.cssSelector(".dataTable tr td.item"), cartPage.products.size()));
        }
        if (this.isElementPresent(driver, By.cssSelector("[name=remove_cart_item]"))) {
            cartPage.btnRemove.click();
        }
    }

    public void goToCart() {
        mainPage.cart.click();
    }
}