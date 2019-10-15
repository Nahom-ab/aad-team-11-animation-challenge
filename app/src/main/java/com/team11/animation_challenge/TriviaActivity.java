package com.team11.animation_challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class TriviaActivity extends AppCompatActivity {

    public final OkHttpClient client = new OkHttpClient();

    public String url = "https://opentdb.com/api.php?amount=1&type=multiple";
    public TextView questionText;
    public TextView questionCata;
    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    public Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        getSupportActionBar().setTitle("Trivia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        questionText = (TextView) findViewById(R.id.questionText);
        questionCata = (TextView) findViewById(R.id.questionCata);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fetch() throws Exception {
        // Swap url with one that comes from categories screen intent
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
                    Log.d("response", Body);
                    TriviaRequest triviaRequest = gson.fromJson(Body, TriviaRequest.class);
                    Log.d("triviaRequest", String.valueOf(triviaRequest));
                    TriviaResult result = triviaRequest.getResults().get(0);
                    Log.d("triviaResult category", String.valueOf(result.getCategory()));

                    final List<CharSequence> questions = new ArrayList<>();
                    final Spanned question = Html.fromHtml(result.getQuestion());
                    final Spanned questionCategory = Html.fromHtml(result.getCategory());
                    final Spanned correctAnswer = Html.fromHtml(result.getCorrect_answer());
                    Spanned incorrectAnswer1 = Html.fromHtml(result.getIncorrect_answers().get(0));
                    Spanned incorrectAnswer2 = Html.fromHtml(result.getIncorrect_answers().get(1));
                    Spanned incorrectAnswer3 = Html.fromHtml(result.getIncorrect_answers().get(2));
                    questions.add(correctAnswer);
                    questions.add(incorrectAnswer1);
                    questions.add(incorrectAnswer2);
                    questions.add(incorrectAnswer3);
                    Log.d("questions", questions.toString());
                    Collections.shuffle(questions);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            questionText.setText(question);
                            questionCata.setText(questionCategory);
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


                            button1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Button btn = (Button) view;
                                    btn.setClickable(false);
                                    if (button1.getText().toString().equals(correctAnswer.toString())) {
                                        try {
                                            //Right Answer Animation Here.
                                            fetch();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //Wrong Answer Animation Here
                                        button1.setText("---");
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
                                            fetch();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //Wrong Answer Animation Here
                                        button2.setText("---");
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
                                            fetch();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //Wrong Answer Animation Here
                                        button3.setText("---");
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
                                            fetch();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //Wrong Answer Animation Here
                                        button4.setText("---");

                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
