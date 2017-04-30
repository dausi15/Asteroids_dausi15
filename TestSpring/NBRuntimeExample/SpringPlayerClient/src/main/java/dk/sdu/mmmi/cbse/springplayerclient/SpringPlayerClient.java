/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.springplayerclient;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.spring.SpringPlayerPlugin;
import dk.sdu.mmmi.cbse.springplayerprocessor.SpringPlayerProcessor;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Daniel
 */

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
        ,
    @ServiceProvider(service = IGamePluginService.class)
})
public class SpringPlayerClient implements IGamePluginService, IEntityProcessingService{

    ApplicationContext c = new ClassPathXmlApplicationContext("BeansProcessor.xml");
    SpringPlayerProcessor springPlayerProcessor = (SpringPlayerProcessor) c.getBean("SpringPlayerProcessor");
    @Override
    public void start(GameData gameData, World world) {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        SpringPlayerPlugin springPlayerPlugin = (SpringPlayerPlugin) context.getBean("SpringPlayerPlugin");
        world.addEntity(springPlayerPlugin.createPlayerShip(gameData));
    }
    
    @Override
    public void process(GameData gameData, World world) {
        springPlayerProcessor.update(gameData, world);
    }

    @Override
    public void stop(GameData gameData, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
