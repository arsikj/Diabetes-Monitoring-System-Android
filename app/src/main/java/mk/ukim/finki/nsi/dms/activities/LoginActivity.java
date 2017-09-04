package mk.ukim.finki.nsi.dms.activities;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
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
import mk.ukim.finki.nsi.dms.utils.PreferencesManager;

/**
 * Created by dejan on 03.9.2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private TextView register;
    private AppCompatButton loginButton;
    private LinearLayout loginView;

    private ProgressDialog progressDialog;

    String username;
    String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_login);

        initComponents();
        initListeners();

        username = PreferencesManager.getInstance(LoginActivity.this).getStringValue(Constants.USERNAME);
        password = PreferencesManager.getInstance(LoginActivity.this).getStringValue(Constants.PASSWORD);

        if(username != null && password != null) {
            usernameText.setText(username);
            passwordText.setText(password);
        }

        Intent intent = getIntent();
        String usernameFromIntent = intent.getStringExtra(Constants.USERNAME);

        if (usernameFromIntent!=null) {
            usernameText.setText(usernameFromIntent);
        }
    }

    private void initComponents(){
        usernameText = (EditText) findViewById(R.id.input_username);
        passwordText = (EditText) findViewById(R.id.input_password);
        register = (TextView) findViewById(R.id.link_signup);
        loginButton = (AppCompatButton) findViewById(R.id.btn_login);
        loginView = (LinearLayout) findViewById(R.id.loginView);
    }

    private void initListeners(){

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void login() {

        if (HTTPRequestService.getInstance().isConnected(LoginActivity.this)) {
            if(usernameText.getText().toString() != null && passwordText.getText().toString() != null) {
                new LoginAsyncTask().execute(Constants.LOGIN_LINK, usernameText.getText().toString(), passwordText.getText().toString());
            } else {
                Snackbar snackbar = Snackbar.make(loginView, Constants.FILL_INPUTS_MESSAGE, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar.make(loginView, Constants.CONNECT_TO_INTERNET_MESSAGE, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    private class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(Constants.AUTHENTICATING);
            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return HTTPRequestService.getInstance().login(params[0], params[1], params[2], LoginActivity.this);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if(aBoolean) {
                PreferencesManager.getInstance(LoginActivity.this).addStringValue(Constants.USERNAME, usernameText.getText().toString());
                PreferencesManager.getInstance(LoginActivity.this).addStringValue(Constants.PASSWORD, passwordText.getText().toString());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Snackbar snackbar = Snackbar.make(loginView, Constants.NOT_LOGGED_IN, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }
}
