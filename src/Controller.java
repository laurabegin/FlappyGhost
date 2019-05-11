import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/**
 * Contrôleur du jeu
 */
public class Controller {

    private final FlappyGhost view;
    private final Player ghost;
    private static double time;
    private ArrayList<Obstacle> ObstacleList;
    private final Background background;
    private Boolean restarting = false;

    /**
     * Constructeur du contrôleur (Controller)
     *
     * @param view référence vers la vue du jeu
     */
    public Controller(FlappyGhost view) {
        this.view = view;
        this.ghost = new Player(FlappyGhost.SCENEWIDTH / 2 - 30, FlappyGhost.BGHEIGHT / 2, this);
        ObstacleList = new ArrayList<>();
        background = new Background(FlappyGhost.BGHEIGHT, FlappyGhost.SCENEWIDTH);
    }

    /**
     * Dessine toutes les entités sur le canvas
     *
     * @param entity Entité à dessiner (obstacle ou fantôme)
     */
    public void draw(Entity entity) {

        // Si le jeu est en mode débug
        if (view.isModeDebug()) {

            if (entity instanceof Obstacle && entity.isIntersected()) {
                view.getContext().setFill(Color.RED);
                view.getContext().fillOval(entity.getX(), entity.getY(), entity.getR() * 2, entity.getR() * 2);

            } else {
                view.getContext().setFill(entity.getColor());
                view.getContext().fillOval(entity.getX(), entity.getY(), entity.getR() * 2, entity.getR() * 2);
            }

            // Si le jeu n'est pas en mode débug
        } else {
            view.getContext().drawImage(entity.getImg(), entity.getX(), entity.getY());
        }
    }

    /**
     * Gère les obstacles à chaque frame
     *
     * @param dt delta-time
     */
    public void manageObstacles(double dt) {
        time += dt;

        if (!restarting) {

            // Ajoute un obstacle à chaque trois secondes
            if (((long) time) % 3 == 0 && ((long) (time - dt)) % 3 != 0) {
                ObstacleList.add(addObstacle(-ghost.getVx()));
            }

            // Itère au travers tous les obstacles
            for (int i = 0; i < ObstacleList.size(); i++) {
                Obstacle obstacle = ObstacleList.get(i);
                obstacle.moveObstacle(dt, ghost);
                Entity.entityIntersection(obstacle, ghost);

                // Vérifie si l'obstacle a été compté dans le score
                if (ghost.getX() > obstacle.getX() && !obstacle.isCounted()) {
                    updateScore(ghost, obstacle);
                    obstacle.setCounted();
                    ghost.moreGravitySpeed();
                }

                // Vérifie si l'obstacle est à l'extérieur de la fanêtre de jeu
                if (obstacle.getX() + 100 < 0) {
                    removeObstacle(obstacle);
                }
            }
        }
    }

    /**
     * Met à jour le score du fantôme et l'affiche dans la vue
     *
     * @param ghost    le joueur
     * @param obstacle l'obstacle passé par le joueur
     */
    public void updateScore(Player ghost, Obstacle obstacle) {
        ghost.updateScore();
        view.getRightScore().setText("Score: " + ghost.getScore());
    }

    /**
     * Retire l'obstacle de la liste
     *
     * @param obstacle l'obstacle à retirer
     */
    public void removeObstacle(Obstacle obstacle) {
        ObstacleList.remove(obstacle);
    }

    /**
     * Vérifie si la partie est perdue. Si oui, la réinitialise.
     */
    public void checkIfLost() {
        if (ghost.isIntersected() && !view.isModeDebug()) {
            restart();
        }
    }

    /**
     * Gère les événements du clavier
     *
     * @param scene scène du jeu
     */
    public void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(event -> {

            // Barre d'espace pour sauter
            if ((event.getCode()) == KeyCode.SPACE) {
                ghost.jump();
            }

            // Touche escape pour quitter le jeu
            if ((event.getCode()) == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
    }

    /**
     * Gère les événements du bouton pause
     *
     * @param pause bouton pause
     */
    public void handlePause(ToggleButton pause) {
        pause.setOnAction((event) -> {

            if (pause.isSelected()) {
                try {
                    view.getTimer().stop();
                    pause.setText("Resume");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    view.getTimer().start();
                    pause.setText("Pause");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Pour empêcher la barre d'espace d'activer le bouton
            Platform.runLater(() -> view.getCanvas().requestFocus());
        });
    }

    /**
     * Gère les événements du checkbox mode débug
     *
     * @param debug Checkbox du mode débug
     */
    public void handleDebug(CheckBox debug) {
        debug.setOnAction((event) -> {

            if (debug.isSelected()) {
                view.setModeDebug(true);
            } else {
                view.setModeDebug(false);
            }

            Platform.runLater(() -> {

                // Pour empêcher la barre d'espace d'activer le bouton
                view.getCanvas().requestFocus();

                // Pour que l'affichage soit mis à jour même quand le jeu est sur pause
                view.getContext().clearRect(0, 0, FlappyGhost.SCENEWIDTH, FlappyGhost.BGHEIGHT);
                draw(ghost);
                manageObstacles(0);
            });
        });
    }

    /**
     * Ajoute un obstacle dans le jeu au hasard parmi les trois types
     *
     * @param ghostSpeed vitesse du fantôme
     * @return l'obstacle ajouté au jeu
     */
    public Obstacle addObstacle(double ghostSpeed) {
        int choose = (int) (Math.random() * 3);
        Obstacle result = null;

        double y = Math.random() * (FlappyGhost.BGHEIGHT - 70) + 35;
        double x = FlappyGhost.SCENEWIDTH + 45;

        int number = (int) (Math.random() * 26);
        int radius = (int) (Math.random() * 35) + 10;

        switch (choose) {
            case 0:
                result = new ObstacleQuantum(x, y, ghostSpeed, radius, number, this);
                break;

            case 1:
                result = new ObstacleSimple(x, y, ghostSpeed, radius, number, this);
                break;

            case 2:
                result = new ObstacleSinus(x, y, ghostSpeed, radius, number, this);
                break;
        }

        return result;
    }

    /**
     * Réinitialise le jeu avec les valeurs par défaut
     */
    public void restart() {

        restarting = true;
        ghost.setScore(0);
        view.getRightScore().setText("Score: 0   ");
        ghost.setVy(0);
        ghost.setVx(150);
        ghost.setX(FlappyGhost.SCENEWIDTH / 2.0 - 30);
        ghost.setY(FlappyGhost.BGHEIGHT / 2.0);
        ghost.setAy(500);
        background.getBg1().setX(0);
        background.getBg2().setX(background.getInitX());

        ObstacleList = new ArrayList<>();
        restarting = false;
    }

    /**
     * Getter de la référence vers le joueur
     *
     * @return la référence vers le joueur
     */
    public Player getGhost() {
        return this.ghost;
    }

    /**
     * Getter du temps de jeu
     *
     * @return temps de jeu depuis le début
     */
    public static double getTime() {
        return time;
    }

    /**
     * Getter de l'arrière-plan
     *
     * @return l'arrière-plan
     */
    public Background getBackground() {
        return this.background;
    }

}


