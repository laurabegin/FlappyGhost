/**
 * Classe du type d'obstacle quantique
 */
public class ObstacleQuantum extends Obstacle {

    /**
     * la dernière fois qu'il s'est téléporté
     */
    private double lastTimeTeleport;

    /**
     * Instancie un nouvel obstacle quantique
     *
     * @param x          la position en x
     * @param y          la position en y
     * @param ghostSpeed la vitesse du fantome
     * @param radius     le rayon
     * @param number     le numero de l'image
     * @param controller la référence vers le contrôleur
     */
    public ObstacleQuantum(double x, double y, double ghostSpeed, int radius, int number, Controller controller) {
        super(x, y, ghostSpeed, 0, 0, radius, number, controller);
    }

    /**
     * Mets a jour lobstacle
     *
     * @param dt         le temps écoulé depuis la dernière update
     * @param ghostSpeed la vitesse du fantome
     */
    public void update(double dt, double ghostSpeed) {

        setVx(ghostSpeed);
        setX(getX() + dt * getVx());
        if (Controller.getTime() - lastTimeTeleport >= 0.2) {
            setX(getX() + Math.random() * 60 - 30);
            setY(getY() + Math.random() * 60 - 30);
            lastTimeTeleport = Controller.getTime();
        }
    }

}
