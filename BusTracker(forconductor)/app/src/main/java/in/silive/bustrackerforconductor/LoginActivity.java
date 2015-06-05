package in.silive.bustrackerforconductor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.panningview.library.PanningView;
import com.gc.materialdesign.views.ButtonFlat;

import java.net.MalformedURLException;
import java.net.URL;

import listener.NetworkResponseListener;

public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;
    public EditText userName;
    public EditText mPasswordView;
    PanningView panningView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.password);
        panningView = (PanningView) findViewById(R.id.panningView);
        panningView.startPanning();
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

        ButtonFlat mEmailSignInButton = (ButtonFlat) findViewById(R.id.login);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        userName.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = userName.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            userName.setError(getString(R.string.error_field_required));
            focusView = userName;
            cancel = true;
        }
        CheckNetwork check = new CheckNetwork(this);
        if (cancel) {
            focusView.requestFocus();
        } else if (check.isNetworkConnected() && check.isGPSEnabled() && check.checkConnectivity()) {
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String res = "";
        private ProgressDialog pDialog;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in....");
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }

            if (mEmail.equals(mPassword)) {
                for (int i = 1; i <= 10; i++) {
                    if (mEmail.equals(String.valueOf(i))) {
                        ConductorActivity.setId(String.valueOf(i));
                        res = "Logged in successfully";
                        return true;
                    }
                }
            }
            res = "Login failed";
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            pDialog.dismiss();

            if (success) {
                CheckNetwork checkNetwork = new CheckNetwork(LoginActivity.this);
                if (checkNetwork.isGPSEnabled() && checkNetwork.isNetworkConnected()) {
                    Intent i = new Intent(LoginActivity.this, ConductorActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                }
            } else {
                mPasswordView
                        .setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        panningView.stopPanning();
    }

    @Override
    protected void onResume() {
        super.onResume();
        panningView.startPanning();
    }
}
