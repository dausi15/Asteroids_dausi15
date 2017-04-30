/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.spring;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.player.Player;

/**
 *
 * @author Daniel
 */
public class SpringPlayerPlugin{

    public Entity createPlayerShip(GameData gameData) {
        Entity playerShip = new Player();

        playerShip.setPosition(gameData.getDisplayWidth() / 3, gameData.getDisplayHeight() / 3);

        playerShip.setMaxSpeed(300);
        playerShip.setAcceleration(200);
        playerShip.setDeacceleration(10);
        playerShip.setLife(4);

        playerShip.setRadius(10);
        playerShip.setRadians(3.1415f / 2);
        playerShip.setRotationSpeed(3);

        System.out.println("PlayerShip created");
        return playerShip;
    }
    
}
