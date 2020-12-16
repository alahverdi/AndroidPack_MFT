package ir.allahverdi.digiapplication.service;

import java.util.List;

import ir.allahverdi.digiapplication.entity.Product;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DigikalaApi {

    String BASE_CLASS = "https://run.mocky.io/";

    @GET("v3/473fbc1a-fbe8-48a5-8d04-04756c8700e0")
    Call<List<Product>> getAllProducts();

    // support :
    //v3/a6147bd4-64c6-40a2-b817-83f83ed54642
    // optimize :
    //v3/473fbc1a-fbe8-48a5-8d04-04756c8700e0
}
