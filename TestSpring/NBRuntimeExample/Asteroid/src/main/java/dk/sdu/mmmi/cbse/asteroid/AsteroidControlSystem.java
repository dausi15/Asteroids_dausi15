package dk.sdu.mmmi.cbse.asteroid;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import dk.sdu.mmmi.cbse.common.asteroid.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Daniel
 */

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
        ,
    @ServiceProvider(service = IGamePluginService.class)
})
public class AsteroidControlSystem implements IEntityProcessingService, IGamePluginService {

    float shapex[];
    float shapey[];
    
    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {

            float x = asteroid.getX();
            float y = asteroid.getY();
            float dx = asteroid.getDx();
            float dy = asteroid.getDy();
            float dt = gameData.getDelta();
            float radians = asteroid.getRadians();

            setShape(asteroid);

            asteroid.setShapeX(shapex);
            asteroid.setShapeY(shapey);


            radians += asteroid.getRotationSpeed() * gameData.getDelta();
            asteroid.setRadians(radians);

            // set position
            x += dx * dt;
            y += dy * dt;
            asteroid.setX(x);
            asteroid.setY(y);

            if (x < 0) {
                asteroid.setX(gameData.getDisplayWidth());
            }
            if (x > gameData.getDisplayWidth()) {
                asteroid.setX(0);
            }
            if (y < 0) {
                asteroid.setY(gameData.getDisplayHeight());
            }
            if (y > gameData.getDisplayHeight()) {
                asteroid.setY(0);
            }

            if (asteroid.getIsHit()) {
                if(asteroid.getLife() > 0){
                Entity a1 = new Asteroid();
                Entity a2 = new Asteroid();
                a1.setLife(asteroid.getLife()-1);
                a2.setLife(asteroid.getLife()-1);
                a1.setPosition(asteroid.getX(), asteroid.getY());
                a2.setPosition(asteroid.getX(), asteroid.getY());

                a1.setMaxSpeed(asteroid.getMaxSpeed());
                a2.setMaxSpeed(asteroid.getMaxSpeed());
                
                a1.setDx(-asteroid.getDy());
                a1.setDy(asteroid.getDx());
                
                a2.setDx(asteroid.getDy());
                a2.setDy(-asteroid.getDx());
                
                a1.setRadians(asteroid.getRadians() + 3.1415f / 4);
                a2.setRadians(asteroid.getRadians() - 3.1415f / 4);
                a1.setRadius(asteroid.getRadius()/2);
                a2.setRadius(asteroid.getRadius()/2);
                a1.setRotationSpeed(1);
                a2.setRotationSpeed(1);

                setShape(a1);

                a1.setShapeX(shapex);
                a1.setShapeY(shapey);

                setShape(a2);

                a2.setShapeX(shapex);
                a2.setShapeY(shapey);
                //Remove asteroid
                world.removeEntity(asteroid);
                asteroid = null;
                //add the two new asteroids
                world.addEntity(a1);
                world.addEntity(a2);
                }else{
                    world.removeEntity(asteroid);
                    asteroid = null;
                }
            }
        }
    }

    private void setShape(Entity asteroid) {

        float radians = asteroid.getRadians();
        float radius = asteroid.getRadius();
        float b = (3.1415f / 2);
        shapex = new float[8];
        shapey = new float[8];

        shapex[0] = (float) (asteroid.getX() + Math.cos(radians) * radius);
        shapey[0] = (float) (asteroid.getY() + Math.sin(radians) * radius);

        shapex[1] = (float) (asteroid.getX() + Math.cos(radians + b / 2) * radius);
        shapey[1] = (float) (asteroid.getY() + Math.sin(radians + b / 2) * radius);

        shapex[2] = (float) (asteroid.getX() + Math.cos(radians + b) * radius);
        shapey[2] = (float) (asteroid.getY() + Math.sin(radians + b) * radius);

        shapex[3] = (float) (asteroid.getX() + Math.cos(radians + b * 1.5) * radius);
        shapey[3] = (float) (asteroid.getY() + Math.sin(radians + b * 1.5) * radius);

        shapex[4] = (float) (asteroid.getX() + Math.cos(radians + b * 2) * radius);
        shapey[4] = (float) (asteroid.getY() + Math.sin(radians + b * 2) * radius);

        shapex[5] = (float) (asteroid.getX() + Math.cos(radians + b * 2.5) * radius);
        shapey[5] = (float) (asteroid.getY() + Math.sin(radians + b * 2.5) * radius);

        shapex[6] = (float) (asteroid.getX() + Math.cos(radians + b * 3) * radius);
        shapey[6] = (float) (asteroid.getY() + Math.sin(radians + b * 3) * radius);

        shapex[7] = (float) (asteroid.getX() + Math.cos(radians + b * 3.5) * radius);
        shapey[7] = (float) (asteroid.getY() + Math.sin(radians + b * 3.5) * radius);
    }
    
    private Entity createAsteroid(GameData gameData) {

        Entity newAsteroid = new Asteroid();
        Random random = new Random();

        Random rnd = new Random();
        newAsteroid.setPosition(rnd.nextInt(gameData.getDisplayWidth()), rnd.nextInt(gameData.getDisplayHeight()));

        newAsteroid.setMaxSpeed(70);
        newAsteroid.setAcceleration(50);
        newAsteroid.setDeacceleration(10);
        newAsteroid.setLife(3);
        newAsteroid.setIsHit(false);
        int x = rnd.nextInt(2);
        int y = rnd.nextInt(2);
        if(x == 1){
            newAsteroid.setDx(50);
        }else{
            newAsteroid.setDx(50*-1);
        }
        if(y == 1){
            newAsteroid.setDy(y);
        }else{
            newAsteroid.setDy(50*-1);
        }

        int r = random.nextInt(32);
        float direction = (3.1415f / 8) * r;

        newAsteroid.setRadians(direction);
        newAsteroid.setRadius(30);
        newAsteroid.setRotationSpeed(1);

        return newAsteroid;
    }

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < 6; i++) {
            Entity asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
