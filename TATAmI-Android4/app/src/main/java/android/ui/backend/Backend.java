package android.ui.backend;

import com.tatami.tatami_android.BackgroundThread;

/**
 * Created by uidk9631 on 17.05.2016.
 */
public class Backend {

    LogHub logHub;

    public Backend(){
        logHub = new LogHub();
        while(BackgroundThread.getBoot() == null);
        while(BackgroundThread.getBoot().getSimulationManager() == null);
        while(BackgroundThread.getBoot().getSimulationManager().ext() == null);

        BackgroundThread.getBoot().getSimulationManager().ext().registerListener("Loghub", logHub);
    }

    public LogHub getLogHub(){
        return logHub;
    }
}
