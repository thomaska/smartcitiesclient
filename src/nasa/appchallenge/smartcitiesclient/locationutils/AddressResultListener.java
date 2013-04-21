package nasa.appchallenge.smartcitiesclient.locationutils;

import android.location.Address;
import nasa.appchallenge.smartcitiesclient.R;

public interface AddressResultListener {
    public void onAddressAvailable(Address address);
}
