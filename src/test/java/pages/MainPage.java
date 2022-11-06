package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends Page {

    @FindBy(className = "product")
    public WebElement product;

    @FindBy(id = "cart")
    public WebElement cart;

    @FindBy(css = "#cart .quantity")
    public WebElement cartQuantity;

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
