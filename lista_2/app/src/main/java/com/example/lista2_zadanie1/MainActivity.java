package com.example.lista2_zadanie1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //pole tekstowe
    private TextView viewQuestion;

    //przyciski
    private Button button_true;
    private Button button_false;
    private Button button_next;
    private Button button_previous;
    private Button button_restart;
    private Button button_cheat;

    //pytania
    private final Question[] questions = new Question[] {
            new Question(R.string.question1, R.bool.question1_ans, false, false),
            new Question(R.string.question2, R.bool.question2_ans, false, false),
            new Question(R.string.question3, R.bool.question3_ans, false, false),
            new Question(R.string.question4, R.bool.question4_ans, false, false),
            new Question(R.string.question5, R.bool.question5_ans, false, false),
            new Question(R.string.question6, R.bool.question6_ans, false, false),
            new Question(R.string.question7, R.bool.question7_ans, false, false),
            new Question(R.string.question8, R.bool.question8_ans, false, false),

    };

    //które obecnie pytanie
    int i;
    //ilość punktów
    int points;
    //czy jest podsumowanie
    boolean result;
    //ile razy oszukano
    int cheated;
    //na ile pytań już udzielono odpowiedzi
    int answered;
    //maksymalna ilość punktów
    int maxPoints = questions.length;
    //końcowy wynik
    double finalResultPercentage;

    //druga aktywność - przekazywane zmienne
    public static final String current_question_cheated = "com.example.lista2_zadanie1.QCH";
    public static final String current_answer_cheated = "com.example.lista2_zadanie1.ACH";
    public static final String current_question_answered = "com.example.lista2_zadanie1.QA";
    public static final String is_question_cheated = "com.example.lista2_zadanie1.CH";
    public static final int is_question_cheated_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindComponentsId();
        i = 0;
        points = 0;
        cheated = 0;
        answered = 0;
        finalResultPercentage = 0;
        result = false;
        setViewQuestionLook();
    }

    //wyświetlanie pytania z indeksem przed nim
    public void setViewQuestionLook() {
        if(!result){
            viewQuestion.setTextSize(30);
            viewQuestion.setText(String.format("%d. ", i+1)+getString(questions[i].getTextId()));}
        else{
            viewQuestion.setTextSize(26);
            viewQuestion.setText(String.format("Final score: %s%%\nCorrect answers: %d\nIncorrect answers: %d\nCheated answers: %d", String.format("%.2f", finalResultPercentage), points, maxPoints-points, cheated));}

    }

    //dwie metody dotyczące przycisku w prawym górnym rogu, odpowiadającego za wyszukiwanie informacji w internecie
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!result){
            switch (item.getItemId()) {
                case R.id.searchButton:
                    SearchForAnswer();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //odczyt zapisanych danych przed zmianą orientacji urządzenia
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            i = savedInstanceState.getInt("current_question");
            answered = savedInstanceState.getInt("answered_questions");
            cheated = savedInstanceState.getInt("cheated_questions");
            points = savedInstanceState.getInt("points");
            result = savedInstanceState.getBoolean("result");
            finalResultPercentage = savedInstanceState.getDouble("final_result_percentage");

            for (int i = 0; i < questions.length; i++) {
                boolean is_question_answered = savedInstanceState.getBoolean(String.format("question_ans%d", i));
                questions[i].setAlreadyAnswered(is_question_answered);
                boolean is_question_cheated_by_user = savedInstanceState.getBoolean(String.format("question_ch%d", i));
                questions[i].setCheatedByUser(is_question_cheated_by_user);
            }
            setViewQuestionLook();
            ChangeButtonsVisibility();
        }
    }

    //przypisanie wartości przycisków i pola textview do zmiennych
    public void FindComponentsId() {
        viewQuestion = findViewById(R.id.view_question);
        button_true = findViewById(R.id.button_true);
        button_false = findViewById(R.id.button_false);
        button_next = findViewById(R.id.button_next);
        button_previous = findViewById(R.id.button_previous);
        button_restart = findViewById(R.id.button_restart);
        button_cheat = findViewById(R.id.button_cheat);
    }

    //zmiana aktualnego pytania
    public void Next(View view) {
        i++;
        if (i >= questions.length) {
            i = 0;
        }
        setViewQuestionLook();
        ChangeButtonsVisibility();
    }

    public void Previous(View view) {
        i--;
        if (i < 0) {
            i = questions.length-1;
        }
        setViewQuestionLook();
        ChangeButtonsVisibility();
    }

    //odpowiedź na pytanie
    public void AnswerTrue(View view) {
        if (!questions[i].isAlreadyAnswered()){
            answered += 1;
        }
        if (Boolean.valueOf(getString(questions[i].isAnswer())) && !questions[i].isAlreadyAnswered()) {
            questions[i].setAlreadyAnswered(true);
            points += 1;
        }
        else {
            questions[i].setAlreadyAnswered(true);
        }
        ChangeButtonsVisibility();

        if (answered == questions.length) {
            FinalStats(view);
        }
    }

    public void AnswerFalse(View view) {
        if (!questions[i].isAlreadyAnswered()){
            answered += 1;
        }
        if (!Boolean.valueOf(getString(questions[i].isAnswer())) && !questions[i].isAlreadyAnswered()) {
            questions[i].setAlreadyAnswered(true);
            points += 1;
        }
        else {
            questions[i].setAlreadyAnswered(true);
        }
        ChangeButtonsVisibility();

        if (answered == questions.length) {
            FinalStats(view);
        }
    }

    //restart quizu
    public void RestartQuiz(View view) {
        i = 0;
        points = 0;
        cheated = 0;
        answered = 0;
        finalResultPercentage = 0;
        result = false;

        for (int i = 0; i < questions.length; i++) {
            questions[i].setAlreadyAnswered(false);
            questions[i].setCheatedByUser(false);
        }
        setViewQuestionLook();
        ChangeButtonsVisibility();
    }

    //druga aktywność aplikacji, otwiera się okno z możliwością oszustwa
    public void CheatAnswer(View view) {
        String currentQuestion = getString(questions[i].getTextId());
        String currentAnswer = String.valueOf(getString(questions[i].isAnswer()));
        String isAnswered = String.valueOf(questions[i].isAlreadyAnswered());
        String isQuestionCheated = String.valueOf(questions[i].isCheatedByUser());

        Intent intent = new Intent(this, CheatActivity.class);
        intent.putExtra(current_question_cheated, currentQuestion);
        intent.putExtra(current_answer_cheated, currentAnswer);
        intent.putExtra(current_question_answered, isAnswered);
        intent.putExtra(is_question_cheated, isQuestionCheated);
        startActivityForResult(intent, is_question_cheated_request);
    }

    //odbieranie danych z drugiej aktywności, jeśli null nic się nie zmienia
    @Override
    protected void onActivityResult(int Request, int Result, @Nullable Intent Data) {
        super.onActivityResult(Request, Result, Data);
        String reply = "";
        //System.out.println(Data);
        if (Data != null) {
            reply = Data.getStringExtra(CheatActivity.already_cheated);
            if (!questions[i].isAlreadyAnswered() && !questions[i].isCheatedByUser() && Boolean.valueOf(reply)) {
                questions[i].setCheatedByUser(Boolean.valueOf(reply));
                cheated += 1;
            }
        }
        ChangeButtonsVisibility();
    }

    //otwieranie strony internetowej z odpowiedzią
    public void SearchForAnswer(){
        String url = getString(questions[i].getTextId());
        Uri webpage = Uri.parse(String.format("https://www.google.com/search?q=%s", url));
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            Log.d("error", "activity not found");
        }
    }

    //aktualizowanie, które przyciski mają się wyświetlać w danym momencie
    public void ChangeButtonsVisibility() {
        if (questions[i].isAlreadyAnswered() && !result){
            //udzielono odpowiedź i nie ma ekranu końcowego
            button_true.setVisibility(View.INVISIBLE);
            button_false.setVisibility(View.INVISIBLE);
            button_next.setVisibility(View.VISIBLE);
            button_previous.setVisibility(View.VISIBLE);
            button_cheat.setVisibility(View.VISIBLE);
            button_restart.setVisibility(View.INVISIBLE);
            button_cheat.setText(getString(R.string.answer));

        } else if (!questions[i].isAlreadyAnswered() && !result){
            //nie udzielono odpowiedzi i nie ma ekranu końcowego
            button_true.setVisibility(View.VISIBLE);
            button_false.setVisibility(View.VISIBLE);
            button_next.setVisibility(View.VISIBLE);
            button_previous.setVisibility(View.VISIBLE);
            button_cheat.setVisibility(View.VISIBLE);
            button_restart.setVisibility(View.INVISIBLE);

            if (!questions[i].isCheatedByUser())
                button_cheat.setText(getString(R.string.cheat));
            else
                button_cheat.setText(getString(R.string.answer));

        } else {
            //wynik koncowy
            button_true.setVisibility(View.INVISIBLE);
            button_false.setVisibility(View.INVISIBLE);
            button_next.setVisibility(View.INVISIBLE);
            button_previous.setVisibility(View.INVISIBLE);
            button_cheat.setVisibility(View.INVISIBLE);
            button_restart.setVisibility(View.VISIBLE);
        }
    }

    //wyświetlenie rezultatu
    public void FinalStats(View view) {
        result = true;
        ChangeButtonsVisibility();
        double finalResult = points*(1-(cheated*0.15));
        if (finalResult <= 0)
            finalResult = 0;
        finalResultPercentage = (finalResult/maxPoints)*100;
        setViewQuestionLook();
    }

    //zapisanie danych przed zmianą orientacji
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < questions.length; i++) {
            outState.putBoolean(String.format("question_ans%d", i), questions[i].isAlreadyAnswered());
            outState.putBoolean(String.format("question_ch%d", i), questions[i].isCheatedByUser());
        }
        outState.putInt("current_question", i);
        outState.putInt("answered_questions", answered);
        outState.putInt("cheated_questions", cheated);
        outState.putInt("points", points);
        outState.putBoolean("result", result);
        outState.putDouble("final_result_percentage", finalResultPercentage);
    }
}