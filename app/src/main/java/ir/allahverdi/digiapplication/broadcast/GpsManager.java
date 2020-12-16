package ir.allahverdi.digiapplication.broadcast;

import android.app.Service;
import android.content.Context;
import android.location.LocationManager;

public class GpsManager {
    Context context;
    public GpsManager(Context base) {
        context = base;
    }

    public boolean isGpsAvailable() {
        LocationManager manager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
        boolean isEnabledGps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabledGps;
    }

}
