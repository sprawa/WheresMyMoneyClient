package ms.wmm.client.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.ConnectException;

import cz.msebera.android.httpclient.Header;
import ms.wmm.client.AuthData;
import ms.wmm.client.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private Button registerBtn;
    private ProgressDialog progress;
    private String serverAdress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginTxt=(EditText) findViewById(R.id.loginTxt);
        passwordTxt=(EditText) findViewById(R.id.passwordTxt);
        loginBtn=(Button) findViewById(R.id.loginBtn);
        registerBtn=(Button) findViewById(R.id.registerBtn);
        serverAdress=getResources().getString(R.string.server);
        addListeners();
        makeProgressDialog();
    }

    private void makeProgressDialog() {
        progress=new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setMessage("Logowanie...");
    }

    private void addListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if(!validateFields()) return;
        makeLoginRequest();
    }

    private boolean validateFields() {
        String login=loginTxt.getText().toString();
        String password=passwordTxt.getText().toString();
        if(login.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(),"Pola nie mogą być puste",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void makeLoginRequest() {
        progress.show();
        final String login=loginTxt.getText().toString();
        final String password=passwordTxt.getText().toString();
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(login,password);
            client.get(serverAdress+"/login", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    AuthData.setUsername(login);
                    AuthData.setPassword(password);
                    openGroupsActivity();
                    progress.dismiss();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progress.hide();
                    if (statusCode == 0)
                        Toast.makeText(getApplicationContext(), "Serwer nie odpowiada", Toast.LENGTH_LONG).show();
                    else if (statusCode == 401)
                        Toast.makeText(getApplicationContext(), "Złe dane logowania", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Wystąpił nieokreślony błąd", Toast.LENGTH_LONG).show();
                }
            });
    }

    private void openGroupsActivity() {
        Intent intent=new Intent(getApplicationContext(),GroupsActivity.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }
}
