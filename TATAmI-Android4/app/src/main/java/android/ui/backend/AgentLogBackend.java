package android.ui.backend;

import android.util.Log;

import com.tatami.tatami_android.core.simulation.external_interface.ExtAgentInfo;
import com.tatami.tatami_android.core.simulation.external_interface.ExternalInterfaceListener;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by uidk9631 on 17.05.2016.
 */
public class AgentLogBackend extends Vector<String> {

    public AgentLogBackend(){
        super();
    }

    public String toString(){
        String result = "";
        for(String line : this){
            result += line + "\n";
        }

        return result;
    }
}
