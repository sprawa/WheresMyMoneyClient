package ms.wmm.client.activity.groupView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import ms.wmm.client.AuthData;
import ms.wmm.client.R;

public class AddTransaction extends AppCompatActivity {

    private EditText descTxt;
    private EditText valueTxt;
    private Button okBtn;
    private Button cancelBtn;
    private String serverAdress;
    private ArrayList<String> users;
    private Spinner userSpinner;
    private ArrayAdapter<String> usersAdapter;
    private Long groupId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        descTxt=(EditText) findViewById(R.id.descTxt);
        okBtn=(Button) findViewById(R.id.okBtn);
        cancelBtn=(Button) findViewById(R.id.cancelBtn);
        serverAdress=getResources().getString(R.string.server);
        valueTxt=(EditText) findViewById(R.id.valueTxt);
        userSpinner=(Spinner) findViewById(R.id.userSpinner);
        Bundle bundle = getIntent().getExtras();
        users=bundle.getStringArrayList("users");
        groupId=bundle.getLong("groupId");
        usersAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,users);
        usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(usersAdapter);
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
                createTransaction();
            }
        });
    }

    private void createTransaction() {
        String lender=userSpinner.getSelectedItem().toString();
        String value=valueTxt.getText().toString();
        String desc=descTxt.getText().toString();

        try {
            desc= URLEncoder.encode(desc,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        if(lender.isEmpty()){
            Toast.makeText(getApplicationContext(), "Musisz wybrać użytkownika", Toast.LENGTH_LONG).show();
            return;
        }

        if(value.isEmpty()){
            Toast.makeText(getApplicationContext(), "Musisz podać kwotę", Toast.LENGTH_LONG).show();
            return;
        }

        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(AuthData.getUsername(),AuthData.getPassword());
        String query=serverAdress+"/borrow?lender="+lender+"&desc="+desc+""+"&value="+value+"&groupId="+groupId;
        client.post(query, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 0)
                    Toast.makeText(getApplicationContext(), "Serwer nie odpowiada", Toast.LENGTH_LONG).show();
                else if (statusCode == 406)
                    Toast.makeText(getApplicationContext(), "Użytkownik nie istnieje", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Wystąpił nieokreślony błąd", Toast.LENGTH_LONG).show();
            }
        });
    }
}
