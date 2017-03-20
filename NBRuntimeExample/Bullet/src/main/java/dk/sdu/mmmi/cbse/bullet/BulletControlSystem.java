/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import static dk.sdu.mmmi.cbse.common.events.EventType.ENEMY_SHOOT;
import static dk.sdu.mmmi.cbse.common.events.EventType.PLAYER_SHOOT;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
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
public class BulletControlSystem implements IEntityProcessingService, IGamePluginService {

    float[] shapex;
    float[] shapey;

    @Override
    public void process(GameData gameData, World world) {

        if (!gameData.getEvents().isEmpty()) {
            for (Event event : gameData.getEvents()) {
                if (event.getType() == PLAYER_SHOOT) {
                    for (Entity entity : world.getEntities(PLAYER)) {
                        if (entity.getID().equals(event.getEntityID())) {
                            Entity bullet = new Entity();
                            bullet.setType(BULLET);
                            bullet.setPosition((float) (entity.getX() + entity.getRadius() * 2 * Math.cos(entity.getRadians())),
                                    (float) (entity.getY() + entity.getRadius() * 2 * Math.sin(entity.getRadians())));

                            bullet.setMaxSpeed(100);
                            bullet.setAcceleration(100);
                            bullet.setDeacceleration(10);

                            bullet.setRadians(entity.getRadians());
                            bullet.setRadius(6);
                            bullet.setRotationSpeed(1);
                            bullet.setExpiration(2);
                            bullet.setDx(entity.getDx());
                            bullet.setDy(entity.getDy());

                            setShape(bullet);
                            bullet.setShapeX(shapex);
                            bullet.setShapeY(shapey);
                            world.addEntity(bullet);
                            gameData.removeEvent(event);
                        }
                    }
                } else if (event.getType() == ENEMY_SHOOT) {
                    for (Entity entity : world.getEntities(ENEMY)) {
                        if (entity.getID().equals(event.getEntityID())) {
                            Entity bullet = new Entity();
                            bullet.setType(BULLET);
                            bullet.setPosition((float) (entity.getX() + entity.getRadius() * 2 * Math.cos(entity.getRadians())),
                                    (float) (entity.getY() + entity.getRadius() * 2 * Math.sin(entity.getRadians())));

                            bullet.setMaxSpeed(100);
                            bullet.setAcceleration(100);
                            bullet.setDeacceleration(10);

                            bullet.setRadians(entity.getRadians());
                            bullet.setRadius(6);
                            bullet.setRotationSpeed(1);
                            bullet.setExpiration(2);
                            bullet.setDx(entity.getDx());
                            bullet.setDy(entity.getDy());

                            setShape(bullet);
                            bullet.setShapeX(shapex);
                            bullet.setShapeY(shapey);
                            world.addEntity(bullet);
                            gameData.removeEvent(event);
                        }
                    }
                }
            }
        }
        for (Entity b : world.getEntities(BULLET)) {
            if (b.getIsHit()) {
                world.removeEntity(b);
                b = null;
            } else {
                update(b, gameData, world);
            }
        }
    }

    private void update(Entity bullet, GameData gameData, World world) {
        float dx = bullet.getDx();
        float dy = bullet.getDy();
        float x = bullet.getX();
        float y = bullet.getY();

        dx += Math.cos(bullet.getRadians()) * 1000 * gameData.getDelta();
        dy += Math.sin(bullet.getRadians()) * 1000 * gameData.getDelta();
        bullet.setDx(dx);
        bullet.setDy(dy);

        x += dx * gameData.getDelta();
        y += dy * gameData.getDelta();
        bullet.setX(x);
        bullet.setY(y);
        bullet.setExpiration(bullet.getExpiration() - gameData.getDelta());

        setShape(bullet);
        bullet.setShapeX(shapex);
        bullet.setShapeY(shapey);

        if (bullet.getExpiration() <= 0) {
            world.removeEntity(bullet);
        }
    }

    private void setShape(Entity bullet) {

        shapex = new float[4];
        shapey = new float[4];
        float radians = bullet.getRadians();
        float b = (3.1415f / 2);
        float radius = bullet.getRadius();

        shapex[0] = (float) (bullet.getX() + Math.cos(radians) * radius);
        shapey[0] = (float) (bullet.getY() + Math.sin(radians) * radius);

        shapex[1] = (float) (bullet.getX() + Math.cos(radians + b) * radius);
        shapey[1] = (float) (bullet.getY() + Math.sin(radians + b) * radius);

        shapex[2] = (float) (bullet.getX() + Math.cos(radians + b * 2) * radius);
        shapey[2] = (float) (bullet.getY() + Math.sin(radians + b * 2) * radius);

        shapex[3] = (float) (bullet.getX() + Math.cos(radians + b * 3) * radius);
        shapey[3] = (float) (bullet.getY() + Math.sin(radians + b * 3) * radius);
    }
    
    private Entity createBullet(GameData gameData) {

        Entity newBullet = new Entity();
        newBullet.setType(BULLET);
        newBullet.setPosition(gameData.getDisplayWidth()/3, gameData.getDisplayHeight()/3);

        newBullet.setMaxSpeed(100);
        newBullet.setAcceleration(100);
        newBullet.setDeacceleration(10);
        newBullet.setIsHit(false);

        newBullet.setRadians(3.1415f / 2);
        newBullet.setRadius(30);
        newBullet.setRotationSpeed(1);

        return newBullet;
    }

    @Override
    public void start(GameData gameData, World world) {
        Entity bullet = createBullet(gameData);
        world.addEntity(bullet);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for(Entity e : world.getEntities(BULLET)){
        world.removeEntity(e);    
        }
    }
}
