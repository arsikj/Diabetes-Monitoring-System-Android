package mk.ukim.finki.nsi.dms.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import mk.ukim.finki.nsi.dms.R;
import mk.ukim.finki.nsi.dms.utils.Constants;
import mk.ukim.finki.nsi.dms.utils.HTTPRequestService;

/**
 * Created by dejan on 03.9.2017.
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText usernameText;
    private EditText passwordText;
    private AppCompatButton registerButton;
    private TextView loginLink;
    private LinearLayout registerView;

    private ProgressDialog progressDialog;
    String username = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_signup);

        initComponents();
        initListeners();
    }

    private void initComponents() {
        nameText = (EditText) findViewById(R.id.sign_up_name);
        usernameText = (EditText) findViewById(R.id.sign_up_username);
        passwordText = (EditText) findViewById(R.id.sign_up_password);
        registerButton = (AppCompatButton) findViewById(R.id.btn_register);
        loginLink = (TextView) findViewById(R.id.link_login);
        registerView = (LinearLayout) findViewById(R.id.registerView);
    }

    private void initListeners(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class SignUpAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(Constants.SIGN_UP_MESSAGE);
            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            username = params[2];
            return HTTPRequestService.getInstance().signUp(params[0], params[1], params[2], params[3]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if (aBoolean) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.putExtra(Constants.USERNAME, username);
                startActivity(intent);
            } else {
                Snackbar.make(registerView, Constants.SIGN_UP_FAILED, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
