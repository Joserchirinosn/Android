package cleanpress.cleanpress;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by miguelprogrammer on 10/30/15.
 */
public class PickupNow extends Fragment implements GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener
        ,LocationListener {

    GoogleMap map;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    ArrayList<LatLng> markerPoints;
    TextView fullAddress,timedDriver;
    Button nextStep,changeAddress,saveDate;
    LinearLayout pickFields,btnsLayout;
    LocationManager locationManager;
    android.location.LocationListener Listener;
    EditText address,zip,unit,city,state;
    String flag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pickup_now,container,false);

        //Set the Google Api client to get the callbacks.
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object for the current location of the user.
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)// 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        setMap();

        fullAddress = (TextView) view.findViewById(R.id.full_address);
        timedDriver = (TextView) view.findViewById(R.id.timeDriver);
        nextStep = (Button) view.findViewById(R.id.accept_confirm);
        changeAddress = (Button) view.findViewById(R.id.another_address);
        pickFields = (LinearLayout) view.findViewById(R.id.edit_Fields);
        saveDate = (Button) view.findViewById(R.id.nsaveDate);
        address = (EditText) view.findViewById(R.id.Address);
        zip = (EditText) view.findViewById(R.id.zipcode);
        unit = (EditText) view.findViewById(R.id.unitSuite);
        city = (EditText) view.findViewById(R.id.city);
        state = (EditText) view.findViewById(R.id.state);
        btnsLayout = (LinearLayout) view.findViewById(R.id.btnsLayout);

        markerPoints = new ArrayList<LatLng>();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Listener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                    String sZip = addresses.get(0).getPostalCode();


                    if (sZip == null || sZip.length() < 4){
                        Toast.makeText(getActivity(), "Cleanpress is not available at your location", Toast.LENGTH_SHORT).show();
                        map.clear();
                        map.setMyLocationEnabled(true);
                        fullAddress.setText("");
                    } else {
                        String sAddress = addresses.get(0).getThoroughfare();
                        String sSubAdminArea = addresses.get(0).getSubAdminArea();
                        String sAdminArea = addresses.get(0).getAdminArea();
                        String sCountry = addresses.get(0).getCountryName();
                        String full_address = sAddress + " " + sSubAdminArea + " " + sAdminArea + " " + sZip + " " + sCountry;
                        fullAddress.setText(full_address);
                        address.setText(sAddress);
                        zip.setText(sZip);
                        city.setText(sSubAdminArea);
                        state.setText(sAdminArea);

                        Toast.makeText(getActivity(),"ZIP: "+Long.parseLong(sZip),Toast.LENGTH_SHORT).show();
                        map.setMyLocationEnabled(false);
                        ParseQuery<ParseObject> firstLocation = new ParseQuery<ParseObject>("Driver");
                        firstLocation.whereEqualTo("Zipcode",Long.parseLong(sZip)).whereEqualTo("Available", true);
                        firstLocation.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    LatLng nearDriver = new LatLng(parseObject.getDouble("DriverLatitude"), parseObject.getDouble("DriverLongitude"));
                                    if (markerPoints.size() > 1) {
                                        markerPoints.clear();
                                        map.clear();
                                    } else {
                                        double lat = location.getLatitude();
                                        double longit = location.getLongitude();
                                        LatLng currentLoc = new LatLng(lat, longit);
                                        markerPoints.add(0, currentLoc);
                                        markerPoints.add(1, nearDriver);

                                        MarkerOptions markerCurrent = new MarkerOptions()
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                .position(currentLoc)
                                                .title("Current Location");
                                        map.addMarker(markerCurrent);

                                        MarkerOptions markerDriver = new MarkerOptions()
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                                .position(nearDriver)
                                                .title("Current Location");
                                        map.addMarker(markerDriver);


                                        // Checks, whether start and end locations are captured
                                        LatLng origin = markerPoints.get(0);
                                        LatLng dest = markerPoints.get(1);

                                        // Getting URL to the Google Directions API
                                        String url = getDirectionsUrl(origin, dest);

                                        DownloadTask downloadTask = new DownloadTask();

                                        // Start downloading json data from Google Directions API
                                        downloadTask.execute(url);

                                    }
                                }
                            }
                        });
                        ParseQuery<ParseObject> allDrivers = new ParseQuery<ParseObject>("Driver");
                        allDrivers.whereEqualTo("Zipcode",Long.parseLong(sZip)).whereEqualTo("Available", true);

                        allDrivers.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (e==null){
                                    for (ParseObject drivers : list){
                                        double driverLat = drivers.getDouble("DriverLatitude");
                                        double driverLong = drivers.getDouble("DriverLongitude");
                                        MarkerOptions driver = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                                .position(new LatLng(driverLat,driverLong));
                                        map.addMarker(driver);
                                    }
                                }
                            }
                        });

                    }




                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,Listener);

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullAddress.setVisibility(View.GONE);
                pickFields.setVisibility(View.VISIBLE);
                btnsLayout.setVisibility(View.GONE);
                saveDate.setVisibility(View.VISIBLE);
                locationManager.removeUpdates(Listener);
                unit.requestFocus();
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fullAddress.getText().toString().trim().isEmpty()){
                    ParseQuery<ParseObject> findCard = new ParseQuery<ParseObject>("User");
                    findCard.whereEqualTo("Username", ParseUser.getCurrentUser().getEmail());
                    findCard.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                String senderAddress = fullAddress.getText().toString();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                if (parseObject.getBoolean("CardUsed")) {
                                    Fragment activated = new PickupConfirm();
                                    Bundle data = new Bundle();
                                    data.putString("Order Details", senderAddress);
                                    activated.setArguments(data);
                                    fm.beginTransaction()
                                            .replace(R.id.container, activated)
                                            .commit();
                                    Log.e("Found: ", "True");
                                } else {
                                    Fragment activated = new PaymentMethod();
                                    Bundle data = new Bundle();
                                    data.putString("Order Details", senderAddress);
                                    activated.setArguments(data);
                                    fm.beginTransaction()
                                            .replace(R.id.container, activated)
                                            .commit();
                                    Log.e("Found: ","False");
                                    Log.e("String address",fullAddress.getText().toString());
                                }

                            } else {
                                Log.e("Error in card query: ", e.getLocalizedMessage());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(),"Please enter an address for pickup",Toast.LENGTH_SHORT).show();
                }

            }
        });

        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullAddress.isShown()){


                } else {

                    if (!address.getText().toString().trim().isEmpty() && address.getText().toString().length() >= 8){
                        if (!unit.getText().toString().trim().isEmpty()){
                            if (!city.getText().toString().trim().isEmpty() && city.getText().toString().length() >= 5){
                                if  (!state.getText().toString().trim().isEmpty() && state.getText().toString().length() == 1){
                                    if (!zip.getText().toString().trim().isEmpty() && zip.getText().toString().length() == 4) {

                                        final String senderAddress =
                                           address.getText().toString() + " "
                                                   + city.getText().toString() + " "
                                                   + state.getText().toString() + " "
                                                   + zip.getText().toString();

                                        ParseQuery<ParseObject> checkCC = new ParseQuery<ParseObject>("User");
                                        checkCC.whereEqualTo("Username", ParseUser.getCurrentUser().getEmail());
                                        checkCC.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                FragmentManager fm = getActivity().getSupportFragmentManager();

                                                if (e==null){
                                                    if (parseObject.getBoolean("CardUsed")){
                                                        Fragment activated = new PickupConfirm();
                                                        Bundle data = new Bundle();
                                                        data.putString("Order Details",senderAddress);
                                                        activated.setArguments(data);
                                                        fm.beginTransaction()
                                                                .replace(R.id.container,activated)
                                                                .commit();
                                                    } else {
                                                        Fragment activated = new PaymentMethod();
                                                        Bundle data = new Bundle();
                                                        data.putString("Order Details",senderAddress);
                                                        activated.setArguments(data);
                                                        fm.beginTransaction()
                                                                .replace(R.id.container,activated)
                                                                .commit();
                                                    }

                                                } else {
                                                    Log.e("Error in query card: ",e.getLocalizedMessage());
                                                }
                                            }
                                        });

                                    } else {
                                        showMessageOnVerifiedFields("zipcode");
                                    }
                                } else {
                                    showMessageOnVerifiedFields("state (Ex: FL, NY)");
                               }
                           } else {
                                showMessageOnVerifiedFields("city");
                           }
                        } else {
                            showMessageOnVerifiedFields("unit/suite");
                        }
                    } else {
                        showMessageOnVerifiedFields("address");
                    }

                }
            }
        });



        return view;
    }

    private void showMessageOnVerifiedFields(String nameOfTheField){
        Toast.makeText(getActivity(),"Please enter a valid " + nameOfTheField,Toast.LENGTH_SHORT).show();
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception download url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.TRANSPARENT);
            }
            Toast.makeText(getActivity(),"Distance: " + distance + "  , Duration: " + duration,Toast.LENGTH_LONG).show();

            String time = duration.substring(Math.max(duration.length() - 5, 0));
            timedDriver.setText("Estimated available pickup in "+ time +" minutes");

            Log.e("Distance: ", distance + ", Duration: " + duration);

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        //Here we can get a user Location, just to know if GPS is working.
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        //Location will be null only if GPS is disabled. So, we have to ask to the user for turning it ON.
        if (location == null) {
            //Keep on searching for updates in the location of the user through the method requestLocationUpdates.
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


            //Show a dialog when the user has disabled the GPS on the device.
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
            dialog.setMessage("Location services are turned off.");

            //Make a button in the dialog that will show the Location settings when clicked
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps settings
                }
            });

            //Make another button to dismiss the dialog.
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();

        }
        else {
            //When the GPS is working just call the method to get the current location.
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), 9000);

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {

            Log.i("Request Log: " , "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    //This is where we handle the user current location on the Map.
    private void handleNewLocation(Location location) {
        Log.e("Request Log: ", location.toString());

        //Declare variables to get the Latitude and Longitude and unite
        // them to use it for the Android class named LatLng.
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //Create a Marker to pin the user current Location and handle the
        // rotation, bearing and other configs for the camera position on the map.
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");

        //map.addMarker(options);


        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();

        //Make the Camera animation works in a specific way.
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



    }

    @Override
    public void onResume() {
        super.onResume();
        setMap();
        googleApiClient.connect();
    }

    private void setMap(){
        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.myMap))
                .getMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(Listener);
        if (googleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            googleApiClient.disconnect();
        }
    }

    public class DirectionsJSONParser {

        /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
        public List<List<HashMap<String,String>>> parse(JSONObject jObject){

            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            JSONObject jDistance = null;
            JSONObject jDuration = null;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");

                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                    /** Traversing all legs */
                    for(int j=0;j<jLegs.length();j++){

                        /** Getting distance from the json data */
                        jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                        HashMap<String, String> hmDistance = new HashMap<String, String>();
                        hmDistance.put("distance", jDistance.getString("text"));

                        /** Getting duration from the json data */
                        jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                        HashMap<String, String> hmDuration = new HashMap<String, String>();
                        hmDuration.put("duration", jDuration.getString("text"));

                        /** Adding distance object to the path */
                        path.add(hmDistance);

                        /** Adding duration object to the path */
                        path.add(hmDuration);

                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for(int k=0;k<jSteps.length();k++){
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for(int l=0;l<list.size();l++){
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                path.add(hm);
                            }
                        }
                    }
                    routes.add(path);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }
            return routes;
        }

        /**
         * Method to decode polyline points
         * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         * */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

}
