package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private static final int MAX_CHAR_COUNT = 280;

    EditText etTweetInput;
    Button btnSend;
    TwitterClient client;
    TextView tvCharCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etTweetInput = findViewById(R.id.etTweetInput);
        tvCharCounter = findViewById(R.id.tvCharCounter);
        btnSend = findViewById(R.id.btnSend);

        client = TwitterApp.getRestClient(this);
        etTweetInput.addTextChangedListener(mTextEditorWatcher);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
            }
        });
    }

    private void sendTweet(){
        client.sendTweet(etTweetInput.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet= Tweet.fromJSON(response);
                    Intent intent = new Intent();
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }
        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {


        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharCounter.setText(String.valueOf(MAX_CHAR_COUNT-s.length() + " characters remaining"));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
