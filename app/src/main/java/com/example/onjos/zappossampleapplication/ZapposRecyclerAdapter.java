package com.example.onjos.zappossampleapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onjos.zappossampleapplication.model.Product;
import com.example.onjos.zappossampleapplication.model.ProductSearchResult;
import com.example.onjos.zappossampleapplication.model.ProductSearchService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by onjos on 12-09-2016.
 */
    public class ZapposRecyclerAdapter extends RecyclerView.Adapter<ZapposRecyclerAdapter.CustomViewHolder>{
        private Context mContext;
        private Retrofit retrofit6pm;
        private static final String TAG = "ZapposRecyclerAdapter";
        private List<Product> productList;
        private String baseUrl6pm = "https://api.6pm.com/";
        private String key6pm = "524f01b7e2906210f7bb61dcbe1bfea26eb722eb";

        public ZapposRecyclerAdapter(Context context, List<Product> productList) {
            this.productList = productList;
            this.mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
            Product product = productList.get(i);

            //Download image using picasso library
            Picasso.with(mContext).load(product.getThumbnailImageUrl())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);

            //Setting the name
            customViewHolder.txtName.setText((product.getProductName()));
            //Setting the brand name
            customViewHolder.txtBrand.setText((product.getBrandName()));
            //Setting the price
            customViewHolder.txtPrice.setText((product.getPrice()));

            //Handle click event on both title and image click
            customViewHolder.txtName.setOnClickListener(clickListener);
            customViewHolder.imageView.setOnClickListener(clickListener);

            customViewHolder.txtName.setTag(customViewHolder);
            customViewHolder.imageView.setTag(customViewHolder);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                int position = holder.getPosition();

                Product clickedProduct = ZapposRecyclerAdapter.this.productList.get(position);

                retrofit6pm = new Retrofit.Builder()
                        .baseUrl(baseUrl6pm)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                new AsyncHttpTask6pm().execute(clickedProduct.getProductName(),key6pm,clickedProduct.getProductId(),clickedProduct.getPrice());
            }
        };

        @Override
        public int getItemCount() {
            return (null != productList ? productList.size() : 0);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected ImageView imageView;
            protected TextView txtName;
            protected TextView txtPrice;
            protected TextView txtBrand;

            public CustomViewHolder(View view) {
                super(view);
                this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
                this.txtName = (TextView) view.findViewById(R.id.title);
                this.txtPrice = (TextView) view.findViewById(R.id.price);
                this.txtBrand = (TextView) view.findViewById(R.id.brand);
            }
        }

    public class AsyncHttpTask6pm extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                String term = params[0];
                String key = params[1];
                String productId = params[2];
                String price = params[3];
                ProductSearchService service = retrofit6pm.create(ProductSearchService.class);
                Call<ProductSearchResult> productSearchResults = service.getProducts(term, key);
                ProductSearchResult productResults = productSearchResults.execute().body();
                List<Product> productList6pm = productResults.getResults();
                if (productList6pm.size() > 0)
                {
                    for (Product product6pm:productList6pm) {
                        if (product6pm.getProductId().equals(productId))
                        {
                            // To eliminate the dollar sign, take substring from 1st index.
                            Float price6pm = Float.parseFloat(product6pm.getPrice().substring(1));
                            Float priceZappos = Float.parseFloat(price.substring(1));
                            if(price6pm < priceZappos)
                            {
                                result = 1;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Toast.makeText(mContext, "This item is cheaper on 6pm!", Toast.LENGTH_SHORT).show();
            } else if(result ==0){
                Toast.makeText(mContext, "No cheaper item found on 6pm!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
