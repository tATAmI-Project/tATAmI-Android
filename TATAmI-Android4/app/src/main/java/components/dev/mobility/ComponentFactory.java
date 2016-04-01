package components.dev.mobility;

import com.tatami.tatami_android.core.agent.AgentComponent;
import com.tatami.tatami_android.core.simulation.PlatformLoader;
import com.tatami.tatami_android.websocket.WebSocketMessagingPlatform;

/**
 * Created by yonutix on 01/04/2016.
 */

public class ComponentFactory {

    private static ComponentFactory singleton = null;

    private ComponentFactory(){

    }

    public static ComponentFactory getInst(){
        if(singleton == null){
            singleton = new ComponentFactory();
        }
        return singleton;
    }

    public static final String STATE_AGENT_TEST_COMPONENT = "componenets.dev.mobility.StateAgentTestComponent";

    public static final String PLATFORM_WEBSOCKET_MESSAGING = "websocket";


    public AgentComponent getComponent(String className){
        if(className.compareTo(STATE_AGENT_TEST_COMPONENT) == 0){
            //Instantiation
        }

        return null;
    }

    public PlatformLoader getPlatform(String platformName, Object... arg){
        if(platformName.compareTo(PLATFORM_WEBSOCKET_MESSAGING) == 0){
            return new WebSocketMessagingPlatform();
        }

        return null;
    }

}
