package Model;

class Trivia {
    private QuestionFactory questionFactory;
    private Question currentQuestion;

    public Trivia() {
        this.questionFactory = new QuestionFactory();
    }

    public Question generateQuestion() {
        currentQuestion = questionFactory.createRandomQuestion();
        return currentQuestion;
    }

    public Question getCurrentQuestion() { return currentQuestion; }
}

