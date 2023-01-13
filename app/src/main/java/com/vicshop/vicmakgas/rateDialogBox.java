package com.vicshop.vicmakgas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class rateDialogBox extends Dialog {

    private float gRate = 0;
    private ReviewInfo reviewInfo;
    private ReviewManager manager;
    Context context;
    SharedPreferences sharedPreferences;

    public rateDialogBox(@NonNull Context context, SharedPreferences sharedPreferences) {
        super(context);
        this.context = context;
        this.sharedPreferences = sharedPreferences;

        if(context instanceof Activity){
            setOwnerActivity((Activity) context);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_layout);

        final AppCompatButton rateNow = findViewById(R.id.rateNow);
        final AppCompatButton rateLater = findViewById(R.id.rateLater);
        final RatingBar rateBar = findViewById(R.id.myRateBar);
        final ImageView rateEmoji = findViewById(R.id.rateEmoji);

        activeReviewInfo();

        rateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(v <= 1){
                    rateEmoji.setImageResource(R.drawable.single_star);
                }else if(v <= 2){
                    rateEmoji.setImageResource(R.drawable.two_stars);
                }else if(v <= 3){
                    rateEmoji.setImageResource(R.drawable.three_stars);
                }else if(v <= 4){
                    rateEmoji.setImageResource(R.drawable.four_stars);
                }else if(v <= 5){
                    rateEmoji.setImageResource(R.drawable.five_stars);
                }

                gRate = v;
            }
        });


        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                startReviewFlow();

                sharedPreferences.edit().putBoolean("rated_us", true).apply();
            }
        });

        rateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        int i = 0;

        int delayMillis = 0;

        for(i = 0; i < 3; i++){
            delayMillis += 1400;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animateStars(rateBar);
                }
            }, delayMillis);

        }

    }

    void activeReviewInfo(){
        manager = ReviewManagerFactory.create(context);

        Task<ReviewInfo> managerInfoTask = manager.requestReviewFlow();

        managerInfoTask.addOnCompleteListener((task)->{
            if(task.isSuccessful()){
                reviewInfo = task.getResult();
            }else{
                Toast.makeText(context, "Review Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void startReviewFlow(){
        if(reviewInfo != null){
            Task<Void> flow = manager.launchReviewFlow(getOwnerActivity(), reviewInfo);

            flow.addOnCompleteListener(task -> {
                Toast.makeText(context, "Rating is successful", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void animateStars(RatingBar ratingBar){

        int i = 0;
        int delayMillis = 0;

        for(i = 0; i < 6; i++){
            delayMillis += 200;

            int finalI = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ratingBar.setRating(finalI);
                }
            }, delayMillis);
        }

    }


}
