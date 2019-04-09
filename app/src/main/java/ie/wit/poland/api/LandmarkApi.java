package ie.wit.poland.api;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.cm.activities.Home;
import ie.cm.models.Coffee;
import ie.wit.poland.models.Landmark;

public class LandmarkApi {

    private static final String hostURL = "http://coffeemate-fullfat.herokuapp.com";
    private static final String LocalhostURL = "http://192.168.0.13:3000";
    //private static List<Coffee> result = null;
    private static AlertDialog loader;

    public static void attachDialog(AlertDialog aloader) {
        loader = aloader;
    }
    public static int FLAG;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static void get(String url) {
        showLoader("Downloading Coffee Data...");
        // Request a string response
        Log.v("coffeemate","GET REQUEST : " + hostURL + url);
        JsonObjectRequest gsonRequest = new JsonObjectRequest(Request.Method.GET, hostURL + url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            FLAG = response.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Result handling
                        List<Landmark> result = null;
                        Log.v("coffeemate","COFFEE JSON DATA : " + response);
                        Type collectionType = new TypeToken<List<Coffee>>(){}.getType();

                        try {
                            result = new Gson().fromJson(response.getString("data"), collectionType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(FLAG == 99)vListener.setList(result); //99 indicates a 'GetAll' on Server
                        vListener.setCoffee(result.get(0));
                        vListener.updateUI((Fragment)vListener);
                        hideLoader();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
            }
        });

// Add the request to the queue
        Home.app.add(gsonRequest);
    }

    public static void post(String url,Coffee aCoffee) {
        Log.v("coffeemate", "POSTing to : " + url + " with " + aCoffee);
        Type objType = new TypeToken<Coffee>(){}.getType();
        String json = new Gson().toJson(aCoffee, objType);
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest gsonRequest = new JsonObjectRequest( Request.Method.POST, hostURL + url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("coffeemate", "insert new Coffee " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { // Handle Error
                        Log.v("coffeemate", "Unable to insert new Coffee");
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }
        };

        // Add the request to the queue
        Home.app.add(gsonRequest);
    }

    public static void put(final String url, Coffee aCoffee, final String usertoken) {

        Log.v("coffeemate", "PUTing to : " + url);
        Type objType = new TypeToken<Coffee>(){}.getType();
        String json = new Gson().toJson(aCoffee, objType);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest gsonRequest = new JsonObjectRequest( Request.Method.PUT, hostURL + url,

                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Result handling
                        List<Coffee> result = null;
                        Type objType = new TypeToken<List<Coffee>>(){}.getType();

                        try {
                            result = new Gson().fromJson(response.getString("data"), objType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        get("/coffees/" + usertoken); // Forcing a refresh of the updated list on the Server
                        Log.v("coffeemate", "Updating a Coffee successful with : " + response + result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Log.v("coffeemate", "Unable to update Coffee with error : " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                //headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        // Add the request to the queue
        Home.app.add(gsonRequest);
    }

    public static void delete(String url, final String usertoken) {
        showLoader("Deleting Coffee Data...");
        Log.v("coffeemate", "DELETEing from " + url);

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, hostURL + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Result handling
                        Log.v("coffeemate", "DELETE success " + response);
                        get("/coffees/" + usertoken); // Forcing a refresh of the updated list on the Server
                        hideLoader();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                Log.v("coffeemate","Something went wrong with DELETE!");
                error.printStackTrace();
            }
        });

        // Add the request to the queue
        Home.app.add(stringRequest);
    }

    public static void getGooglePhoto(String url,final ImageView googlePhoto) {
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Home.app.googlePhoto = response;
                        googlePhoto.setImageBitmap(Home.app.googlePhoto);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Something went wrong!");
                        error.printStackTrace();
                    }
                });
        // Add the request to the queue
        Home.app.add(imgRequest);
    }

    private static void showLoader(String message) {
        if (!loader.isShowing()) {
            if(message != null)loader.setTitle(message);
            loader.show();
        }
    }

    private static void hideLoader() {
        if (loader.isShowing())
            loader.dismiss();
    }
}
