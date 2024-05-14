package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.BPlusTree.Post.BPlusTreeManagerPost;
import com.example.myapplication.BPlusTree.Remark.BPlusTreeManagerRemark;
import com.example.myapplication.R;
import com.example.myapplication.activity.Image.GlideImageLoader;
import com.example.myapplication.src.Post;
import com.example.myapplication.src.Remark.RemarkDemo;

import java.util.List;

public class BuyPostActivity extends AppCompatActivity {
    private TextView post_name;
    private TextView post_price;
    private ImageView post_image;

    private ImageView post_return;
    private ImageView post_like;

    private TextView seller_name;

    private ImageView post_star;
    private TextView post_description;

    private Post currentPost;

    private GridLayout gl_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_post);
        init();
        showDetail();
        //id of the current post
        String post_id = getIntent().getStringExtra("post_id");
        //user id in this post
        String user_id = getIntent().getStringExtra("user_id");
        //the class for the current post
        currentPost = BPlusTreeManagerPost.searchPostId(getApplicationContext(), post_id);
        showComment(currentPost);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //showDetail();
    }

    private void showDetail(){
        String p_name = getIntent().getStringExtra("post_name");
        String p_image = getIntent().getStringExtra("post_image");
        Double p_price = getIntent().getDoubleExtra("post_price",0);
        String p_description = getIntent().getStringExtra("post_description");
        String p_seller = getIntent().getStringExtra("post_seller");

        GlideImageLoader.loadImage(BuyPostActivity.this,p_image,post_image);
        post_description.setText(p_description);
        post_price.setText(String.valueOf(p_price));
        post_name.setText(p_name);
        seller_name.setText(p_seller);

        //back button
        post_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showComment(Post currentPost){

        //comment list
        List<RemarkDemo> list = BPlusTreeManagerRemark.get(currentPost.getPostID());

        for (RemarkDemo remark: list){
            //get the layout from item_comment.xml
            View view = LayoutInflater.from(this).inflate(R.layout.item_comment,null);
            TextView comment_name = view.findViewById(R.id.comment_email);
            TextView comment_context = view.findViewById(R.id.comment_message);

            comment_name.setText(remark.getUserEmail());
            comment_context.setText(remark.getText());

            //get the height and weight from the screen
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            //add to grid layout
            gl_comment.addView(view,params);
        }
    }

    private void init(){
        post_name = findViewById(R.id.post_name);
        post_description = findViewById(R.id.post_description);
        post_image = findViewById(R.id.post_image);
        post_like = findViewById(R.id.btn_post_like);
        post_return = findViewById(R.id.btn_post_return);
        post_star = findViewById(R.id.post_star);
        seller_name = findViewById(R.id.seller_name);
        post_price = findViewById(R.id.post_price);
        gl_comment = findViewById(R.id.gl_comment);
    }
}