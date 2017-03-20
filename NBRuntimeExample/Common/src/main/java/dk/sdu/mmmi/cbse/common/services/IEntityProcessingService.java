package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;


public interface IEntityProcessingService {
    /**
     * Processes the data on the entities implementing IEntityProcessingService.
     * Events is handled, attributes changes and movement is registred. 
     * 
     * @param gameData GameData that contains events, keys, delta-time and information about the screen etc. width/height.
     * @param world World contains list that hold the entities.
     */
    void process(GameData gameData, World world);
}
