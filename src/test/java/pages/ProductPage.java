package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage extends Page {

    @FindBy(name = "options[Size]")
    public WebElement sizeSelector;

    @FindBy(name = "add_cart_product")
    public WebElement btnAddToCart;

    public ProductPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

}
