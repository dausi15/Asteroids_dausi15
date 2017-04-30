/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.bulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Daniel
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class BulletControlSystem implements IEntityProcessingService, bulletSPI {

    float[] shapex;
    float[] shapey;

    @Override
    public void process(GameData gameData, World world) {

        for (Entity b : world.getEntities(Bullet.class)) {
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

    @Override
    public Entity createBullet(Entity e, GameData gameData) {
        Entity bullet = new Bullet();
        bullet.setPosition((float) (e.getX() + e.getRadius() * 2 * Math.cos(e.getRadians())),
                (float) (e.getY() + e.getRadius() * 2 * Math.sin(e.getRadians())));

        bullet.setMaxSpeed(100);
        bullet.setAcceleration(100);
        bullet.setDeacceleration(10);

        bullet.setRadians(e.getRadians());
        bullet.setRadius(6);
        bullet.setRotationSpeed(1);
        bullet.setExpiration(2);
        bullet.setDx(e.getDx());
        bullet.setDy(e.getDy());

        setShape(bullet);
        bullet.setShapeX(shapex);
        bullet.setShapeY(shapey);

        return bullet;
    }
}
