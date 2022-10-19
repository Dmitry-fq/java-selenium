import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        return driver.findElement(By.tagName("h1")).isDisplayed();
    }

    public void goToMain() {
        driver.get("http://localhost/litecart/en/");
    }

    public void goToAdminCountries() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
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
        for (WebElement el : products) {
            List<WebElement> stickers = el.findElements(By.className("sticker"));
            Assertions.assertEquals(stickers.size(), 1);
        }
    }

    @Test
    public void testSortCountriesAndZones() {
        this.goToAdminCountries();
        this.adminLogin();

        int rowsSize = driver.findElements(By.className("row")).size();
        ArrayList<String> listNames = new ArrayList<String>();
        List<WebElement> rowsCountries = driver.findElements(By.className("row"));
        for (int i = 0; i < rowsSize; i++) {
            WebElement rowCountry = rowsCountries.get(i);
            WebElement name = rowCountry.findElement(By.tagName("a"));
            listNames.add(name.getText());
            String country = name.getText(); // Удалить после дебага

            WebElement zone = rowCountry.findElement(By.cssSelector("td:nth-child(6)"));
            if (!Objects.equals(zone.getText(), "0")) {
                name.click();
                ArrayList<String> listZoneNames = new ArrayList<String>();
                WebElement tableZones = driver.findElement(By.id("table-zones"));
                List<WebElement> rowZones = tableZones.findElements(By.tagName("tr"));
                rowZones.remove(0);
                rowZones.remove(rowZones.size() - 1);
                for (WebElement rowZone: rowZones) {
                    WebElement zoneName = rowZone.findElement(By.cssSelector("td:nth-child(3)"));
                    listZoneNames.add(zoneName.getAttribute("textContent"));
                }
                ArrayList<String> listZoneNamesSorted = new ArrayList<String>(listZoneNames);
                Collections.sort(listZoneNamesSorted);

                Assertions.assertEquals(listZoneNames, listZoneNamesSorted);

                System.out.println("Зоны " + country + " проверены"); // Удалить после дебага
                driver.navigate().back();
                rowsCountries = driver.findElements(By.className("row"));
            }
        }
        ArrayList<String> listNamesSorted = new ArrayList<>(listNames);
        Collections.sort(listNamesSorted);

        Assertions.assertEquals(listNames, listNamesSorted);
    }
}