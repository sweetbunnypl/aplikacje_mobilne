package com.example.lista2_zadanie1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;

public class CheatActivity extends AppCompatActivity {

    private TextView viewQuestion;
    private TextView warningQuestion;
    private boolean answerShowed;
    private Button button_cheat;
    private String question;
    private boolean answer;
    private boolean userAnswered;
    private boolean intentCreated;

    public static final String already_cheated = "com.example.lista2_zadanie1.CHR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        viewQuestion = findViewById(R.id.view_question);
        warningQuestion = findViewById(R.id.warning_question);
        button_cheat = findViewById(R.id.button_cheat);
        intentCreated = false;
        CheckState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //powrót do głównej aktywności
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //odczyt zapisanych danych przed zmianą orientacji urządzenia
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            question = savedInstanceState.getString("question");
            answer = savedInstanceState.getBoolean("answer");
            answerShowed = savedInstanceState.getBoolean("answer_showed");
            userAnswered = savedInstanceState.getBoolean("user_answered");
            intentCreated = savedInstanceState.getBoolean("intent_created");
            ChangeButtonsVisibility();
            if (intentCreated) {
                ReturnMessage();
            }
        }
    }

    //pokaż poprawną odpowiedź
    public void ShowAnswer(View view) {
        answerShowed = true;
        warningQuestion.setText("Answer: "+answer);
        button_cheat.setVisibility(view.INVISIBLE);
        ReturnMessage();
    }

    //odczytaj otrzymany intent z aktywności głównej
    public void CheckState(){
        Intent intent = getIntent();
        question = intent.getStringExtra(MainActivity.current_question_cheated);
        answer = Boolean.valueOf(intent.getStringExtra(MainActivity.current_answer_cheated));
        userAnswered = Boolean.valueOf(intent.getStringExtra(MainActivity.current_question_answered));
        answerShowed = Boolean.valueOf(intent.getStringExtra(MainActivity.is_question_cheated));
        ChangeButtonsVisibility();
    }

    //aktualizowanie, które przyciski mają się wyświetlać w danym momencie
    public void ChangeButtonsVisibility(){
        if(answerShowed){
            warningQuestion.setText("Answer: "+answer);
            button_cheat.setVisibility(View.INVISIBLE);
        } else {
            button_cheat.setVisibility(View.VISIBLE);
            if (userAnswered) {
                warningQuestion.setText("Answer: "+answer);
                button_cheat.setVisibility(View.INVISIBLE);
            }
        }
        viewQuestion.setText(question);
    }

    //po wciśnięciu "wróć" intent zostaje zamknięty
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //zwraca intent z odpowiedzią czy ktoś oszukał czy nie
    public void ReturnMessage() {
        intentCreated = true;
        Intent intent = new Intent();
        intent.putExtra(already_cheated, String.valueOf(answerShowed));
        setResult(RESULT_OK, intent);
    }

    //zapisanie danych przed zmianą orientacji
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("question", question);
        outState.putBoolean("answer", answer);
        outState.putBoolean("answer_showed", answerShowed);
        outState.putBoolean("user_answered", userAnswered);
        outState.putBoolean("intent_created", intentCreated);
    }
}