/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.asteroid.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
//import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Daniel
 */

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    //    ,@ServiceProvider(service = IGamePluginService.class)
})
public class CollisionControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (!entity1.equals(entity2)) {
                    if (!(entity1.getClass().equals(Asteroid.class) && entity2.getClass().equals(Asteroid.class))) {
                        if (!(entity1.getClass().equals(Bullet.class) && entity2.getClass().equals(Bullet.class))) {
                            if (!entity1.getIsHit() && !entity2.getIsHit()) {
                                float cx = Math.abs(entity1.getX() - entity2.getX());
                                float cy = Math.abs(entity1.getY() - entity2.getY());
                                //Pythagoras
                                double a = Math.pow((double) cx, 2);
                                double b = Math.pow((double) cy, 2);
                                double c = a + b;
                                if (Math.sqrt(c) < entity1.getRadius() + entity2.getRadius()) {
                                    entity1.setIsHit(true);
                                    entity2.setIsHit(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
