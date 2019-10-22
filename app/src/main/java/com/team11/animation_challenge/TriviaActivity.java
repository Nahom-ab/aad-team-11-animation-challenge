package com.team11.animation_challenge;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TriviaActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String CATEGORY_URL = "com.team11.animation_challenge.CATEGORY_URL";
    public static final String CATEGORY_TITLE = "com.team11.animation_challenge.CATEGORY_TITLE";
    public static final int TIME_LIMIT = 1000 * 25; //11 sec
    public final OkHttpClient client = new OkHttpClient();

    private String url;
    private String title;
    private int position = 0;
    private int correct = 0;

    private TextSwitcher questionText;
    private TextView questionCount;
    private ProgressBar timerProgressBar;
    private CharSequence correctAnswer;
    private CharSequence question;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button dialogButton;
    private TriviaRequest triviaRequest;
    private TriviaResult result;
    private List<CharSequence> questions;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog completedDialog;
    private TranslateAnimation animObj;
    private ObjectAnimator progressBarOA;
    private ImageView modalTrophyImage;
    private Animation mShakeAnimation;
    private Animation mBounceAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        getUrlFromIntent();
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setTitle(title);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeButtonEnabled(true);
        questionText = (TextSwitcher) findViewById(R.id.question_text);
        Animation textAnimationIn = AnimationUtils.
                loadAnimation(this, android.R.anim.slide_in_left);
        textAnimationIn.setDuration(300);
        Animation textAnimationOut = AnimationUtils.
                loadAnimation(this, android.R.anim.slide_out_right);
        textAnimationIn.setDuration(500);

        questionText.setInAnimation(textAnimationIn);
        questionText.setOutAnimation(textAnimationOut);

        questionCount = (TextView) findViewById(R.id.question_count);
        timerProgressBar = (ProgressBar) findViewById(R.id.timer_progress_bar);
        timerProgressBar.setMax(TIME_LIMIT);
        mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.skake_animation);
        mBounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);

        button1 = (Button) findViewById(R.id.button_trivia_1);
        button2 = (Button) findViewById(R.id.button_trivia_2);
        button3 = (Button) findViewById(R.id.button_trivia_3);
        button4 = (Button) findViewById(R.id.button_trivia_4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        categoryToTriviaAnimation();


        try {
            fetch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void progressBarAnimation() {
        timerProgressBar.setProgress(0);
        progressBarOA = ObjectAnimator.ofInt(timerProgressBar, "progress", TIME_LIMIT).setDuration(TIME_LIMIT);
        progressBarOA.start();
        progressBarOA.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d("Animation Ended", "true");
                moveNext();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

    }

    private void categoryToTriviaAnimation() {
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", -10000f, 0f);
        ObjectAnimator button1OA = ObjectAnimator.ofPropertyValuesHolder(button1, translationX).setDuration(1000);
        ObjectAnimator button2OA = ObjectAnimator.ofPropertyValuesHolder(button2, translationX).setDuration(1000);
        ObjectAnimator button3OA = ObjectAnimator.ofPropertyValuesHolder(button3, translationX).setDuration(1000);
        ObjectAnimator button4OA = ObjectAnimator.ofPropertyValuesHolder(button4, translationX).setDuration(1000);
        ObjectAnimator progressBarTOA = ObjectAnimator.ofPropertyValuesHolder(timerProgressBar, translationX).setDuration(1000);

        button1OA.start();
        button2OA.start();
        button3OA.start();
        button4OA.start();
        progressBarTOA.start();
    }


    private void showCompletedDialog() {

        dialogBuilder = new AlertDialog.Builder(TriviaActivity.this);
        View layoutView = getLayoutInflater().inflate(R.layout.dialog_completed, null);
        dialogButton = (Button) layoutView.findViewById(R.id.button_dialog);
        TextView resultText = (TextView) layoutView.findViewById(R.id.result_text);
        TextView praiseText = (TextView) layoutView.findViewById(R.id.praise_text);
        modalTrophyImage = (ImageView) findViewById(R.id.award);

        dialogBuilder.setView(layoutView);
        completedDialog = dialogBuilder.create();
        completedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        resultText.setText(getString(R.string.result_Info, correct, 11));
        if (correct < 5) {
            praiseText.setText(R.string.less_than_5);
        } else if (correct >= 5 && correct <= 7) {
            praiseText.setText(R.string.five_to_7);
        } else if (correct > 8 && correct <= 10) {
            praiseText.setText(R.string.eight_to_10);
        } else {
            praiseText.setText(R.string.perfect_score);
        }

        completedDialog.getWindow().getAttributes().windowAnimations = R.style.CompletedDialogAnimation;

        completedDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completedDialog.dismiss();
                Intent i = new Intent(getBaseContext(), CategoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    private void trophyAnimation() {
        PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotationX", 0f, 360f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", -10000f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", -10000f, 0f);
        ObjectAnimator imageOA = ObjectAnimator.ofPropertyValuesHolder(modalTrophyImage, rotateX).setDuration(10000);
        imageOA.start();
    }

    private void getUrlFromIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra(CATEGORY_URL);
        title = intent.getStringExtra(CATEGORY_TITLE);
    }


    private void fetch() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    assert responseBody != null;
                    String Body = responseBody.string();
                    triviaRequest = gson.fromJson(Body, TriviaRequest.class);
                    result = triviaRequest.getResults().get(position);
                    displayQuestion(result);
                }
            }
        });
    }

    private void moveNext() {
        progressBarOA.removeAllListeners();
        progressBarOA.cancel();
        clearButtonImages();
        questionChangeAnimation();

        if (position == triviaRequest.getSize() - 1) {
            showCompletedDialog();
        } else {
            ++position;
            result = triviaRequest.getResults().get(position);
            displayQuestion(result);
        }
    }

    private void questionChangeAnimation() {
        PropertyValuesHolder fadeOut = PropertyValuesHolder.ofFloat("alpha", 1, 0);
        PropertyValuesHolder fadeIn = PropertyValuesHolder.ofFloat("alpha", 0, 1);

        ObjectAnimator button1OA = ObjectAnimator.ofPropertyValuesHolder(button1, fadeOut, fadeIn).setDuration(1000);
        ObjectAnimator button2OA = ObjectAnimator.ofPropertyValuesHolder(button2, fadeOut, fadeIn).setDuration(1000);
        ObjectAnimator button3OA = ObjectAnimator.ofPropertyValuesHolder(button3, fadeOut, fadeIn).setDuration(1000);
        ObjectAnimator button4OA = ObjectAnimator.ofPropertyValuesHolder(button4, fadeOut, fadeIn).setDuration(1000);
        button1OA.start();
        button2OA.start();
        button3OA.start();
        button4OA.start();
    }

    private void clearButtonImages() {
        button1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        button2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        button3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        button4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void displayQuestion(TriviaResult result) {
        Log.d("position", String.valueOf(position));
        Log.d("question", String.valueOf(result.getQuestion()));
        questions = new ArrayList<>();
        question = Html.fromHtml(result.getQuestion());
        correctAnswer = Html.fromHtml(result.getCorrect_answer());
        Spanned incorrectAnswer1 = Html.fromHtml(result.getIncorrect_answers().get(0));
        Spanned incorrectAnswer2 = Html.fromHtml(result.getIncorrect_answers().get(1));
        Spanned incorrectAnswer3 = Html.fromHtml(result.getIncorrect_answers().get(2));
        questions.add(correctAnswer);
        questions.add(incorrectAnswer1);
        questions.add(incorrectAnswer2);
        questions.add(incorrectAnswer3);

        Collections.shuffle(questions);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // Stuff that updates the UI
                questionCount.setText(getString(R.string.question_Count, position + 1, 11));
                questionText.setText(question);
                button1.setText(questions.get(0));
                button2.setText(questions.get(1));
                button3.setText(questions.get(2));
                button4.setText(questions.get(3));
                button1.setClickable(true);
                button2.setClickable(true);
                button3.setClickable(true);
                button4.setClickable(true);
                progressBarAnimation();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        button4.setClickable(false);
        if (btn.getText().toString().equals(correctAnswer.toString())) {
            //Right Answer Animation Here.
            ++correct;
            btn.startAnimation(mBounceAnimation);
            btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ans_right, 0);

        } else {
            //Wrong Answer Animation Here
            btn.startAnimation(mShakeAnimation);
            btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ans_wrong, 0);

        }
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        moveNext();
                    }
                },
                700);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trivia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            try {
                fetch();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
