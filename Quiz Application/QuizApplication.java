import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Question {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;

    public Question(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer.toUpperCase(); // Ensure consistency
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equals(answer);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

public class QuizApplication {
    private static final String QUESTIONS_FILE = "questions.txt";
    private static final int SCORE_PER_QUESTION = 10;

    private List<Question> questions;
    private int score;

    public void loadQuestions() {
        File file = new File(QUESTIONS_FILE);

        if (!file.exists() || file.length() == 0) {  // Check if file exists and is not empty
            System.out.println("⚠️ Error: Question file is missing or empty.");
            return;
        }

        questions = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.startsWith("Question:")) {
                    if (!scanner.hasNextLine()) continue;
                    String questionText = extractContent(line);
                    String optionA = extractContent(scanner.nextLine());
                    String optionB = extractContent(scanner.nextLine());
                    String optionC = extractContent(scanner.nextLine());
                    String optionD = extractContent(scanner.nextLine());
                    String correctAnswer = extractContent(scanner.nextLine());

                    questions.add(new Question(questionText, optionA, optionB, optionC, optionD, correctAnswer));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("⚠️ Error: Unable to find " + QUESTIONS_FILE);
        }
    }

    private String extractContent(String line) {
        int colonIndex = line.indexOf(":");
        return (colonIndex != -1) ? line.substring(colonIndex + 1).trim() : "";
    }

    public void startQuiz() {
        if (questions.isEmpty()) {
            System.out.println("⚠️ No questions available. Please check the question file.");
            return;
        }

        score = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🎯 Welcome to the Quiz Application!\n");

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("📌 Question " + (i + 1) + ": " + question.getQuestionText());
            System.out.println("A. " + question.getOptionA());
            System.out.println("B. " + question.getOptionB());
            System.out.println("C. " + question.getOptionC());
            System.out.println("D. " + question.getOptionD());

            String userAnswer = getUserAnswer(scanner);

            if (question.isCorrectAnswer(userAnswer)) {
                System.out.println("✅ Correct!\n");
                score += SCORE_PER_QUESTION;
            } else {
                System.out.println("❌ Incorrect! The correct answer was: " + question.getCorrectAnswer() + "\n");
            }
        }

        System.out.println("\n--- 📊 Quiz Summary ---");
        System.out.println("Total Questions: " + questions.size());
        System.out.println("Correct Answers: " + (score / SCORE_PER_QUESTION));
        System.out.println("Incorrect Answers: " + ((questions.size() * SCORE_PER_QUESTION - score) / SCORE_PER_QUESTION));
        System.out.println("Final Score: " + score + " / " + (questions.size() * SCORE_PER_QUESTION));
        System.out.println("\n🎉 Thank you for playing!");

        scanner.close();
    }

    private String getUserAnswer(Scanner scanner) {
        String answer;
        while (true) {
            System.out.print("Your Answer (A/B/C/D): ");
            answer = scanner.nextLine().trim().toUpperCase();
            if (answer.matches("[ABCD]")) {
                return answer;
            } else {
                System.out.println("❌ Invalid choice! Please enter A, B, C, or D.");
            }
        }
    }

    public static void main(String[] args) {
        QuizApplication quiz = new QuizApplication();
        quiz.loadQuestions();
        quiz.startQuiz();
    }
}
