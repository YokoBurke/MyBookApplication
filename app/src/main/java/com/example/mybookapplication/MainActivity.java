package com.example.mybookapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private MyBooksAdapter myBooksAdapter;
    private TextView mEmptyStateTextView;
    private EditText searchTermEditText;
    private Button searchButton;
    String searchTerm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView myBookListView = (ListView) findViewById(R.id.list);
        myBooksAdapter = new MyBooksAdapter(this, new ArrayList<MyBooks>());
        myBookListView.setAdapter(myBooksAdapter);

        searchTermEditText = (EditText) findViewById(R.id.plain_text_input);
        searchButton = (Button) findViewById(R.id.search_button);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view) ;
        myBookListView.setEmptyView(mEmptyStateTextView);

        searchTermEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    checkData();
                }
                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

    }

    private class AsyncTaskBooks extends AsyncTask<String, Void, ArrayList<MyBooks>> {

        @Override
        protected ArrayList<MyBooks> doInBackground(String... myUrl) {

            Log.i(LOG_TAG, "Asynctask" + myUrl[0]);
            if (myUrl.length < 1 || myUrl[0] == null) {
                return null;
            }

            ArrayList<MyBooks> result = QueryUtils.fetchMyBookData(myUrl[0]);
            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<MyBooks> data){
            myBooksAdapter.clear();
            if (data == null){
                mEmptyStateTextView.setText("No Data Found");
            }

            if (data != null && !data.isEmpty()){
                myBooksAdapter.addAll(data);
            }
        }
    }

    public void checkData(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            searchTerm = searchTermEditText.getText().toString();
            AsyncTaskBooks task = new AsyncTaskBooks();
            task.execute(searchTerm);
        } else {
            mEmptyStateTextView.setText("No Network Connection");
        }

    }


}