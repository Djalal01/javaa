import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

public class quiz extends JFrame {

    private JLabel questionLabel, resultatLabel, scoreLabel, tempsRestantLabel;
    private JButton[] choixButtons;
    private int niveau;
    private int play;
    private int score;
    private int nombre1, nombre2, reponseCorrecte;
    private char operateur;
    private int numQuestion;
    private Timer chrono; // Timer pour le chronomètre
    private int tempsRestant; // Temps restant en secondes
    private JPanel topPanel;

    public quiz(String titre) {
        super(titre);

        // Demander le niveau à l'utilisateur
      

        // Initialisation des composants
        questionLabel = new JLabel();
        resultatLabel = new JLabel();
        scoreLabel = new JLabel("Score: 0");
        tempsRestantLabel = new JLabel();
        choixButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            choixButtons[i] = new JButton();
        }

        // Configuration de l'interface
        setLayout(new BorderLayout());

        // Panel for the top section (Question number, Timer, Score)
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Question: " + numQuestion));
        if (niveau == 2) {
            topPanel.add(tempsRestantLabel);
        }
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);
        topPanel.setBackground(new Color(0, 0,0));
        // Panel for the center section (Question, Result, and Choices)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Panel for the question
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        questionPanel.add(questionLabel);
        centerPanel.add(questionPanel);

        // Panel for the choices
        JPanel choicesPanel = new JPanel(new GridLayout(2, 2));
        for (int i = 0; i < 4; i++) {
            choicesPanel.add(choixButtons[i]);
        }
        centerPanel.add(choicesPanel);

        // Panel for the result
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultPanel.add(resultatLabel);
        centerPanel.add(resultPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Panel for the bottom section (Result)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(resultatLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Ajout du label et du chronomètre seulement pour le niveau difficile
        if (niveau == 2) {
            tempsRestantLabel.setText("Temps restant: ");
            chrono = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tempsRestant--;
                    tempsRestantLabel.setText("Temps restant: " + tempsRestant + "s");

                    if (tempsRestant <= 0) {
                        resultatLabel.setText("Temps écoulé. La réponse correcte est : " + reponseCorrecte);
                        stopChrono();
                        initialiserJeu();
                    }
                }
            });
        }

        // Gestionnaire d'événements pour les boutons de choix
        for (int i = 0; i < 4; i++) {
            final int choix = i;
            choixButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    verifierReponse(choix);
                }
            });
        }
 

    

        // Initialisation du jeu
        initialiserJeu();

        // Affichage de la fenêtre
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        
        setVisible(true);  
      

    }

    private void initialiserJeu() {
        if (numQuestion >= 10) {
            // Afficher un message de fin du jeu
            if(score >= 5){
                JOptionPane.showMessageDialog(null, "Fin du jeu, vous avez gagner! Votre score final est : " + score, "Fin du jeu", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Fin du jeu, vous avez perdu! Votre score final est : " + score, "Fin du jeu", JOptionPane.INFORMATION_MESSAGE);
            }
             String[] playagain = {"Rejouer", "Quitter"};
        play = JOptionPane.showOptionDialog(null, "Choisissez le niveau", "Niveau", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, playagain, playagain[0]) + 1;
                if (play == 1) {
                    score = 0;
                    numQuestion = 0;
                    initialiserJeu();
                    
                } else {
                    System.exit(0);
                }
        }

        // Incrémenter le numéro de la question
        numQuestion++;

        // Génération des nombres et de l'opérateur en fonction du niveau
        Random random = new Random();
        if (niveau == 1) {
            nombre1 = random.nextInt(10);
            nombre2 = random.nextInt(10);
            operateur = (random.nextInt(2) == 0) ? '+' : '-';
        } else {
            nombre1 = random.nextInt(10);
            nombre2 = random.nextInt(10);
            operateur = getOperateur(random.nextInt(4));
            tempsRestant = 10; // Changer la valeur du temps pour le niveau difficile
            tempsRestantLabel.setText("Temps restant: " + tempsRestant + "s");
            startChrono();
        }

        // Calcul de la réponse correcte
        reponseCorrecte = calculerReponse();

        // Affichage de la question
        questionLabel.setText("Question " + numQuestion + ": Combien font " + nombre1 + " " + operateur + " " + nombre2 + "?");

        // Génération de choix aléatoires pour les boutons
        int reponseCorrecteIndex = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == reponseCorrecteIndex) {
                choixButtons[i].setText(String.valueOf(reponseCorrecte));
            } else {
                choixButtons[i].setText(String.valueOf(genererChoixAleatoire()));
            }
        }

        // Mise à jour de l'affichage du numéro de la question et du score
        scoreLabel.setText("Score: " + score);
        ((JLabel) topPanel.getComponent(0)).setText("Question: " + numQuestion);
    }

    private void verifierReponse(int choix) {
        if (choixButtons[choix].getText().equals(String.valueOf(reponseCorrecte))) {
            resultatLabel.setText("Correct!");
            playSound("/home/djalal/Desktop/javaa/Sounds/mixkit-correct-answer-notification-947.wav");
            score++;
        } else {
            resultatLabel.setText("Incorrect. La réponse correcte est : " + reponseCorrecte);
            playSound("/home/djalal/Desktop/javaa/Sounds/mixkit-click-error-1110.wav");
        }

        stopChrono();
        initialiserJeu();
    }

    private int calculerReponse() {
        switch (operateur) {
            case '+':
                return nombre1 + nombre2;
            case '-':
                return nombre1 - nombre2;
            case '*':
                return nombre1 * nombre2;
            case '/':
                return nombre1 / nombre2;
            default:
                throw new IllegalArgumentException("Opérateur non pris en charge: " + operateur);
        }
    }

    private char getOperateur(int index) {
        char[] operateurs = {'+', '-', '*', '/'};
        return operateurs[index];
    }

    private int genererChoixAleatoire() {
        Random random = new Random();
        return random.nextInt(20); // Modifiez la limite selon vos besoins
    }

    private void startChrono() {
        chrono.start();
    }

    private void stopChrono() {
        if (chrono != null) {
            chrono.stop();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new quiz("Jeu de Maths"));
    }
     public static void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}