import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Classe de l'arrière-plan (Background)
 */

public class Background {

    public final int bgHeight;
    public final int bgWidth;

    private Player ghost;
    private final Image img1 = new Image("img/bg.png");
    private final Image img2 = new Image("img/bg.png");
    private final double initX;

    private final ImageView bg1 = new ImageView(img1);
    private final ImageView bg2 = new ImageView(img2);

    /**
     * Constructeur de Background
     *
     * @param bgHeight Hauteur de l'arrière-plan
     * @param bgWidth  Largeur de l'arrière-plan
     */
    public Background(int bgHeight, int bgWidth) {
        this.bgHeight = bgHeight;
        this.bgWidth = bgWidth;
        bg2.setX(bgWidth);
        initX = bgWidth;
    }

    /**
     * Getter du premier arrière-plan
     *
     * @return bg1, le premier arrière-plan
     */
    public ImageView getBg1() {
        return bg1;
    }

    /**
     * Getter du deuxième arrière-plan
     *
     * @return bg2, le deuxième arrière-plan
     */
    public ImageView getBg2() {
        return bg2;
    }

    /**
     * Bouge l'arrière-plan selon la vitesse du joueur (fantôme)
     *
     * @param speed vitesse du joueur en pixel par frame
     */
    public void moveBg(Double speed) {
        bg1.setX(bg1.getX() - speed);
        bg2.setX(bg2.getX() - speed);

        if (bg2.getX() <= -bgWidth + speed) {
            bg2.setX(bgWidth);
        }

        if (bg2.getX() <= speed) {
            bg1.setX(bg2.getX() + bgWidth);
        }
    }

    /**
     * Getter de la position initiale du deuxième arrière-plan
     *
     * @return initX, position initiale du deuxième arrière-plan
     */
    public double getInitX() {
        return this.initX;
    }

}
