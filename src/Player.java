import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Classe du joueur (le fantôme)
 */
public class Player extends Entity {
    private int score;
    private double displacementPerFrame;
    private final Paint color = Color.BLACK;


    /**
     * Instancie un nouveau objet Player
     *
     * @param x the x
     * @param y the y
     */
    public Player(double x, double y, Controller controller) {
        super(x, y, 150, 0, 500, new Image("img/ghost.png", 60, 60, false, false), 30, controller);
        this.score = 0;
    }

    /**
     *  Fais sauter le joueur
     */
    public void jump() {
        this.setVy(-300);

    }

    /**
     * Met a jour l'objet
     *
     * @param dt  le temps depuis le dernier frame
     */
    public void update(double dt) {

        setDisplacementPerFrame(getVx() * dt);
        setVy(getVy() + dt * getAy());
        setY(getY() + dt * getVy());

        if (getVy() > 300) {
            setVy(300);
        }

        if (getVy() < -300) {
            setVy(-300);
        }

        if ((getY() + getR() * 2) > FlappyGhost.BGHEIGHT || getY() < 0) {
            setVy(getVy() * -1);
        }

        setY(Math.min(getY(), FlappyGhost.BGHEIGHT - getR() * 2));
        setY(Math.max(getY(), 0));

    }

    public void moreGravitySpeed() {
        if (this.getScore() % 10 == 0) {//If 2 obstacles have been passed,increments gravity and speed
            this.addAY();
            this.addVX();
        }
    }

    /**
     * Incremente la vitesse en x
     */
    public void addVX() {
        this.setVx(this.getVx() + 15);
    }

    /**
     * Incremente l'acceleration
     */
    public void addAY() {
        this.setAy(this.getAy() + 15);
    }

    /**
     * Incremente le score
     */
    public void updateScore() {
        this.score += 5;
    }

    /**
     * Getter du score
     *
     * @return le score
     */
    public int getScore () {
        return this.score;
    }

    /**
     * Getter du déplacement par frame
     *
     * @return déplacement par frames
     */
    public double getDisplacementPerFrame() {
        return displacementPerFrame;
    }

    /**
     * Setter du déplacement par frames
     *
     * @param displacementPerFrame déplacement par frames
     */
    public void setDisplacementPerFrame(double displacementPerFrame) {
        this.displacementPerFrame = displacementPerFrame;
    }

    public Paint getColor() {
        return color;
    }

    /**
     * Setter du score
     *
     * @param score score
     */
    public void setScore(int score) {
        this.score = score;
    }
}

