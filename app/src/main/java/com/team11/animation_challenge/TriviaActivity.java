package com.team11.animation_challenge;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class TriviaActivity extends AppCompatActivity {
    public static final String CATEGORY_URL = "com.team11.animation_challenge.CATEGORY_URL";
    public static final String CATEGORY_TITLE = "com.team11.animation_challenge.CATEGORY_TITLE";
    public final OkHttpClient client = new OkHttpClient();

    private String url;
    private String title;
    private int position = 0;
    public TextView questionText;
    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    public ImageButton restartButton;
    private TriviaRequest triviaRequest;
    private TriviaResult result;
    private List<CharSequence> questions;
    private ActionBar supportActionBar;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        getUrlFromIntent();
        supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setTitle(title);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeButtonEnabled(true);
        questionText = (TextView) findViewById(R.id.questionText);
        button1 = (Button) findViewById(R.id.button_trivia_1);
        button2 = (Button) findViewById(R.id.button_trivia_2);
        button3 = (Button) findViewById(R.id.button_trivia_3);
        button4 = (Button) findViewById(R.id.button_trivia_4);
        button5 = (Button) findViewById(R.id.button_trivia_next);
        restartButton = (ImageButton) findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    position = 0;
                    fetch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            fetch();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getUrlFromIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra(CATEGORY_URL);
        title = intent.getStringExtra(CATEGORY_TITLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

        clearButtonImages();

        if (position == triviaRequest.getSize() - 1) {
            Toast.makeText(this, "End of Questions Reached", Toast.LENGTH_SHORT).show();
        } else {
            ++position;
            result = triviaRequest.getResults().get(position);
            displayQuestion(result);
        }
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
        final Spanned question = Html.fromHtml(result.getQuestion());
        final Spanned correctAnswer = Html.fromHtml(result.getCorrect_answer());
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
                questionText.setText(question);
                button1.setText(questions.get(0));
                button2.setText(questions.get(1));
                button3.setText(questions.get(2));
                button4.setText(questions.get(3));
                button1.setClickable(true);
                button2.setClickable(true);
                button3.setClickable(true);
                button4.setClickable(true);
                Log.d("correctAnswer", correctAnswer.toString());
                Log.d("Button1 Text ", button1.getText().toString());

                button5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveNext();
                    }
                });


                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button btn = (Button) view;
                        btn.setClickable(false);
                        if (button1.getText().toString().equals(correctAnswer.toString())) {
                            try {
                                //Right Answer Animation Here.
                                button1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //Wrong Answer Animation Here
                            button1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wrong_black_24dp, 0);
                        }
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button btn = (Button) view;
                        btn.setClickable(false);
                        if (button2.getText().toString().equals(correctAnswer.toString())) {
                            try {
                                //Right Answer Animation Here.
                                button2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //Wrong Answer Animation Here
                            button2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wrong_black_24dp, 0);
                        }
                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button btn = (Button) view;
                        btn.setClickable(false);
                        if (button3.getText().toString().equals(correctAnswer.toString())) {
                            try {
                                //Right Answer Animation Here.
                                button3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //Wrong Answer Animation Here
                            button3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wrong_black_24dp, 0);
                        }
                    }
                });
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button btn = (Button) view;
                        btn.setClickable(false);
                        if (button4.getText().toString().equals(correctAnswer.toString())) {
                            try {
                                //Right Answer Animation Here.
                                button4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //Wrong Answer Animation Here
                            button4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wrong_black_24dp, 0);
                        }
                    }
                });
            }
        });
    }


}
