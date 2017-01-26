package ms.wmm.client.activity.loginView;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.net.ConnectException;

import cz.msebera.android.httpclient.Header;
import ms.wmm.client.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText loginTxt;
    private EditText passwordTxt;
    private EditText password2Txt;
    private Button cancelBtn;
    private Button registerBtn;

    private ProgressDialog progress;

    private String serverAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginTxt=(EditText) findViewById(R.id.loginTxt);
        passwordTxt=(EditText) findViewById(R.id.passwordTxt);
        password2Txt=(EditText) findViewById(R.id.password2Txt);
        cancelBtn=(Button) findViewById(R.id.cancelBtn);
        registerBtn=(Button) findViewById(R.id.registerBtn);
        serverAdress=getResources().getString(R.string.server);
        addListeners();
        makeProgressDialog();
    }

    private void makeProgressDialog() {
        progress=new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setMessage("Rejestracja...");
    }

    private void addListeners() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        if(!validateFields()) return;
        makeRegisterRequest();
    }

    private void makeRegisterRequest() {
        progress.show();
        String user=loginTxt.getText().toString();
        String password=passwordTxt.getText().toString();
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("user",user);
        params.put("pass",password);
        client.post(serverAdress+"/register",params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"Zarejestrowano", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progress.hide();
                if(statusCode==0) Toast.makeText(getApplicationContext(),"Serwer nie odpowiada", Toast.LENGTH_LONG).show();
                else if(statusCode==409) Toast.makeText(getApplicationContext(),"Użytkownik o tej nazwie już istnieje", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(),"Wystąpił nieokreślony błąd", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateFields() {
        String login=loginTxt.getText().toString();
        String password=passwordTxt.getText().toString();
        String password2=password2Txt.getText().toString();

        if(login.isEmpty() || password.isEmpty() || password2.isEmpty()){
            Toast.makeText(getApplicationContext(),"Pola nie mogą być puste",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!password.equals(password2)){
            Toast.makeText(getApplicationContext(),"Hasła muszą być identyczne",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void cancel() {
        finish();
    }
}
