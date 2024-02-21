package org.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
public class Parser {
    private DataBase dataBase = new DataBase();
    private List<String> logRating = new ArrayList();

    public List<String> getLogRating() {
        return this.logRating;
    }

    public Parser() throws SQLException {
    }

    public boolean parsing(long chatId) throws SQLException {
        System.setProperty("webdriver.edge.driver", "C:\\msedgedriver.exe");
        WebDriver driver = new EdgeDriver();
        driver.get("https://poo.susu.ru/");
        WebElement usernameInput = driver.findElement(By.cssSelector("input[placeholder='Логин']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[placeholder='Пароль']"));
        usernameInput.sendKeys(new CharSequence[]{this.dataBase.outputLogin(chatId)});
        passwordInput.sendKeys(new CharSequence[]{this.dataBase.outputPassword(chatId)});

        WebElement tdElement;
        try {
            timeSkip(2000);
            WebElement loginButton = driver.findElement(By.cssSelector(".button-login-title"));
            loginButton.click();
            timeSkip(2000);
            WebElement con = driver.findElement(By.cssSelector(".btn.btn-default[title='Продолжить']"));
            con.click();
            timeSkip(2000);
            tdElement = driver.findElement(By.id("11"));
            tdElement.click();
            timeSkip(2000);
            WebElement button = driver.findElement(By.cssSelector("button[title='Сформировать']"));
            button.click();
            timeSkip(2000);
        } catch (NoSuchElementException var10) {
            return false;
        }

        List<WebElement> tdElements = driver.findElements(By.tagName("tr"));
        Iterator var12 = tdElements.iterator();

        while(var12.hasNext()) {
            tdElement = (WebElement)var12.next();
            this.logRating.add(tdElement.getText());
        }

        return true;
    }

    private static void timeSkip(int milSec) {
        try {
            Thread.sleep((long)milSec);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

    }
}
