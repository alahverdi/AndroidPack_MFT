package ir.allahverdi.digiapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.MainActivity;
import ir.allahverdi.digiapplication.adapter.ProductAdapterGridView;
import ir.allahverdi.digiapplication.database.ProductDbHelper;
import ir.allahverdi.digiapplication.entity.Product;
import ir.allahverdi.digiapplication.ui.DetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ServiceDigikalaApi extends IntentService {

    public static final String TAG = ServiceDigikalaApi.class.getSimpleName();

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "ir.allahverdi.digiapplication.service.action.FOO";
    public static final String ACTION_BAZ = "ir.allahverdi.digiapplication.service.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "ir.allahverdi.digiapplication.service.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "ir.allahverdi.digiapplication.service.extra.PARAM2";

    public ServiceDigikalaApi() {
        super("ServiceDigikalaApi");
        Log.e(TAG, "\n\nServiceDigikalaApi ------------------------------------------------\n\n ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "\n\nonHandleIntent ------------------------------------------------\n\n");
        getDataApi();
    }


    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private void getDataApi() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 1) make instance of Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DigikalaApi.BASE_CLASS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 2) make Api
        DigikalaApi api = retrofit.create(DigikalaApi.class);

        // 3) make Request
        Call<List<Product>> request = api.getAllProducts();

        // 4) callback
        request.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.e(TAG, "onResponse: " + response.message());

                // web service called and now import data into database:
                if (getDataFromDatabase().size() > 0) {
                    Log.e(TAG, "onResponse: data exist in db and INSERT_Into_DATABASE() method did not called again.");
                } else {
                    for (int i = 0; i < response.body().size(); i++) {
                        Product product = new Product(
                                response.body().get(i).getName(),
                                response.body().get(i).getPrice(),
                                response.body().get(i).getImgId(),
                                response.body().get(i).getModel(),
                                response.body().get(i).getScore());

                        new ProductDbHelper(ServiceDigikalaApi.this).insert(product);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        Intent successIntent = new Intent("SuccessApi");
        sendBroadcast(successIntent);

    }

    private List<Product> getDataFromDatabase() {
        Log.e(TAG, "getDataFromDatabase: ");
        return new ProductDbHelper(this).select();
    }

} // end Service