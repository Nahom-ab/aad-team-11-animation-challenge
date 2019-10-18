package com.team11.animation_challenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    public final OkHttpClient client = new OkHttpClient();

    private String url;
    private String title;
    private int position = 0;
    private int correct = 0;

    private TextView questionText;
    private TextView questionCount;
    private ProgressBar pbar;
    private Spanned correctAnswer;
    private Spanned question;
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
        questionText = (TextView) findViewById(R.id.question_text);
        questionCount = (TextView) findViewById(R.id.question_count);
        pbar = findViewById(R.id.counter_pbar);
        button1 = (Button) findViewById(R.id.button_trivia_1);
        button2 = (Button) findViewById(R.id.button_trivia_2);
        button3 = (Button) findViewById(R.id.button_trivia_3);
        button4 = (Button) findViewById(R.id.button_trivia_4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        try {
            fetch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showCompletedDialog() {

        dialogBuilder = new AlertDialog.Builder(TriviaActivity.this);
        View layoutView = getLayoutInflater().inflate(R.layout.dialog_completed, null);
        dialogButton = (Button) layoutView.findViewById(R.id.button_dialog);
        TextView resultText = (TextView) layoutView.findViewById(R.id.result_text);
        TextView praiseText = (TextView) layoutView.findViewById(R.id.praise_text);

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

        completedDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completedDialog.dismiss();
                Intent i = new Intent(getBaseContext(), CategoryActivity.class);
                startActivity(i);
            }
        });
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

        clearButtonImages();

        if (position == triviaRequest.getSize() - 1) {
            showCompletedDialog();
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
                pbar.setProgress(position + 1);
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
            }
        });
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        btn.setClickable(false);
        if (btn.getText().toString().equals(correctAnswer.toString())) {
            //Right Answer Animation Here.
            ++correct;
            btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        } else {
            //Wrong Answer Animation Here
            btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wrong_black_24dp, 0);
        }
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // This'll run 1100 milliseconds later
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
