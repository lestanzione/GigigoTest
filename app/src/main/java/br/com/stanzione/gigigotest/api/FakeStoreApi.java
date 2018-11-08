package br.com.stanzione.gigigotest.api;

import java.util.List;

import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface FakeStoreApi {

    @GET("products")
    Observable<List<Product>> getProducts();

}
