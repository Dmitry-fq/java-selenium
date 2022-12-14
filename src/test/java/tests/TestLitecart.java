package tests;

import actions.Actions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.time.Duration;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestLitecart extends Actions {

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

    public void goToCreateAccount() {
        driver.findElement(By.cssSelector("[name=login_form] a")).click();
    }

    public void doLogout() {
        driver.findElement(By.cssSelector(".list-vertical li:nth-child(4) a")).click();
    }

    public void goToAdminMenuItem(int index) {
        List<WebElement> menu = driver.findElements(By.id("app-"));
        menu.get(index).click();
    }

    public void openNewWindow(String originalWindow, WebElement link) {
        Set<String> existingWindows = driver.getWindowHandles();
        link.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String newWindow = wait.until(anyWindowOtherThan(existingWindows));
        driver.switchTo().window(newWindow);
        driver.close();
        driver.switchTo().window(originalWindow);
    }


    @Test
    public void testAdminMenu() {
        this.goToAdminPanel();
        this.adminLogin();

        List<WebElement> menu = driver.findElements(By.id("app-"));
        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).click();
            Assertions.assertTrue(h1Present());
            //TODO ???????????? ???????????????????????? ???????? ?? menu ?? submenu
            menu = driver.findElements(By.id("app-"));
            List<WebElement> submenu = menu.get(i).findElements(By.tagName("li"));
            for (int j = 0; j < submenu.size(); j++) {
                submenu.get(j).click();
                menu = driver.findElements(By.id("app-"));
                submenu = menu.get(i).findElements(By.tagName("li"));
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

                driver.navigate().back();
                rowsCountries = driver.findElements(By.className("row"));
            }
        }
        ArrayList<String> listNamesSorted = new ArrayList<>(listNames);
        Collections.sort(listNamesSorted);

        Assertions.assertEquals(listNames, listNamesSorted);
    }

    @Test
    public void testRegistration() {
        this.goToMain();
        this.goToCreateAccount();

        driver.findElement(By.cssSelector("[name=firstname]")).sendKeys("Test");
        driver.findElement(By.cssSelector("[name=lastname]")).sendKeys("Test");
        driver.findElement(By.cssSelector("[name=address1]")).sendKeys("Test");
        driver.findElement(By.cssSelector("[name=postcode]")).sendKeys("12345");
        driver.findElement(By.cssSelector("[name=city]")).sendKeys("Test");
        WebElement selector = driver.findElement(By.cssSelector("[name=country_code]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = 224; arguments[0].dispatchEvent(new Event('change'))", selector);
        String email = Double.toString((int) (Math.random() * 100000)) + "@gmail.com";
        driver.findElement(By.cssSelector("[name=email]")).sendKeys(email);
        driver.findElement(By.cssSelector("[name=phone]")).sendKeys("+123");
        String password = "123";
        driver.findElement(By.cssSelector("[name=password]")).sendKeys(password);
        driver.findElement(By.cssSelector("[name=confirmed_password]")).sendKeys(password);
        driver.findElement(By.cssSelector("[name=create_account]")).click();

        this.doLogout();
        driver.findElement(By.cssSelector("[name=email]")).sendKeys(email);
        driver.findElement(By.cssSelector("[name=password]")).sendKeys(password);
        driver.findElement(By.cssSelector("[name=login]")).click();
        this.doLogout();
    }

    @Test
    public void testAddNewProduct() {
        this.goToAdminPanel();
        this.adminLogin();
        this.goToAdminMenuItem(1);

        driver.findElement(By.cssSelector("#content div a:nth-child(2)")).click();
        driver.findElement(By.cssSelector("[name=status][value='1']")).click();
        driver.findElement(By.cssSelector("[name='name[en]']")).sendKeys("Test");
        String productName = driver.findElement(By.cssSelector("[name='name[en]'")).getAttribute("value");
        driver.findElement(By.cssSelector("[name=code]")).sendKeys("Test");
        driver.findElement(By.cssSelector("[data-name='Rubber Ducks']")).click();
        Select defaultCategory = new Select(driver.findElement(By.cssSelector("[name=default_category_id]")));
        defaultCategory.selectByIndex(1);
        driver.findElement(By.cssSelector("[value='1-1']")).click();
        driver.findElement(By.cssSelector("[name=quantity]")).clear();
        driver.findElement(By.cssSelector("[name=quantity]")).sendKeys("1");
        String path = new File("files/wat-duck.jpg").getAbsolutePath();
        driver.findElement(By.cssSelector("[name='new_images[]']")).sendKeys(path);
        driver.findElement(By.cssSelector("[name='new_images[]']")).sendKeys(path);
        driver.findElement(By.cssSelector("[name=date_valid_from]")).sendKeys("20102022");
        driver.findElement(By.cssSelector("[name=date_valid_to]")).sendKeys("20102023");

        driver.findElement(By.cssSelector(".index li:nth-child(2)")).click();
        Select manufacturer_id = new Select(driver.findElement(By.cssSelector("[name=manufacturer_id]")));
        manufacturer_id.selectByIndex(1);
        driver.findElement(By.cssSelector("[name=keywords]")).sendKeys("duck");
        driver.findElement(By.cssSelector("[name='short_description[en]']")).sendKeys("duck");
        driver.findElement(By.className("trumbowyg-editor")).sendKeys("duck");
        driver.findElement(By.cssSelector("[name='head_title[en]']")).sendKeys("duck");
        driver.findElement(By.cssSelector("[name='meta_description[en]']")).sendKeys("duck");

        driver.findElement(By.cssSelector(".index li:nth-child(4)")).click();
        driver.findElement(By.cssSelector("[name=purchase_price]")).clear();
        driver.findElement(By.cssSelector("[name=purchase_price]")).sendKeys("13");
        Select currency_code = new Select(driver.findElement(By.cssSelector("[name=purchase_price_currency_code]")));
        currency_code.selectByIndex(1);
        driver.findElement(By.cssSelector("[name='prices[USD]']")).sendKeys("1");
        driver.findElement(By.cssSelector("[name='prices[EUR]']")).sendKeys("1");
        driver.findElement(By.cssSelector("[name=save]")).click();

        List<WebElement> catalogList = driver.findElements(By.cssSelector(".dataTable tbody tr"));
        WebElement product = catalogList.get(catalogList.size() - 2);
        Assertions.assertEquals(productName, product.getText());

        product.findElement(By.cssSelector("[type=checkbox]")).click();
        driver.findElement(By.cssSelector("[name=delete]")).click();
        driver.switchTo().alert().accept();
    }

    @Test
    public void testAddProductToCart() {
        this.openMain();
        this.addProductToCart(3);
        this.goToCart();
        this.removeAllProductsFromCart();

    }

    @Test
    public void testLinkInNewWindow() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        this.adminLogin();
        driver.findElement(By.cssSelector("a.button")).click();

        List<WebElement> externalLinks = driver.findElements(By.cssSelector(".fa.fa-external-link"));
        for (WebElement link: externalLinks) {
            String windowAddNewCountry = driver.getWindowHandle();
            this.openNewWindow(windowAddNewCountry, link);
        }
    }

    @Test
    public void testLogProducts() {
        driver.get("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");
        this.adminLogin();

        List<WebElement> rows = driver.findElements(By.cssSelector(".dataTable tr.row"));
        for (int i = 1; i < rows.size(); i++) {
            rows = driver.findElements(By.cssSelector(".dataTable tr.row"));
            WebElement input = rows.get(i).findElement(By.cssSelector("td input"));
            if (input.getAttribute("name").contains("products")) {
            rows.get(i).findElement(By.cssSelector("td a")).click();
            System.out.println(driver.manage().logs().get("browser"));
            driver.navigate().back();
            }
        }
    }
}