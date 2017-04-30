package dk.sdu.mmmi.cbse.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.UP;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.bullet.bulletSPI;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Daniel Åside
 */

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
        ,
    @ServiceProvider(service = IGamePluginService.class)
})

public class PlayerControlSystem implements IEntityProcessingService, IGamePluginService {

    float shapex[];
    float shapey[];
    
    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {

            if (player.getLife() <= 0) {
                world.removeEntity(player);
                player = null;
                System.out.println("GAME OVER!");
            } else {

                if (player.getIsHit()) {
                    player.setLife(player.getLife() - 1);
                    player.setIsHit(false);
                }

                float x = player.getX();
                float y = player.getY();

                float maxSpeed = player.getMaxSpeed();
                float acceleration = player.getAcceleration();
                float deceleration = player.getDeacceleration();

                float dx = player.getDx();
                float dy = player.getDy();

                float radians = player.getRadians();
                int rotationSpeed = player.getRotationSpeed();
                float dt = gameData.getDelta();

                setShape(player);

                player.setShapeX(shapex);
                player.setShapeY(shapey);

                if (player.getIsHit()) {
                    player.setLife(player.getLife() - 1);
                    player.setIsHit(false);
                }

                // turning
                if (gameData.getKeys().isDown(LEFT)) {
                    radians += rotationSpeed * gameData.getDelta();
                    player.setRadians(radians);
                } else if (gameData.getKeys().isDown(RIGHT)) {
                    radians -= rotationSpeed * gameData.getDelta();
                    player.setRadians(radians);
                }

//         accelerating
                if (gameData.getKeys().isDown(UP)) {
                    dx += Math.cos(radians) * acceleration * dt;
                    dy += Math.sin(radians) * acceleration * dt;
                    player.setDx(dx);
                    player.setDy(dy);
                }

                bulletSPI bulletProvider = Lookup.getDefault().lookup(bulletSPI.class);

            if (gameData.getKeys().isDown(SPACE) && bulletProvider != null) {
                Entity bullet = bulletProvider.createBullet(player, gameData);
                world.addEntity(bullet);
            }

                // deceleration
                float vec = (float) Math.sqrt(dx * dx + dy * dy);
                if (vec > 0) {
                    dx -= (dx / vec) * deceleration * gameData.getDelta();
                    dy -= (dy / vec) * deceleration * gameData.getDelta();
                    player.setDx(dx);
                    player.setDy(dy);
                }
                if (vec > maxSpeed) {
                    dx = (dx / vec) * maxSpeed;
                    dy = (dy / vec) * maxSpeed;
                    player.setDx(dx);
                    player.setDy(dy);
                }

                // set position
                x += dx * gameData.getDelta();
                y += dy * gameData.getDelta();
                player.setX(x);
                player.setY(y);

                // screen wrap
                if (x < 0) {
                    player.setX(gameData.getDisplayWidth());
                }
                if (x > gameData.getDisplayWidth()) {
                    player.setX(0);
                }
                if (y < 0) {
                    player.setY(gameData.getDisplayHeight());
                }
                if (y > gameData.getDisplayHeight()) {
                    player.setY(0);
                }
            }
        }
        // TODO: Implement entity processor
    }

    private void setShape(Entity player) {

        float radians = player.getRadians();
        shapex = new float[4];
        shapey = new float[4];
        shapex[0] = (float) (player.getX() + Math.cos(radians) * 8);
        shapey[0] = (float) (player.getY() + Math.sin(radians) * 8);

        shapex[1] = (float) (player.getX() + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (player.getY() + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (player.getX() + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (player.getY() + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (player.getX() + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (player.getY() + Math.sin(radians + 4 * 3.1415f / 5) * 8);
    }

    @Override
    public void stop(GameData gameData, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Entity createPlayerShip(GameData gameData) {
        Entity playerShip = new Player();

        playerShip.setPosition(gameData.getDisplayWidth() / 3, gameData.getDisplayHeight() / 3);

        playerShip.setMaxSpeed(300);
        playerShip.setAcceleration(200);
        playerShip.setDeacceleration(10);
        playerShip.setLife(4);

        playerShip.setRadius(10);
        playerShip.setRadians(3.1415f / 2);
        playerShip.setRotationSpeed(3);

        return playerShip;
    }
}
