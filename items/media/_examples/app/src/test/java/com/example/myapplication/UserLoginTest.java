package com.example.myapplication;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.myapplication.BPlusTree.User.BPlusTreeManagerUser;
import com.example.myapplication.activity.loginUsingBPlusTree.LoginCheckService;
import com.example.myapplication.src.User;
import org.junit.After;
import org.junit.Before;

/**
 * This class contains unit tests for user login functionality.
 *
 * @author Yingxuan Tang u7670526
 */
public class UserLoginTest {

    private LoginCheckService loginCheckService;

    @Before
    public void setUp() {
        loginCheckService = new LoginCheckService(null);
        initTestData();
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    /**
     * Initialize test data by creating and inserting test users into the BPlusTree.
     */
    private void initTestData() {
        // 创建测试用户对象
        User user1 = new User("testuser1", "testtest1@anu.edu.au", User.hashPassword("comp6442"), "Test User 1", "Test Address 1", "1234567890", "userType1", "9989");
        User user2 = new User("testuser2", "testtest2@anu.edu.au", User.hashPassword("comp2100"), "Test User 2", "Test Address 2", "0987654321", "userType2", "7678");

        // 将测试用户对象插入到 BPlusTree 中
        BPlusTreeManagerUser.getTreeInstance(null).insert(user1.getEmail(), user1);
        BPlusTreeManagerUser.getTreeInstance(null).insert(user2.getEmail(), user2);
    }

    /**
     * Clear the test data by removing the test users from the BPlusTree.
     */
    private void clearTestData() {
        // 删除测试用户
        BPlusTreeManagerUser.getTreeInstance(null).remove("testtest1@anu.edu.au");
        BPlusTreeManagerUser.getTreeInstance(null).remove("testtest2@anu.edu.au");
    }

    /**
     * Test for successful login of user 1.
     */
    @Test
    public void testLoginSuccessUser1() {
        String username = "comp6442@anu.edu.au";
        String password = "comp6442";

        boolean result = loginCheckService.loginCheck(username, password);

        assertTrue(result);
    }

    /**
     * Test for successful login of user 2.
     */
    @Test
    public void testLoginSuccessUser2() {
        String username = "comp2100@anu.edu.au";
        String password = "comp2100";

        boolean result = loginCheckService.loginCheck(username, password);

        assertTrue(result);
    }

    /**
     * Test for login failure with wrong password.
     */
    @Test
    public void testLoginFailureWrongPassword() {
        String username = "comp6442@anu.edu.au";
        String password = "wrongpassword";

        boolean result = loginCheckService.loginCheck(username, password);

        assertFalse(result);
    }

    /**
     * Test for login failure when user is not found.
     */
    @Test
    public void testLoginFailureUserNotFound() {
        String username = "nonexistentuser@anu.edu.au";
        String password = "password123";

        boolean result = loginCheckService.loginCheck(username, password);

        assertFalse(result);
    }
}
