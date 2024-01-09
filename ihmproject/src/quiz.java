
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class quiz extends JPanel {
    public static class JFRAME extends JFrame {
        public JFRAME() {
            super("Jeu pour enfant!");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
        }
    }

    private JLabel questionLabel, resultatLabel, scoreLabel, tempsRestantLabel;
    JFRAME q = new JFRAME();
    private Button[] choixButtons, fakebuttons;
    private int niveau;
    private int play;
    private int score;
    private int nombre1, nombre2, reponseCorrecte;
    private char operateur;
    private int numQuestion;
    public Timer chrono; // Timer pour le chronomètre7
    private int tempsRestant; // Temps restant en secondes
    private JPanel topPanel;
    public Clip timer, error, correct;
    private static CardLayout CardLayout = new CardLayout();
    private static JPanel container = new JPanel(CardLayout);

    String[] niveaux = { "Facile", "Difficile" };

    public quiz(String titre) {

        try {

            File fontFile = new File("Fonts/Playtime.otf");
            Font playf = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(50f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(playf);

            // Initialisation de l'interface
           JPanel panel = new JPanel();
            ImageIcon imageIcon = new ImageIcon(quiz.this.getClass().getResource("images/Happy Morning Animation.gif"));

            JLabel background = new JLabel(imageIcon);

            imageIcon.setImageObserver(background);

            q.setContentPane(background);
            q.setLayout(new BorderLayout());
            q.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            q.setResizable(false);

            // Demander le niveau à l'utilisateur

            JPanel startpanel = Creatstart();

            panel.setOpaque(false);

            JButton startButton = new JButton("Facile");
            JButton startButton2 = new JButton("Difficile");

            // button facile
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numQuestion = 0;
                    niveau = 1;
                    initialiserJeu();
                    CardLayout.show(container, "panelall");

                }
            });
            // button difficile
            startButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numQuestion = 0;
                    niveau = 2;

                    initialiserJeu();

                    CardLayout.show(container, "panelall");

                }

            });

            // Initialisation des composants

            questionLabel = new JLabel();
            questionLabel.setFont(playf);
            resultatLabel = new JLabel();

            scoreLabel = new JLabel("Score: 0");
            tempsRestantLabel = new JLabel();
            choixButtons = new Button[4];
            fakebuttons = new Button[6];
            for (int i = 0; i < 6; i++) {
                fakebuttons[i] = new Button();
                fakebuttons[i].setVisible(false);

            }
            for (int i = 0; i < 4; i++) {

                choixButtons[i] = new Button();

                choixButtons[i].setFont(playf);
                choixButtons[i].setPreferredSize(new Dimension(0, 0));

            }

            // Configuration de l'interface

            // Panel for the top section (Question number, Timer, Score)

            topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            topPanel.add(new JLabel("Question: " + (numQuestion - 1)));

            topPanel.add(scoreLabel);

            // Panel for the center section (Question, Result, and Choices)
            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

            // Panel for the question
            JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            questionPanel.add(questionLabel);
            questionPanel.setSize(20, 20);
            centerPanel.add(questionPanel);
            questionPanel.setOpaque(false);

            // Panel for the choices
            JPanel choicesPanel = new JPanel(new GridLayout(2, 4));

            choicesPanel.add(fakebuttons[0]);
            choicesPanel.add(choixButtons[0]);
            choicesPanel.add(fakebuttons[1]);
            choicesPanel.add(choixButtons[1]);
            choicesPanel.add(fakebuttons[2]);
            choicesPanel.add(fakebuttons[3]);
            choicesPanel.add(choixButtons[2]);
            choicesPanel.add(fakebuttons[4]);
            choicesPanel.add(choixButtons[3]);
            choicesPanel.add(fakebuttons[5]);

            centerPanel.add(choicesPanel);
            // imodaskdoisandas

            // Panel for the result
            JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel panelall = new JPanel();
            panelall.setLayout(new BorderLayout());
            panelall.add(centerPanel, BorderLayout.CENTER);
            panelall.add(topPanel, BorderLayout.NORTH);
            panelall.add(bottomPanel, BorderLayout.SOUTH);
            resultPanel.add(resultatLabel);
            centerPanel.add(resultPanel);
            resultPanel.setOpaque(false);

            topPanel.setOpaque(false);
            bottomPanel.setOpaque(false);
            centerPanel.setOpaque(false);
            panelall.setOpaque(false);
            // Panel for the bottom section (Result)

            bottomPanel.add(resultatLabel);
            panel.add(startButton);
            panel.add(startButton2);
            container.add(startpanel,"start");
            container.add(panel, "panel");
            container.add(panelall, "panelall");
            container.setOpaque(false);

            q.add(container);

            // Ajout du label et du chronomètre seulement pour le niveau difficile

            if (niveau == 2) {
                tempsRestantLabel.setText("Temps restant: ");
                chrono = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tempsRestant--;
                        tempsRestantLabel.setText("Temps restant: " + tempsRestant + "s");
                        if (tempsRestant == 4) {
                            timer = playSound(
                                    "Sounds/mixkit-tick-tock-clock-timer-1045.wav",
                                    timer);
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

            choicesPanel.setVisible(true);

            choicesPanel.setOpaque(false);
            // Initialisation du jeu
            initialiserJeu();

            // Affichage de la fenêtre
            q.pack();
            q.setLocationRelativeTo(null); // Center the frame on the screen

            q.setVisible(true);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void initialiserJeu() {
        if (niveau == 2) {

            topPanel.add(tempsRestantLabel);
        }

        if (numQuestion >= 10) {
            // Afficher un message de fin du jeu
            replay();
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
            nombre2 = random.nextInt(9) + 1;
            operateur = getOperateur(random.nextInt(4));
            tempsRestant = 10; // Changer la valeur du temps pour le niveau difficile
            tempsRestantLabel.setText("Temps restant: " + tempsRestant + "s");

        }

        // Calcul de la réponse correcte
        reponseCorrecte = calculerReponse();

        // Affichage de la question
        questionLabel.setText(
                "Question " + numQuestion + ": Combien font " + nombre1 + " " + operateur + " " + nombre2 + "?");

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
            playSound("Sounds/mixkit-correct-answer-notification-947.wav",
                    correct);

            score++;
            stopSound(timer);
        } else {
            resultatLabel.setText("Incorrect. La réponse correcte est : " + reponseCorrecte);
            playSound("Sounds/mixkit-click-error-1110.wav", error);
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
        char[] operateurs = { '+', '-', '*', '/' };
        return operateurs[index];
    }

    private int genererChoixAleatoire() {
        Random random = new Random();
        return random.nextInt(20); // Modifiez la limite selon vos besoins
    }

    public void startChrono() {
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

    public static int stopSound(Clip ssd) {
        if (ssd == null) {
            return 0;
        }
        ssd.stop();
        return 0;
    }

    private void replay() {
        JPanel replay = new JPanel();
        JButton replayButton = new JButton("Rejouer");
        JButton exitButton = new JButton("Quitter");
        if (score < 5) {
            JLabel loss = new JLabel("FIN DE JEU VOUS AVEZ PERDU VOTRE SCORE: " + score);
            replay.add(loss);

        } else {
            JLabel win = new JLabel("FIN DE JEU VOUS AVEZ GAGNER VOTRE SCORE: " + score);
            replay.add(win);
        }
        replay.add(replayButton);
        replay.add(exitButton);
        container.add(replay, "replay");
        CardLayout.show(container, "replay");

        replayButton.addActionListener(e -> {
            CardLayout.show(container, "panel");
            score = 0;
            numQuestion = 1;
            initialiserJeu();

        });
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

    }

    public static Clip playSound(String soundFilePath, Clip sd) {
        try {
            sd = AudioSystem.getClip();

            sd.open(AudioSystem.getAudioInputStream(new File(soundFilePath)));
            sd.start();
            return sd;
        } catch (Exception e) {
            e.printStackTrace();
            return sd;

        }

    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    // start panel
    public JPanel Creatstart() {

        JPanel firstJPanel = new JPanel(new CardLayout());
        JPanel p = new JPanel(new BorderLayout());

        firstJPanel.add(p);

        JPanel northPanel = createPanel(Color.RED.darker(), 100, 50);
        JPanel southPanel = createPanel(Color.RED.darker(), 300, 170);
        JPanel eastPanel = createPanel(Color.RED.darker(), 200, 400);
        JPanel westPanel = createPanel(Color.RED.darker(), 200, 400);
        southPanel.setLayout(new FlowLayout());
        eastPanel.setLayout(new FlowLayout());
        westPanel.setLayout(new FlowLayout());
        JLabel labil = new JLabel("bienvenue dans le jeu");
        labil.setFont(new Font("SansSerif", Font.PLAIN, 30));
        labil.setForeground(new Color(0, 0, 128).darker());
        labil.setHorizontalAlignment(0);
        northPanel.add(labil);
        // dessin du bas
        ImageIcon imageIcon2 = new ImageIcon(
                "C:/Users/Client/jeu/javaa/javaa/ihmproject/src_2/Symmetry-removebg-preview.png");
        Image image2 = imageIcon2.getImage();
        Image resizedImage2 = image2.getScaledInstance(250, 150, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon2 = new ImageIcon(resizedImage2);
        JLabel label2 = new JLabel(resizedImageIcon2);
        // dessin du droite
        ImageIcon imageIcon = new ImageIcon(
                "C:/Users/Client/jeu/javaa/javaa/ihmproject/src_2/pngegg.png");
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(190, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon = new ImageIcon(resizedImage);
        JLabel label = new JLabel(resizedImageIcon);
        // dessin du gauche
        ImageIcon imageIcon3 = new ImageIcon(
                "C:/Users/Client/jeu/javaa/javaa/ihmproject/src_2/pngegg (1).png");
        Image image3 = imageIcon3.getImage();
        Image resizedImage3 = image3.getScaledInstance(150, 160, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon3 = new ImageIcon(resizedImage3);
        JLabel label3 = new JLabel(resizedImageIcon3);
        westPanel.add(label3);
        eastPanel.add(label);
        southPanel.add(label2);

        p.add(northPanel, BorderLayout.NORTH);
        p.add(southPanel, BorderLayout.SOUTH);
        p.add(eastPanel, BorderLayout.EAST);
        p.add(westPanel, BorderLayout.WEST);
        p.add(createCentrPanel(), BorderLayout.CENTER);
        return p;

    }

    public static JPanel createCentrPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.red.darker());
        ImageIcon imageIcon = new ImageIcon(
                "C:/Users/Client/jeu/javaa/javaa/ihmproject/src_2/Screenshot_2023-12-23_131722-removebg-preview.png");
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

        JButton b = new JButton("START!!");

        b.setBounds(120, 200, 150, 50);
        b.setBackground(new Color(255, 182, 193));
        b.setFont(new Font("SansSerif", Font.PLAIN, 20));
        b.setForeground(Color.RED.darker());
        b.setOpaque(true);
        b.setAlignmentX(0);

        b.setMargin(new Insets(10, 0, 0, 10));
        Border roundBorder = BorderFactory.createLineBorder(Color.black, 2, true);
        b.setBorder(roundBorder);
        b.addActionListener(e -> {
            CardLayout.show(container, "panel");
        });
        JLabel label = new JLabel(resizedImageIcon);
        label.setBounds(65, 180, 50, 50);
        JLabel label1 = new JLabel("JOUANT AVEC LES MATHS");
        label1.setFont(new Font("SansSerif", Font.PLAIN, 25));
        label1.setForeground(new Color(0, 0, 128).darker());
        label1.setBounds(30, 40, 400, 50);
        ImageIcon imageIcon2 = new ImageIcon(
                "C:/Users/Client/jeu/javaa/javaa/ihmproject/src_2/Screenshot_2023-12-23_131102-removebg-preview.png");
        Image image2 = imageIcon2.getImage();
        Image resizedImage2 = image2.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon2 = new ImageIcon(resizedImage2);
        JLabel label2 = new JLabel(resizedImageIcon2);
        label2.setBounds(270, 230, 50, 50);

        panel.add(label2);
        panel.add(label);
        panel.add(label1);
        panel.add(b);
        return panel;
    }

    private static JPanel createPanel(Color Color, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(Color);
        panel.setPreferredSize(new java.awt.Dimension(width, height));
        return panel;
    }
}
