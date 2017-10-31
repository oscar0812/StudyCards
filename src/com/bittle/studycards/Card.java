package com.bittle.studycards;

public class Card {
    private String question, answer;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;

        Card card = (Card) obj;
        return question.trim().equals(card.question.trim())
                && answer.trim().equals(card.answer.trim());
    }

    @Override
    public String toString() {
        return "Question: " + question + "\nAnswer: " + answer;
    }
}
