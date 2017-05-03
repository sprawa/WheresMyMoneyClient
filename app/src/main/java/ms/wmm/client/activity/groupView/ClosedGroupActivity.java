package ms.wmm.client.activity.groupView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import ms.wmm.client.AuthData;
import ms.wmm.client.R;
import ms.wmm.client.activity.loginView.LoginActivity;

public class ClosedGroupActivity extends AppCompatActivity {

    private Long groupId;
    private boolean isAdmin;
    private ArrayList<String> users;

    private Toolbar toolbar;
    private Gson gson;
    private String serverAdress;
    private ListView listView;
    private ClosedGroupActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_group);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        listView=(ListView) findViewById(R.id.listView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        serverAdress=getResources().getString(R.string.server);
        gson=new Gson();
        context=this;

        Bundle bundle = getIntent().getExtras();
        groupId=bundle.getLong("groupId");
        isAdmin=bundle.getBoolean("isAdmin");
        users=bundle.getStringArrayList("users");
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshSummary();
    }

    private void refreshSummary() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isAdmin)
            getMenuInflater().inflate(R.menu.closed_group_admin_menu,menu);
        else
            getMenuInflater().inflate(R.menu.closed_group_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.option_refresh:
                refreshSummary();
                break;
            case R.id.option_delete_group:
                deleteGroup();
                break;
            case R.id.option_exit_group:
                exitGroup();
                break;
            case R.id.option_logout:
                logout();
                break;
            case R.id.option_exit:
                exit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exitGroup() {
    }

    private void deleteGroup() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(AuthData.getUsername(), AuthData.getPassword());
        client.delete(serverAdress + "/deleteGroup?id="+groupId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 0)
                    Toast.makeText(getApplicationContext(), "Serwer nie odpowiada", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Wystąpił nieokreślony błąd", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void exit() {
        finish();
        System.exit(0);
    }

    private void logout() {
        AuthData.setUsername("");
        AuthData.setPassword("");
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
