package components.dev.mobility;

import android.util.Log;

import com.tatami.tatami_android.core.agent.AgentComponent;
import com.tatami.tatami_android.core.agent.mobility.MobilityComponent;
import com.tatami.tatami_android.core.agent.parametric.ParametricComponent;
import com.tatami.tatami_android.core.simulation.PlatformLoader;
import com.tatami.tatami_android.websocket.WebSocketMessagingComponent;
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

    public static final String PARAMETRIC_COMPONENT = "tatami.core.agent.parametric.ParametricComponent";
    public static final String MOBILITY_COMPONENET = "tatami.core.agent.mobility.MobilityComponent";
    public static final String WEBSOCKET_COMPONENT = "com.tatami.tatami_android.websocket.WebSocketMessagingComponent";

    public static final String STATE_AGENT_COMPONENT = "StateAgentTestComponent";

    public static final String PLATFORM_WEBSOCKET_MESSAGING = "websocket";


    public AgentComponent getComponent(String className, Object... arg){
        Log.v("smth", "reach here " + className);
        if(className.compareTo(PARAMETRIC_COMPONENT) == 0){
            Log.v("smth", "Parametric component");
            return new ParametricComponent();
        }

        if(className.compareTo(WEBSOCKET_COMPONENT) == 0){
            Log.v("smth", "Parametric component");
            return new WebSocketMessagingComponent();
        }

        if(className.compareTo(MOBILITY_COMPONENET) == 0){
            Log.v("smth", "Parametric component");
            return new MobilityComponent();
        }
        if(className.compareTo(STATE_AGENT_COMPONENT) == 0){
            Log.v("smth", "Parametric component");
            return new StateAgentTestComponent();
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
