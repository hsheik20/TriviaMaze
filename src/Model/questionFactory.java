package Model;

import java.util.Random;

class QuestionFactory {
    private Random random = new Random();

    public Question createRandomQuestion() {
        int type = random.nextInt(3);
        int difficulty = random.nextInt(3) + 1;

        switch (type) {
            case 0: return createMultipleChoiceQuestion(difficulty);
            case 1: return createTrueFalseQuestion(difficulty);
            case 2: return createFillInQuestion(difficulty);
            default: return createMultipleChoiceQuestion(difficulty);
        }
    }
    //all the question are place holder
    private MultipleQuestion createMultipleChoiceQuestion(int difficulty) {
        String[][] questions = {
            {"What is the capital of France?", "Paris", "London", "Berlin", "Madrid"},
            {"Which planet is closest to the Sun?", "Mercury", "Venus", "Earth", "Mars"},
            {"What is 2 + 2?", "4", "3", "5", "6"},
            {"Who wrote Romeo and Juliet?", "Shakespeare", "Dickens", "Austen", "Tolkien"}
        };

        String[] q = questions[random.nextInt(questions.length)];
        String[] options = {q[1], q[2], q[3], q[4]};
        return new MultipleQuestion(q[0], q[1], options, difficulty);
    }

    private TrueFalseQuestion createTrueFalseQuestion(int difficulty) {
        String[][] questions = {
            {"The Earth is flat.", "False"},
            {"Java is a programming language.", "True"},
            {"Fish can fly.", "False"},
            {"The sun rises in the east.", "True"}
        };

        String[] q = questions[random.nextInt(questions.length)];
        return new TrueFalseQuestion(q[0], q[1], difficulty);
    }

    private FillInAnswerQuestion createFillInQuestion(int difficulty) {
        String[][] questions = {
            {"The capital of Italy is ____.", "Rome"},
            {"2 + 3 = ____", "5"},
            {"The color of grass is ____.", "Green"},
            {"H2O is the formula for ____.", "Water"}
        };

        String[] q = questions[random.nextInt(questions.length)];
        return new FillInAnswerQuestion(q[0], q[1], difficulty);
    }
}

