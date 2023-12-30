import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class quiz extends JFrame {

    private JLabel questionLabel, resultatLabel, scoreLabel, tempsRestantLabel;
    private Button[] choixButtons;
    private int niveau;
    private int play;
    private int score;
    private int nombre1, nombre2, reponseCorrecte;
    private char operateur;
    private int numQuestion;
    private Timer chrono; // Timer pour le chronomètre7
    private int tempsRestant; // Temps restant en secondes
    private JPanel topPanel;
    public Clip timer,error,correct;
String[] niveaux = {"Facile", "Difficile"};
    public quiz(String titre) {
 super(titre);
  setDefaultCloseOperation(quiz.EXIT_ON_CLOSE);
   try {
         
           File fontFile = new File("/home/djalal/Desktop/javaa/ihmproject/Fonts/Playtime.otf");
            Font playf = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(50f);

            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(playf);

           
       

       

        // Demander le niveau à l'utilisateur
      
 
        niveau = JOptionPane.showOptionDialog(null, "Choisissez le niveau", "Niveau", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, niveaux, niveaux[0]) + 1;
                
        // Initialisation des composants
     
        questionLabel = new JLabel();
        questionLabel.setFont(playf); 
        resultatLabel = new JLabel();
        
        scoreLabel = new JLabel("Score: 0");
        tempsRestantLabel = new JLabel();
        choixButtons = new Button[4];
        for (int i = 0; i < 4; i++) {

            choixButtons[i] = new Button ();
          choixButtons[i].setRound(20);
            choixButtons[i].setFont(playf); 
         
            
             
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
        topPanel.setBackground(new Color(255, 245, 224));

        // Panel for the center section (Question, Result, and Choices)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(255, 245, 224));

        // Panel for the question
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        questionPanel.add(questionLabel);
        centerPanel.add(questionPanel);
        questionPanel.setBackground(new Color(255, 245, 224));

        // Panel for the choices
        JPanel choicesPanel = new JPanel(new GridLayout(2, 2));
        for (int i = 0; i < 4; i++) {
            choicesPanel.add(choixButtons[i]);
        }
        centerPanel.add(choicesPanel);
questionPanel.setBackground(new Color(255, 245, 224));

        // Panel for the result
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultPanel.add(resultatLabel);
        centerPanel.add(resultPanel);
        resultPanel.setBackground(new Color(255, 245, 224));
        add(centerPanel, BorderLayout.CENTER);

        // Panel for the bottom section (Result)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(resultatLabel);
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBackground(new Color(255, 245, 224));

        // Ajout du label et du chronomètre seulement pour le niveau difficile
        if (niveau == 2) {
            tempsRestantLabel.setText("Temps restant: ");
            chrono = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tempsRestant--;
                    tempsRestantLabel.setText("Temps restant: " + tempsRestant + "s");
                    if (tempsRestant==4) {
                        timer=playSound("/home/djalal/Desktop/javaa/ihmproject/Sounds/mixkit-tick-tock-clock-timer-1045.wav",timer);
                    }
                    if (tempsRestant <= 0) {
                        resultatLabel.setText("Temps écoulé. La réponse correcte est : " + reponseCorrecte);
                        stopChrono();
                        stopSound(timer);
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
      
 } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
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
                     niveau = JOptionPane.showOptionDialog(null, "Choisissez le niveau", "Niveau", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, niveaux, niveaux[0]) + 1;
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
            nombre2 = random.nextInt(9)+1;
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
            playSound("/home/djalal/Desktop/javaa/ihmproject/Sounds/mixkit-correct-answer-notification-947.wav",correct);

            score++;
            stopSound(timer);
        } else {
            resultatLabel.setText("Incorrect. La réponse correcte est : " + reponseCorrecte);
            playSound("/home/djalal/Desktop/javaa/ihmproject/Sounds/mixkit-click-error-1110.wav",error);
            stopSound(timer);
        }

        stopChrono();
        stopSound(timer);
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
     public static int stopSound(Clip ssd){
          if (ssd == null) {
            return 0 ; 
          }
        ssd.stop();
            return 0 ;
        }
     public static Clip playSound(String soundFilePath,Clip sd) {
        try {
         sd = AudioSystem.getClip();
           
            
            sd.open(AudioSystem.getAudioInputStream(new File(soundFilePath)));
            sd.start();
            return sd ;
        } catch (Exception e) {
            e.printStackTrace();
            return sd ; 

        }
       
    }
}