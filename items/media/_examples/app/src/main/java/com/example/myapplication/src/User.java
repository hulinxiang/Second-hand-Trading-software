package com.example.myapplication.src;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is used to manage user information.
 *
 * Responsibilities:
 * - Store user information such as username, email, password, contact info, etc.
 * - Manage the user's own posts, liked posts, and purchased posts.
 */

public class User {
    private static int nextUserIndex = 4000;  // 初始值设置为4000

    private String userId;  // UUID only used internally
    private String email;   // Username
    private String passwordHash;
    private String name;
    private String address;
    private String phone;
    private PostList postList;  // Add PostList attribute
    private String userType;    // "0": Admin, "1": Normal user
    private String userIndexInFirebase;
    private List<Post> ownPosts;
    private List<Post> likePosts;
    private List<Post> buyPosts;

    // Constructor with only email and password
    public User(String email, String password) {
        this.userId = UUID.randomUUID().toString(); // Generate a unique user ID using UUID
        this.email = email;
        this.passwordHash = password;
        this.userType = "1";
        this.userIndexInFirebase = generateNextUserIndex();  // Assign the next user index
        this.ownPosts = new ArrayList<>();
        this.likePosts = new ArrayList<>();
        this.buyPosts = new ArrayList<>();
    }

    // Constructor used for direct import from Firebase (userId is not autogenerated)
    public User(String userId, String email, String password, String name, String address, String phone, String userType, String userIndexInFirebase) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = password;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.userType = userType;
        this.userIndexInFirebase = userIndexInFirebase;   // Assign the next user index
        this.ownPosts = new ArrayList<>();
        this.likePosts = new ArrayList<>();
        this.buyPosts = new ArrayList<>();
    }

    // Generate and return the next user index
    public static synchronized String generateNextUserIndex() {
        return String.valueOf(nextUserIndex++);  // Increment and return the next index
    }

    // Hash password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    // Update password
    public void updatePassword(String newPassword) {
        this.passwordHash = hashPassword(newPassword);
    }

    // Update name
    public void updateName(String newName) {
        this.name = newName;
    }

    // Update address
    public void updateAddress(String newAddress) {
        this.address = newAddress;
    }

    // Update phone number
    public void updatePhone(String newPhone) {
        this.phone = newPhone;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Get user's PostList
    public PostList getPostList() {
        return postList;
    }

    // Set user's PostList
    public void setPostList(PostList postList) {
        this.postList = postList;
    }

    // Add a post to the user's own posts
    public void addOwnPost(Post post) {
        if (post != null) {
            this.ownPosts.add(post);
        }
    }

    // Delete a post from the user's own posts
    public void removeOwnPost(Post post) {
       this.ownPosts.remove(post);
    }

    public void updateLikes(Post post){
        if(!likePosts.contains(post)) {
            this.likePosts.add(post);
        }
        //this.likePosts.add(post);
    }

    public void removeLikes(Post post) {
        this.likePosts.remove(post);
    }

    public void updateBuys(Post post){
        if(!buyPosts.contains(post)) {
            this.buyPosts.add(post);
        }
    }

    public void updateOwns(Post post){
        this.ownPosts.add(post);
    }

    // Get all posts owned by the user
    public List<Post> getOwnPosts() {
        return this.ownPosts;
    }

    // Get all posts liked by the user
    public List<Post> getLikePosts() {
        return this.likePosts;
    }

    // Get all posts purchased by the user
    public List<Post> getBuyPosts() {
        return this.buyPosts;
    }

    public static int getNextUserIndex() {
        return nextUserIndex;
    }

    public String getUserIndexInFirebase() {
        return userIndexInFirebase;
    }

    public void setUserIndexInFirebase(String userIndexInFirebase) {
        this.userIndexInFirebase = userIndexInFirebase;
    }

    public String getUserType() {
        return userType;
    }

    public void setBuyPosts(List<Post> buyPosts) {
        this.buyPosts = buyPosts;
    }

    public void setLikePosts(List<Post> likePosts) {
        this.likePosts = likePosts;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setOwnPosts(List<Post> ownPosts) {
        this.ownPosts = ownPosts;
    }
}
