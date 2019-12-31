package com.webapp.systemtest; // Generated by Selenium IDE

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.webapp.config.MvcConfig;
import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Juntao Peng
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginSignUpST {

    JavascriptExecutor js;
    private WebDriver driver;
    private Map<String, Object> vars;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
        User user1 = new User("usernameusername1", "passwordpassword1", "name1", "MALE", "13000000000");
        User user2 = new User("usernameusername2", "passwordpassword2", "name2", "FEMALE", "13000000000");
        User user3 = new User("usernameusername3", "passwordpassword3", "name3", "MALE", "13000000000");
        User user4 = new User("usernameusername4", "passwordpassword4", "name4", "FEMALE", "13000000000");
        User user5 = new User("usernameusername5", "passwordpassword5", "name5", "MALE", "13000000000");
        User user6 = new User("username6", "password1212345", "name5", "MALE", "13000000000");
        this.userDao.addUser(user1);
        this.userDao.addUser(user2);
        this.userDao.addUser(user3);
        this.userDao.addUser(user4);
        this.userDao.addUser(user5);
        this.userDao.addUser(user6);
        Admin admin1 = new Admin("admin1", "password1", "name1", "MALE", "tel1");
        Admin admin2 = new Admin("admin2", "password2", "name2", "MALE", "tel2");
        Admin admin3 = new Admin("admin3", "password3", "name3", "MALE", "tel3");
        this.userDao.addUser(admin1);
        this.userDao.addUser(admin2);
        this.userDao.addUser(admin3);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        for (User user : this.userDao.queryAllUsers()) {
            this.userDao.deleteUser(user.getId());
        }
    }

    static Stream<Arguments> correctUserProvider() {
        return Stream.of(
                Arguments.of("usernameusername1", "passwordpassword1"),
                Arguments.of("usernameusername2", "passwordpassword2"),
                Arguments.of("usernameusername3", "passwordpassword3"),
                Arguments.of("usernameusername4", "passwordpassword4"),
                Arguments.of("usernameusername5", "passwordpassword5")
        );
    }

    static Stream<Arguments> wrongUserProvider() {
        return Stream.of(
                Arguments.of("", "", "用户名不能为空"),
                Arguments.of("", "passwordpassword1", "用户名不能为空"),
                Arguments.of("usernameusername1", "", "密码不能为空"),
                Arguments.of("usernameusername1-", "passwordpassword1", "用户名或密码错误"),
                Arguments.of("usernameusername2", "passwordpassword2-", "用户名或密码错误"),
                Arguments.of("username3", "passwordpassword3", "用户名或密码错误"),
                Arguments.of("usernameusername4", "password4", "用户名或密码错误")
        );
    }

    static Stream<Arguments> correctAdminProvider() {
        return Stream.of(
                Arguments.of("admin1", "password1"),
                Arguments.of("admin2", "password2"),
                Arguments.of("admin3", "password3")
        );
    }

    @ParameterizedTest
    @MethodSource("wrongUserProvider")
    @Order(1)
    public void loginFail(String username, String password, String error) {
        // Test name: LoginFail
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/ |  |
        driver.get("http://localhost:8080/MeetHere_war/");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1920, 1080));
        // 3 | click | id=username |  |
        driver.findElement(By.id("username")).click();
        // 4 | type | id=username | 123 |
        driver.findElement(By.id("username")).sendKeys(username);
        // 5 | type | id=password | 123 |
        driver.findElement(By.id("password")).sendKeys(password);
        // 6 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        assertEquals(error, driver.findElement(By.id("error")).getText());
    }

    @ParameterizedTest
    @MethodSource("correctAdminProvider")
    @Order(2)
    public void loginSucceedAdmin(String username, String password) {
        // Test name: LoginSucceedAdmin
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/ |  |
        driver.get("http://localhost:8080/MeetHere_war/");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1920, 1080));
        // 3 | click | id=username |  |
        driver.findElement(By.id("username")).click();
        // 4 | type | id=username | root |
        driver.findElement(By.id("username")).sendKeys(username);
        // 5 | type | id=password | root |
        driver.findElement(By.id("password")).sendKeys(password);
        // 6 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        assertEquals("欢迎您,系统管理员!", driver.findElement(By.id("welcome")).getText());
    }

    @ParameterizedTest
    @MethodSource("correctUserProvider")
    @Order(3)
    public void loginSucceedUser(String username, String password) {
        // Test name: LoginSucceedUser
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/ |  |
        driver.get("http://localhost:8080/MeetHere_war/");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1920, 1080));
        // 3 | click | id=username |  |
        driver.findElement(By.id("username")).click();
        // 4 | type | id=username | pjt |
        driver.findElement(By.id("username")).sendKeys(username);
        // 5 | type | id=password | pjt |
        driver.findElement(By.id("password")).sendKeys(password);
        // 6 | sendKeys | id=password | ${KEY_ENTER} |
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        assertEquals("欢迎您,朋友!", driver.findElement(By.id("welcome")).getText());
    }

    static Stream<Arguments> correctSignupProvider() {
        return Stream.of(
                Arguments.of("12345", "1234567890", "name1", "MALE", "13000000000"),
                Arguments.of("123451234512345", "123456789012345", "name1", "MALE", "13000000000"),
                Arguments.of("1234512345", "123456789012", "name1", "MALE", "13000000000")
        );
    }

    @ParameterizedTest
    @MethodSource("correctSignupProvider")
    @Order(4)
    public void correctSignup(String username, String password, String name, String sex, String tel) {
        // Test name: SignUp
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/ |  |
        driver.get("http://localhost:8080/MeetHere_war/");
        // 2 | setWindowSize | 1054x556 |  |
        driver.manage().window().setSize(new Dimension(1054, 556));
        // 3 | click | css=.btn:nth-child(3) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(3)")).click();
        // 4 | click | id=username |  |
        driver.findElement(By.id("username")).click();
        // 5 | type | id=username | 123 |
        driver.findElement(By.id("username")).sendKeys(username);
        // 6 | type | id=name | 123 |
        driver.findElement(By.id("name")).sendKeys(name);
        // 7 | type | id=password | 123 |
        driver.findElement(By.id("password")).sendKeys(password);
        // 8 | click | css=.radio:nth-child(6) |  |
        if ("FEMALE".equals(sex)) {
            driver.findElement(By.id("sex_female")).click();
        } else if ("MALE".equals(sex)) {
            driver.findElement(By.id("sex_male")).click();
        }
        // 9 | click | id=tel |  |
        driver.findElement(By.id("tel")).click();
        // 10 | type | id=tel | 123 |
        driver.findElement(By.id("tel")).sendKeys(tel);
        // 11 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        assertEquals(username, driver.findElement(By.id("username")).getAttribute("value"));
        assertEquals(password, driver.findElement(By.id("password")).getAttribute("value"));
    }

    static Stream<Arguments> failSignupProvider() {
        return Stream.of(
                Arguments.of("", "1234567890", "name1", "MALE", "13000000000", "用户名不能为空"),
                Arguments.of("1", "1234567890", "name1", "MALE", "13000000000", "用户名必须在5-15个字符之间"),
                Arguments.of("4", "1234567890", "name1", "MALE", "13000000000", "用户名必须在5-15个字符之间"),
                Arguments.of("1234567890123456", "1234567890", "name1", "MALE", "13000000000", "用户名必须在5-15个字符之间"),
                Arguments.of("12345678901234567890", "1234567890", "name1", "MALE", "13000000000", "用户名必须在5-15个字符之间"),
                Arguments.of("1 34567890", "1234567890", "name1", "MALE", "13000000000", "用户名不能含有空格"),
                Arguments.of("1234567890", "", "name1", "MALE", "13000000000", "密码不能为空"),
                Arguments.of("1234567890", "1", "name1", "MALE", "13000000000", "密码必须在10-15个字符之间"),
                Arguments.of("1234567890", "123456789", "name1", "MALE", "13000000000", "密码必须在10-15个字符之间"),
                Arguments.of("1234567890", "1234567890123456", "name1", "MALE", "13000000000", "密码必须在10-15个字符之间"),
                Arguments.of("1234567890", "12345678901234567890", "name1", "MALE", "13000000000", "密码必须在10-15个字符之间"),
                Arguments.of("1234512345", "1 34567890", "name1", "MALE", "13000000000", "密码不能含有空格"),
                Arguments.of("1234512345", "1234567890", "", "MALE", "13000000000", "姓名不能为空"),
                Arguments.of("1234512345", "1234567890", "name1", "MALE", "", "电话不能为空"),
                Arguments.of("1234512345", "1234567890", "name1", "MALE", "22345678901", "电话必须为以1起始的11位数字"),
                Arguments.of("1234512345", "1234567890", "name1", "MALE", "1234567890", "电话必须为以1起始的11位数字"),
                Arguments.of("1234512345", "1234567890", "name1", "MALE", "123456789012", "电话必须为以1起始的11位数字"),
                Arguments.of("1234567890", "1234567890", "name1", "MALE", "12345678901", "用户名和密码不能相同"),
                Arguments.of("username6", "password1212345", "name5", "MALE", "13000000000", "该用户名已存在")
        );

    }

    @ParameterizedTest
    @MethodSource("failSignupProvider")
    @Order(5)
    public void failSignup(String username, String password, String name, String sex, String tel, String expectedErrorMessage) {
        // Test name: SignUp
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/ |  |
        driver.get("http://localhost:8080/MeetHere_war/");
        // 2 | setWindowSize | 1054x556 |  |
        driver.manage().window().setSize(new Dimension(1054, 556));
        // 3 | click | css=.btn:nth-child(3) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(3)")).click();
        // 4 | click | id=username |  |
        driver.findElement(By.id("username")).click();
        // 5 | type | id=username | 123 |
        driver.findElement(By.id("username")).sendKeys(username);
        // 6 | type | id=name | 123 |
        driver.findElement(By.id("name")).sendKeys(name);
        // 7 | type | id=password | 123 |
        driver.findElement(By.id("password")).sendKeys(password);
        // 8 | click | css=.radio:nth-child(6) |  |
        if ("FEMALE".equals(sex)) {
            driver.findElement(By.id("sex_female")).click();
        } else if ("MALE".equals(sex)) {
            driver.findElement(By.id("sex_male")).click();
        }
        // 9 | click | id=tel |  |
        driver.findElement(By.id("tel")).click();
        // 10 | type | id=tel | 123 |
        driver.findElement(By.id("tel")).sendKeys(tel);
        // 11 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        assertEquals(expectedErrorMessage, driver.findElement(By.id("error")).getText());
    }
}
