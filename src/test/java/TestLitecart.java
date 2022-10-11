import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class TestLitecart extends TestBase {

    public void goToAdminPanel() {
        driver.get("http://localhost/litecart/admin/");
    }

    public void adminLogin() {
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    public boolean h1Present() {
        boolean present = driver.findElement(By.tagName("h1")).isDisplayed();
        return present;
    }

    public void goToMain() {
        driver.get("http://localhost/litecart/en/");
    }


    @Test
    public void myFirstTest() {
        this.goToAdminPanel();
        this.adminLogin();

        List<WebElement> menu = driver.findElements(By.id("app-"));
        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).click();
            System.out.println(h1Present());
            Assertions.assertTrue(h1Present());
            //TODO убрать дублирование кода в menu и submenu
            menu = driver.findElements(By.id("app-"));
            List<WebElement> submenu = menu.get(i).findElements(By.tagName("li"));
            for (int j = 0; j < submenu.size(); j++) {
                submenu.get(j).click();
                menu = driver.findElements(By.id("app-"));
                submenu = menu.get(i).findElements(By.tagName("li"));
                System.out.println(h1Present());
                Assertions.assertTrue(h1Present());
            }
            menu = driver.findElements(By.id("app-"));
        }
    }


    @Test
    public void testStickersDisplayed() {
        this.goToMain();

        List<WebElement> products = driver.findElements(By.className("product"));
        for (int i = 0; i < products.size(); i++) {
            List<WebElement> stickers = products.get(i).findElements(By.className("sticker"));
            Assertions.assertEquals(stickers.size(), 1);
            System.out.println(stickers.size());
        }
    }
}