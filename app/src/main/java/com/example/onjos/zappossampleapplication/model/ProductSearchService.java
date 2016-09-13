package com.example.onjos.zappossampleapplication.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by onjos on 11-09-2016.
 */
public interface ProductSearchService {
    @GET("/Search")
    Call<ProductSearchResult> getProducts(@Query("term") String searchTerm,@Query("key") String key);
}
