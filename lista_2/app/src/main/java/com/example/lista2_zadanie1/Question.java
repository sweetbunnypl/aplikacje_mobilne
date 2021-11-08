package com.example.lista2_zadanie1;

public class Question {
    private int textId;
    private int answer;
    private boolean alreadyAnswered;
    private boolean cheatedByUser;

    public Question(int textId, int answer, boolean alreadyAnswered, boolean cheatedByUser) {
        this.textId = textId;
        this.answer = answer;
        this.alreadyAnswered = alreadyAnswered;
        this.cheatedByUser = cheatedByUser;
    }

    //uzyskanie treści pytania
    public int getTextId() {
        return textId;
    }

    //przypisanie treści pytania
    public void setTextId(int textId) {
        this.textId = textId;
    }

    //uzyskanie odpowiedzi na pytanie
    public int isAnswer() {
        return answer;
    }

    //przypisanie odpowiedzi na pytanie
    public void setAnswer(int answer) {
        this.answer = answer;
    }

    //sprawdzenie, czy ktoś już odpowiedział na pytanie
    public boolean isAlreadyAnswered() {
        return alreadyAnswered;
    }

    //ustawienie, że ktoś już odpowiedział na pytanie
    public void setAlreadyAnswered(boolean alreadyAnswered) {
        this.alreadyAnswered = alreadyAnswered;
    }

    //sprawdzenie, czy ktoś już oszukał pytanie
    public boolean isCheatedByUser() {
        return cheatedByUser;
    }

    //ustawienie, że ktoś już oszukał pytanie
    public void setCheatedByUser(boolean cheatedByUser) {
        this.cheatedByUser = cheatedByUser;
    }

}
