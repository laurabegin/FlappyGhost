// Travail de Laura Bégin et Francis Boudreau

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Separator;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Classe de la vue (FlappyGhost)
 */
public class FlappyGhost extends Application {

    public static final int SCENEWIDTH = 640;
    private static final int SCENEHEIGHT = 440;
    public static final int BGHEIGHT = 400;

    final ToggleButton leftPause = new ToggleButton("Pause");
    final CheckBox centerCheckBox = new CheckBox("Mode debug");
    private final Text rightScore = new Text("Score: 0   ");
    final Image icon = new Image("/img/ghost.png");
    private Background background;
    private final Canvas canvas = new Canvas(SCENEWIDTH, BGHEIGHT);
    private final GraphicsContext context = canvas.getGraphicsContext2D();

    private AnimationTimer timer;
    private boolean modeDebug;

    private Controller controller;
    private Player ghost;

    /**
     * Méthode start de l'application, appelée pour démarrer le jeu
     *
     * @param primaryStage stage principal du jeu
     * @throws Exception tout type d'exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox root = new VBox();
        Scene scene = new Scene(root, SCENEWIDTH, SCENEHEIGHT);
        HBox menu = new HBox();

        Separator sep1 = new Separator(Orientation.VERTICAL);
        sep1.setValignment(VPos.CENTER);
        sep1.setPrefHeight(40);

        Separator sep2 = new Separator(Orientation.VERTICAL);
        sep2.setValignment(VPos.CENTER);
        sep2.setPrefHeight(40);

        menu.getChildren().add(leftPause);
        menu.getChildren().add(sep1);
        menu.getChildren().add(centerCheckBox);
        menu.getChildren().add(sep2);
        menu.getChildren().add(rightScore);
        menu.setAlignment(Pos.CENTER);

        // Instanciation du contrôler
        controller = new Controller(this);
        ghost = controller.getGhost();
        background = controller.getBackground();

        Pane pane = new Pane(background.getBg1(), background.getBg2(), canvas);
        root.getChildren().add(pane);
        root.getChildren().add(new Separator());
        root.getChildren().add(menu);

        // Animation du jeu
        timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void start() {
                lastTime = System.nanoTime();
                super.start();
            }

            // Méthode appelée à chaque frame
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) * 1e-9;

                context.clearRect(0, 0, SCENEWIDTH, BGHEIGHT);
                controller.draw(ghost);

                ghost.update(deltaTime);
                controller.manageObstacles(deltaTime);
                background.moveBg(ghost.getDisplacementPerFrame());

                controller.checkIfLost();
                lastTime = now;
            }
        };

        timer.start();

        // Affichage du jeu
        primaryStage.setTitle("Flappy Ghost");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.getIcons().add(icon);

        // Focus automatique sur le canvas
        Platform.runLater(() -> canvas.requestFocus());

        // Refocus sur le canvas si l'utilisateur clique ailleurs sur la scène
        scene.setOnMouseClicked((event) -> canvas.requestFocus());

        // Demande au contrôleur de gérer les événements de l'interface
        controller.handleKeyboard(scene);
        controller.handlePause(leftPause);
        controller.handleDebug(centerCheckBox);
    }

    /**
     * Getter du contexte graphique
     *
     * @return le contexte graphique du canvas
     */
    public GraphicsContext getContext() {
        return context;
    }

    /**
     * Retourne un booléen de l'état du mode débug
     *
     * @return true si le jeu est en mode débug, false sinon
     */
    public boolean isModeDebug() {
        return modeDebug;
    }

    /**
     * Active ou désactive le mode débug
     *
     * @param modeDebug true pour activer mode débug, false pour le désactiver
     */
    public void setModeDebug(boolean modeDebug) {
        this.modeDebug = modeDebug;
    }

    /**
     * Getter du score affiché à la droite du menu
     *
     * @return le score affiché à droite du menu
     */
    public Text getRightScore() {
        return rightScore;
    }

    /**
     * Getter de l'animation timer
     *
     * @return l'animation timer
     */
    public AnimationTimer getTimer() {
        return timer;
    }

    /**
     * Getter du canvas
     *
     * @return le canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
