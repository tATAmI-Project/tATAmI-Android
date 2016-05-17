package android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.ui.backend.AgentLogBackend;
import android.ui.backend.LogHub;
import android.ui.backend.LogHubListener;
import android.view.View;
import android.widget.TextView;

import com.tatami.tatami_android.MainActivity;
import com.tatami.tatami_android.R;

public class AgentLogActivity extends AppCompatActivity implements LogHubListener {

    String agentName;
    TextView agentOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_log_activity);
        agentOutput = (TextView)findViewById(R.id.agentLogTextView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            agentName = extras.getString("name");
        }
        else{
            return;
        }

        agentOutput.setText(LogHub.getAgentLog(agentName).toString());
        LogHub.registerListener(this);

    }

    public void onLogUpdated(){
        agentOutput.setText(LogHub.getAgentLog(agentName).toString());
    }

    public void onClearAgentLogButtonPressed(View v){
        MainActivity.backend.getLogHub().clear(agentName);
    }
}
