import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Question class (Single Responsibility Principle)
class Question {
    private String question;
    private String[] options;
    private int correctAnswer;

    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}

// Quiz logic class (Open/Closed Principle)
class Quiz {
    private Question[] questions;
    private int currentIndex = 0;
    private int score = 0;

    public Quiz(Question[] questions) {
        this.questions = questions;
    }

    public Question getCurrentQuestion() {
        return questions[currentIndex];
    }

    public void checkAnswer(int answer) {
        if (answer == getCurrentQuestion().getCorrectAnswer()) {
            score++;
        }
        currentIndex++;
    }

    public boolean hasNext() {
        return currentIndex < questions.length;
    }

    public int getScore() {
        return score;
    }
}

// GUI class
public class QuizApp extends JFrame {
    private Quiz quiz;
    private JLabel questionLabel, timerLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;
    private int time = 15;

    public QuizApp() {
        setTitle("Quiz App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sample questions
        Question[] q = {
            new Question("Java is?", new String[]{"Language", "OS", "Browser", "Tool"}, 0),
            new Question("OOP stands for?", new String[]{"Object Oriented Programming", "Only One Program", "Open Office", "None"}, 0)
        };

        quiz = new Quiz(q);

        questionLabel = new JLabel();
        timerLabel = new JLabel("Time: 15");

        JPanel top = new JPanel(new GridLayout(2,1));
        top.add(questionLabel);
        top.add(timerLabel);

        add(top, BorderLayout.NORTH);

        options = new JRadioButton[4];
        group = new ButtonGroup();
        JPanel center = new JPanel(new GridLayout(4,1));

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            center.add(options[i]);
        }

        add(center, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        add(nextButton, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> nextQuestion());

        loadQuestion();
        startTimer();

        setVisible(true);
    }

    private void loadQuestion() {
        if (!quiz.hasNext()) {
            JOptionPane.showMessageDialog(this, "Score: " + quiz.getScore());
            System.exit(0);
        }

        group.clearSelection();
        Question q = quiz.getCurrentQuestion();
        questionLabel.setText(q.getQuestion());

        String[] opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            options[i].setText(opts[i]);
        }
    }

    private void nextQuestion() {
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i;
            }
        }

        quiz.checkAnswer(selected);
        time = 15;
        loadQuestion();
    }

    // Multithreading for timer
    private void startTimer() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    time--;
                    timerLabel.setText("Time: " + time);

                    if (time <= 0) {
                        SwingUtilities.invokeLater(() -> nextQuestion());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}