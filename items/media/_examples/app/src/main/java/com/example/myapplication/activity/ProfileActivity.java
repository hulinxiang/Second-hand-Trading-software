package com.example.myapplication.activity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myapplication.R;
import com.example.myapplication.activity.Image.GlideImageLoader;
import com.example.myapplication.activity.loginUsingBPlusTree.LoginActivityBPlusTree;
import com.example.myapplication.src.Post;
import com.example.myapplication.src.SessionManager;
import com.example.myapplication.src.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wenhui Shi u773637, Jin Yang u7724192
 * The ProfileActivity class is an activity that displays the user's profile information and provides various functionalities related to the user's posts, likes, and purchases.
 * It includes a navigation drawer for accessing different sections of the app and implements event handlers for interacting with different views and buttons.
 * The activity dynamically updates and displays the user's posts, likes, and purchases using grid layouts and allows the user to navigate to detailed views of each item.
 */
public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Toolbar toolbar;
    private LinearLayout home;
    private LinearLayout search;
    private LinearLayout create;
    private LinearLayout inbox;
    private LinearLayout profile;
    private TextView textName;
    private TextView textEmail;
    private Button editProfileButton;
    private LinearLayout postsContainer, likesContainer, buyContainer;
    private TextView postsButton, likesButton, buyButton;
    private GridLayout postsGrid, likesGrid, buyGrid;

    /**
     * Called when the activity is first created. Initializes the activity's UI components,
     * sets up the navigation drawer, toolbar, and handles various button click listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

        setupNavigationDrawer();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // User profile display
        User currentUser = SessionManager.getInstance().getUser();
        if (currentUser != null) {
            textName.setText(currentUser.getName());
            textEmail.setText(currentUser.getEmail());
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Add click listener for edit profile button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set click listeners
        postsButton.setOnClickListener(v -> updateViews("posts"));
        likesButton.setOnClickListener(v -> updateViews("likes"));
        buyButton.setOnClickListener(v -> updateViews("buy"));

        // Initial display setup for posts
        updateViews("posts");

        // Initially update button texts
        updateButtonCounts();
    }

    /**
     * Sets up the navigation drawer by initializing the drawer layout and navigation view,
     * and setting the listener for navigation item selection.
     */
    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Intent intent;

                switch (id) {
                    case R.id.nav_home:
                        intent = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_account:
                        intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        intent = new Intent(ProfileActivity.this, LoginActivityBPlusTree.class);
                        startActivity(intent);
                        // Here,  might also want to clear any saved user data or logout from a session manager if applicable
                        //SessionManager.getInstance().logout();
                        //break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    /**
     * Updates the button texts to display the current counts of posts, likes, and buy items.
     * Retrieves the counts from the current user's data.
     */
    @SuppressLint("SetTextI18n")
    private void updateButtonCounts() {
        User currentUser = SessionManager.getInstance().getUser();
        int postsCount = currentUser.getOwnPosts().size(); // Fetch actual post count
        int likesCount = currentUser.getLikePosts().size(); // Fetch actual likes count
        int buyCount = currentUser.getBuyPosts().size(); // Fetch actual buy count

        postsButton.setText(postsCount + " Posts");
        likesButton.setText(likesCount + " Likes");
        buyButton.setText(buyCount + " Buy It");
    }


    /**
     * Updates the visibility of different views (posts, likes, buy) based on the selected view.
     * Shows the selected grid and updates it with relevant data.
     *
     * @param view The view to be displayed ("posts", "likes", or "buy").
     */
    private void updateViews(String view) {
        // Hide all grids initially
        postsContainer.setVisibility(view.equals("posts") ? View.VISIBLE : View.GONE);
        likesContainer.setVisibility(view.equals("likes") ? View.VISIBLE : View.GONE);
        buyContainer.setVisibility(view.equals("buy") ? View.VISIBLE : View.GONE);

        // Show only the selected grid and update it
        if (view.equals("posts")) {
            postsContainer.setVisibility(View.VISIBLE);
            showPost(postsGrid, MyPostActivity.class);
            //showPostCanDelete();
        } else if (view.equals("likes")) {
            likesContainer.setVisibility(View.VISIBLE);
            showPost(likesGrid, PostActivity.class);
        } else if (view.equals("buy")) {
            buyContainer.setVisibility(View.VISIBLE);
            showPost(buyGrid, BuyPostActivity.class);
        }

        // Update button styles to indicate which is active
        updateButtonStyles(view);
    }


    /**
     * Updates the styles of the buttons to indicate which view is currently active.
     *
     * @param view The currently active view ("posts", "likes", or "buy").
     */
    private void updateButtonStyles(String view) {
        postsButton.setTextColor(getResources().getColor(view.equals("posts") ? R.color.black : android.R.color.white));
        likesButton.setTextColor(getResources().getColor(view.equals("likes") ? R.color.black : android.R.color.white));
        buyButton.setTextColor(getResources().getColor(view.equals("buy") ? R.color.black : android.R.color.white));
    }

    /**
     * Displays the posts in the specified grid layout. Retrieves the relevant posts from the
     * current user's data and dynamically adds views to the grid layout.
     *
     * @param grid The grid layout to display the posts in.
     * @param activityClass The activity class to navigate to when a post is clicked.
     */
    private void showPost(GridLayout grid, Class<?> activityClass) {
        // Assume data is ready or handle cases where it might not be
        grid.removeAllViews();
        // Dynamically add views based on the type of grid

        User currentUser = SessionManager.getInstance().getUser();
        if (currentUser != null) {
            List<Post> list = new ArrayList<>();
            if (grid == postsGrid) {
                //get post from likesList
                list = currentUser.getOwnPosts();
           } else if (grid == likesGrid) {
                //get post from likesList
                list = currentUser.getLikePosts();
            } else if (grid == buyGrid) {
                //get post from buyList
                list = currentUser.getBuyPosts();
            }

            for (Post post : list) {
                //get the layout from item_card.xml
                View view = LayoutInflater.from(this).inflate(R.layout.item_card, null);
                ImageView card_image = view.findViewById(R.id.card_image);
                TextView card_name = view.findViewById(R.id.card_name);
                TextView card_price = view.findViewById(R.id.card_price);

                GlideImageLoader.loadImage(ProfileActivity.this, post.getImageUrl(), card_image);
                card_name.setText(post.getProductDisplayName());
                card_price.setText(String.valueOf(post.getPrice()));

                //get the height and weight from the screen
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth / 2, ViewGroup.LayoutParams.WRAP_CONTENT);

                //add to grid layout
                grid.addView(view, params);

                //click image jump to post detail page
                card_image.setOnClickListener(v -> {
                    Intent intent = new Intent(ProfileActivity.this, activityClass);
                    intent.putExtra("post_id", post.getPostID());
                    intent.putExtra("post_image", post.getImageUrl());
                    intent.putExtra("post_name", post.getProductDisplayName());
                    intent.putExtra("post_description", post.getDescription());
                    intent.putExtra("post_price", post.getPrice());
                    intent.putExtra("post_seller", post.getUserID());
                    intent.putExtra("source", "profile");
                    startActivity(intent);
                });
            }
        }
    }

    /**
     * Initializes the UI components of the activity by finding the views by their IDs.
     */
    private void init() {
        home = findViewById(R.id.btn_home);
        search = findViewById(R.id.btn_search);
        create = findViewById(R.id.btn_create);
        profile = findViewById(R.id.btn_profile);
        textEmail = findViewById(R.id.text_email);
        textName = findViewById(R.id.text_name);
        editProfileButton = findViewById(R.id.btn_edit_profile);
        // Initialize Views
        postsContainer = findViewById(R.id.posts_container);
        likesContainer = findViewById(R.id.likes_container);
        buyContainer = findViewById(R.id.buy_container);
        postsButton = findViewById(R.id.posts_button);
        likesButton = findViewById(R.id.likes_button);
        buyButton = findViewById(R.id.buy_button);
        //  Grid layout
        postsGrid = findViewById(R.id.gl_myPosts);
        likesGrid = findViewById(R.id.gl_likePosts);
        buyGrid = findViewById(R.id.gl_buyPosts);
    }
}