package nasa.appchallenge.smartcitiesclient.locationutils;

import android.location.Location;
import nasa.appchallenge.smartcitiesclient.R;

public interface LocationResultListener {
    public void onLocationResultAvailable(Location location);
}
