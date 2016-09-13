package com.example.onjos.zappossampleapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.onjos.zappossampleapplication.model.Product;
import com.example.onjos.zappossampleapplication.model.ProductSearchResult;
import com.example.onjos.zappossampleapplication.model.ProductSearchService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZapposMainActivity extends AppCompatActivity {

    private static final String TAG = "ZapposMainActivity";
    private List<Product> productList;
    private RecyclerView mRecyclerView;
    private ZapposRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private Retrofit retrofitZappos;
    private String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_list);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        searchTerm = bundle.getString("search_term");

        if(searchTerm!=null && !searchTerm.equals(""))
        {
            retrofitZappos = new Retrofit.Builder()
                    .baseUrl("https://api.zappos.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            new AsyncHttpTaskZappos().execute(searchTerm,"b743e26728e16b81da139182bb2094357c31d331");
        }
    }

    public class AsyncHttpTaskZappos extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                ProductSearchService service = retrofitZappos.create(ProductSearchService.class);
                Call<ProductSearchResult> productSearchResults = service.getProducts(params[0], params[1]);
                ProductSearchResult productResults = productSearchResults.execute().body();
                productList = new ArrayList<>();
                productList = productResults.getResults();
                if (productList.size() > 0)
                    result = 1;
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == 1) {
                adapter = new ZapposRecyclerAdapter(ZapposMainActivity.this, productList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(ZapposMainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
