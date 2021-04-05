package com.example.githubquery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubquery.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText mSearchBox;
    TextView mUrlDisplay,mUrlResponse,errorMsg;
    ProgressBar mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchBox=(EditText)findViewById(R.id.et_search_box);
        mUrlDisplay=(TextView)findViewById(R.id.tv_url_display);
        mUrlResponse=(TextView)findViewById(R.id.tv_url_response_json);
        errorMsg=(TextView)findViewById(R.id.tv_error_mesg);
        mLoader=(ProgressBar)findViewById(R.id.pb_progress);
    }
    public void showJsonData()
    {
        mUrlResponse.setVisibility(View.VISIBLE);
        errorMsg.setVisibility(View.INVISIBLE);
    }
    public void showErrorMessage()
    {
        errorMsg.setVisibility(View.VISIBLE);
        mUrlResponse.setVisibility(View.INVISIBLE);
    }
    public void makeGithubSearchQuery()
    {
        String githubQuery=mSearchBox.getText().toString();
        URL githubSearchUrl= NetworkUtils.buildUrl(githubQuery);
        mUrlDisplay.setText(githubSearchUrl.toString());
        String githubSearchResults=null;
        new GithubAsyncTask().execute(githubSearchUrl);
    }
    public class GithubAsyncTask extends AsyncTask<URL,Void,String>
    {
        @Override
        protected void onPreExecute() {
            mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl=urls[0];
            String githubSearchResults=null;
            try {
                githubSearchResults=NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;

        }

        @Override
        protected void onPostExecute(String s) {
            mLoader.setVisibility(View.INVISIBLE);
            if(s!=null&&!s.equals(""))
            {
                mUrlResponse.setText(s);
                showJsonData();

            }
            else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemSelected=item.getItemId();
        if(itemSelected==R.id.action_search);
        {
            makeGithubSearchQuery();
        }
        return super.onOptionsItemSelected(item);
    }
}