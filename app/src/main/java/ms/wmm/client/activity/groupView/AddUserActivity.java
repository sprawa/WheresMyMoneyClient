package ms.wmm.client.activity.groupView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ms.wmm.client.AuthData;
import ms.wmm.client.R;

public class AddUserActivity extends AppCompatActivity {

    private EditText userTxt;
    private Button okBtn;
    private Button cancelBtn;
    private String serverAdress;
    private Long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        userTxt=(EditText) findViewById(R.id.userTxt);
        okBtn=(Button) findViewById(R.id.okBtn);
        cancelBtn=(Button) findViewById(R.id.cancelBtn);
        serverAdress=getResources().getString(R.string.server);
        groupId=getIntent().getLongExtra("groupId",-1);
        addListeners();
    }

    private void addListeners() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void addUser() {
        String user=userTxt.getText().toString();
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(AuthData.getUsername(),AuthData.getPassword());
        client.put(serverAdress+"/addUser?groupId="+groupId+"&user="+user, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 0)
                    Toast.makeText(getApplicationContext(), "Serwer nie odpowiada", Toast.LENGTH_LONG).show();
                else if (statusCode == 400)
                    Toast.makeText(getApplicationContext(), "Użytkownik nie istnieje", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Wystąpił nieokreślony błąd", Toast.LENGTH_LONG).show();
            }
        });
    }
}
