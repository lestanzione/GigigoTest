package br.com.stanzione.gigigotest.api;

import com.google.gson.JsonObject;

import java.util.List;

import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FakeStoreApi {

    @GET("products")
    Observable<List<Product>> getProducts();

    @POST("order")
    Completable postOrder(@Body JsonObject cardInformation);

}
