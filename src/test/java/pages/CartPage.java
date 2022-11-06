package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CartPage extends Page {

    @FindBy(css = ".dataTable tr td.item")
    public List<WebElement> products;

    @FindBy(id = "cart")
    public WebElement cart;

    @FindBy(className = "shortcut")
    public WebElement shortcut;

    @FindBy(name = "remove_cart_item")
    public WebElement btnRemove;

    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
