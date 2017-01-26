package ms.wmm.client.activity.groupsListView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ms.wmm.client.AuthData;
import ms.wmm.client.R;
import ms.wmm.client.activity.loginView.LoginActivity;
import ms.wmm.client.activity.groupView.OpenedGroupActivity;
import ms.wmm.client.adapter.GroupAdapter;
import ms.wmm.client.bo.GroupHead;

public class GroupsListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Gson gson;
    private String serverAdress;
    private ListView listView;
    private GroupsListActivity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        listView=(ListView) findViewById(R.id.groupsListView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        serverAdress=getResources().getString(R.string.server);
        gson=new Gson();
        context=this;
        addListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshGroups();
    }

    private void addListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupHead groupHead= (GroupHead) listView.getItemAtPosition(position);
                openGroupActivity(groupHead);
            }
        });
    }

    private void openGroupActivity(GroupHead groupHead) {
        Intent intent;
        if(groupHead.isOpen()) {
            intent = new Intent(getApplicationContext(), OpenedGroupActivity.class);
        }else{
            intent=null;
        }
        Bundle bundle=new Bundle();
        bundle.putLong("groupId",groupHead.getId());
        bundle.putBoolean("isAdmin",groupHead.isAdmin());
        bundle.putStringArrayList("users",new ArrayList<String>(groupHead.getUsers()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groups_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.option_add:
                addGroup();
                break;
            case R.id.option_refresh:
                refreshGroups();
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

    private void addGroup() {
        Intent intent=new Intent(getApplicationContext(),AddGroupActivity.class);
        startActivity(intent);
    }

    private void refreshGroups() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(AuthData.getUsername(), AuthData.getPassword());
        client.get(serverAdress + "/getGroups", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Type listType = new TypeToken<List<GroupHead>>() {}.getType();
                List<GroupHead> groups = gson.fromJson(response.toString(), listType);
                GroupHead[] groupsArray=groups.toArray(new GroupHead[groups.size()]);
                ListAdapter adapter = new GroupAdapter(context,groupsArray);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0)
                    Toast.makeText(getApplicationContext(), "Serwer nie odpowiada", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Wystąpił nieokreślony błąd", Toast.LENGTH_LONG).show();
            }
        });
    }
}
