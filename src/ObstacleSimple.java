/**
 * Classe du type d'obstacle simple
 */
public class ObstacleSimple extends Obstacle {

    /**
     * Instancie un nouvel Obstacle Simple
     *
     * @param x          la position en x
     * @param y          la position en y
     * @param ghostSpeed la vitesse du fantome
     * @param radius     le rayon de l'obstacle
     * @param number     le numero de l'image
     * @param controller la référence vers le contrôleur
     */
    public ObstacleSimple(double x, double y, double ghostSpeed, int radius, int number, Controller controller) {
        super(x, y, ghostSpeed, 0, 0, radius, number, controller);
    }

    /**
     * Met à jour l`objet
     *
     * @param dt         le temps écoulé depuis la dernière update
     * @param ghostSpeed la vitesse du fantome
     */
    public void update(double dt, double ghostSpeed) {

        setVx(ghostSpeed);
        setX(getX() + dt * getVx());
    }
}
