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

    @Test
    public void myFirstTest() {
        this.start();
        this.goToAdminPanel();
        this.adminLogin();

        List<WebElement> menu = driver.findElements(By.id("app-"));
        for (int i = 0; i < menu.size(); i++) {
            System.out.println("Цикл:" + i);
            System.out.println("Menu:" + menu.get(i).getText());

            menu.get(i).click();
            System.out.println(h1Present());
            Assertions.assertTrue(h1Present());
            //TODO убрать дублирование кода в menu и submenu
            menu = driver.findElements(By.id("app-"));
            List<WebElement> submenu = menu.get(i).findElements(By.tagName("li"));
            System.out.println("Размер submenu:" + submenu.size());
            for (int j = 0; j < submenu.size(); j++) {
                submenu.get(j).click();
                menu = driver.findElements(By.id("app-"));
                submenu = menu.get(i).findElements(By.tagName("li"));
                System.out.println(h1Present());
                Assertions.assertTrue(h1Present());
                System.out.println("submenu " + (j + 1));
            }
            driver.navigate().back();
            menu = driver.findElements(By.id("app-"));
            System.out.println("_______________________");
        }
//        this.stop();
    }
}