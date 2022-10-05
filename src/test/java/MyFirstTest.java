import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

public class MyFirstTest extends TestBase {

    public void goToAdminPanel() {
        driver.get("http://localhost/litecart/admin/");
    }

    public void adminLogin() {
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @Test
    public void myFirstTest() {
        this.start();
        this.goToAdminPanel();
        this.adminLogin();
        this.stop();
    }
}