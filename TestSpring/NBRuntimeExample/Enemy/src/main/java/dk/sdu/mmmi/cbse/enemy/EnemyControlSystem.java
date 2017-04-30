/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.asteroid.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.bulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Daniel
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})

public class EnemyControlSystem implements IEntityProcessingService, IGamePluginService {

    private float[] shapex;
    private float[] shapey;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {

            if (enemy.getLife() <= 0) {
                world.removeEntity(enemy);
                enemy = null;
                System.out.println("Enemy killed - " + world.getEntities(Enemy.class).size() + " enemies left");
            } else {

                if (enemy.getIsHit()) {
                    enemy.setLife(enemy.getLife() - 1);
                    enemy.setIsHit(false);
                }

                float x = enemy.getX();
                float y = enemy.getY();

                float maxSpeed = enemy.getMaxSpeed();
                float acceleration = enemy.getAcceleration();

                float dx = enemy.getDx();
                float dy = enemy.getDy();

                float radians = enemy.getRadians();
                int rotationSpeed = enemy.getRotationSpeed();
                float dt = gameData.getDelta();

                setShape(enemy);

                enemy.setShapeX(shapex);
                enemy.setShapeY(shapey);

                if (enemy.getIsHit()) {
                    enemy.setLife(enemy.getLife() - 1);
                    enemy.setIsHit(false);
                }

                // turning
                int rnd = new Random().nextInt(20);
                if (rnd == 4 || rnd == 6 || rnd == 8) {
                    radians += rotationSpeed * gameData.getDelta();
                    enemy.setRadians(radians);
                } else if (rnd == 10) {
                    radians -= rotationSpeed * gameData.getDelta();
                    enemy.setRadians(radians);
                }

//         accelerating
                dx += Math.cos(radians) * (acceleration) * dt;
                dy += Math.sin(radians) * (acceleration) * dt;
                enemy.setDx(dx);
                enemy.setDy(dy);

                // deceleration
                float vec = (float) Math.sqrt(dx * dx + dy * dy);
//                if (vec > 0) {
//                    dx -= (dx / vec) * deceleration * gameData.getDelta();
//                    dy -= (dy / vec) * deceleration * gameData.getDelta();
//                    enemy.setDx(dx);
//                    enemy.setDy(dy);
//                }
                if (vec > maxSpeed * 5) {
                    dx = (dx / vec) * maxSpeed * 5;
                    dy = (dy / vec) * maxSpeed * 5;
                    enemy.setDx(dx);
                    enemy.setDy(dy);
                }

                // set position
                x += dx * gameData.getDelta();
                y += dy * gameData.getDelta();
                enemy.setX(x);
                enemy.setY(y);

                // screen wrap
                if (x < 0) {
                    enemy.setX(gameData.getDisplayWidth());
                }
                if (x > gameData.getDisplayWidth()) {
                    enemy.setX(0);
                }
                if (y < 0) {
                    enemy.setY(gameData.getDisplayHeight());
                }
                if (y > gameData.getDisplayHeight()) {
                    enemy.setY(0);
                }

                for (Entity e : world.getEntities(Asteroid.class)) {
                    findTarget(enemy, e, gameData, world);
                }
                for (Entity e : world.getEntities(Player.class)) {
                    findTarget(enemy, e, gameData, world);
                }

                enemy.setMaxSpeed(20);
                enemy.setExpiration(enemy.getExpiration() - gameData.getDelta());
                if (enemy.getExpiration() <= 0) {
                    if (new Random().nextBoolean()) {
                        enemy.setRadians(enemy.getRadians() + 0.3f);
                    } else {
                        enemy.setRadians(enemy.getRadians() - 0.3f);
                    }
                    enemy.setExpiration(3);
                }
            }
        }
    }

    private void findTarget(Entity enemy, Entity e, GameData gameData, World world) {
        float radians = enemy.getRadians();
        if (getDistance(enemy, e.getX(), e.getY())
                + getDistance(e, (float) (enemy.getX() + Math.cos(radians) * 800), (float) (enemy.getY() + Math.sin(radians) * 800))
                == getDistance(enemy, (float) (enemy.getX() + Math.cos(radians) * 800), (float) (enemy.getY() + Math.sin(radians) * 800))) {
//            shapex[3] = e.getX();
//            shapey[3] = e.getY();
            Random rnde = new Random();

            float dir = (float) Math.atan2(e.getY() - enemy.getY(), e.getX() - enemy.getX());
            enemy.setRadians(dir);
            if (rnde.nextInt(10) == 2) {

                bulletSPI bulletProvider = Lookup.getDefault().lookup(bulletSPI.class);

            if (bulletProvider != null) {
                Entity bullet = bulletProvider.createBullet(enemy, gameData);
                world.addEntity(bullet);
            }
            }
        }
    }

    private float getDistance(Entity enemy, float ex, float ey) {
        float x = enemy.getX();
        float y = enemy.getY();

        float d = (float) Math.sqrt(Math.pow((ex - x), 2) + Math.pow((ey - y), 2));
        int f = (int) d;
        return f;
    }

    private void setShape(Entity player) {

        float radians = player.getRadians();
        shapex = new float[4];
        shapey = new float[4];

        if (false) {
            shapex = new float[6];
            shapey = new float[6];

            shapex[0] = (float) (player.getX() + Math.cos(radians) * 8);
            shapey[0] = (float) (player.getY() + Math.sin(radians) * 8);

            shapex[1] = (float) (player.getX() + Math.cos(radians - 4 * 3.1415f / 5) * 8);
            shapey[1] = (float) (player.getY() + Math.sin(radians - 4 * 3.1145f / 5) * 8);

            shapex[2] = (float) (player.getX() + Math.cos(radians + 3.1415f) * 5);
            shapey[2] = (float) (player.getY() + Math.sin(radians + 3.1415f) * 5);
            //Test
            shapex[3] = (float) (player.getX() + Math.cos(radians) * 800);
            shapey[3] = (float) (player.getY() + Math.sin(radians) * 800);

            shapex[4] = (float) (player.getX() + Math.cos(radians + 3.1415f) * 5);
            shapey[4] = (float) (player.getY() + Math.sin(radians + 3.1415f) * 5);

            shapex[5] = (float) (player.getX() + Math.cos(radians + 4 * 3.1415f / 5) * 8);
            shapey[5] = (float) (player.getY() + Math.sin(radians + 4 * 3.1415f / 5) * 8);
            //....
        }else{
        shapex[0] = (float) (player.getX() + Math.cos(radians) * 8);
        shapey[0] = (float) (player.getY() + Math.sin(radians) * 8);

        shapex[1] = (float) (player.getX() + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (player.getY() + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (player.getX() + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (player.getY() + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (player.getX() + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (player.getY() + Math.sin(radians + 4 * 3.1415f / 5) * 8);
        }
    }

    private Entity createPlayerShip(GameData gameData) {

        Entity enemyShip = new Enemy();

        Random rnd = new Random();
        enemyShip.setPosition(rnd.nextInt(gameData.getDisplayWidth()), rnd.nextInt(gameData.getDisplayHeight()));

        enemyShip.setMaxSpeed(300);
        enemyShip.setAcceleration(200);
        enemyShip.setDeacceleration(10);
        enemyShip.setLife(10);
        enemyShip.setExpiration(3);

        enemyShip.setRadius(10);

        int r = new Random().nextInt(32);
        float direction = (3.1415f / 8) * r;
        enemyShip.setRadians(direction);
        enemyShip.setRotationSpeed(4);

        return enemyShip;
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        for (int i = 0; i < 4; i++) {
            Entity enemy = createPlayerShip(gameData);
            world.addEntity(enemy);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        //world.removeEntity(enemy);    
    }
}
