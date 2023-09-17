package com.tetash.appupdateandnotifacation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
/*    make a php file in your hosting

            <?php
// Create an associative array with the values
    $data = array(
    "appversion" => "2",
            "showmessage" => true,
            "title" => "App Update ",
            "message" => "This is a sample message. for app update",
            "link" => "https://play.google.com/store/apps/details?id=coqurantafsir"
    );

// Encode the array as JSON
    $jsonData = json_encode($data);

// Check for errors in JSON encoding
if (json_last_error() !== JSON_ERROR_NONE) {
        echo "JSON encoding error: " . json_last_error_msg();
    } else {
        // Set the Content-Type header to indicate JSON data
        header('Content-Type: application/json');

        // Output the JSON data
        echo $jsonData;
    }
?>*/
    String url = "http://webaite.xyz/Apps/update_and_notifacation.php";




    LinearLayout linearLayout1 ;
    LinearLayout linearLayout2 ;
    TextView textView ,textView2;
    Button button ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         linearLayout1 = findViewById(R.id.linear_layout1);
         linearLayout2 = findViewById(R.id.linear_layout2);
         textView = findViewById(R.id.text_view);
         button = findViewById(R.id.button);
        textView = findViewById(R.id.text_view); // Added
        textView2 = findViewById(R.id.text_view2); // Added

        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Define the URL of your PHP script

        // Create a JsonObjectRequest to make a GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String appVersion = response.getString("appversion");
                            int appV= Integer.parseInt(appVersion);
                                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                int versionCode = packageInfo.versionCode;
                                if (versionCode<appV) {

                                    linearLayout1.setVisibility(View.GONE);
                                    linearLayout2.setVisibility(View.VISIBLE);
                                    // Retrieve values from the JSON response
                                    boolean showMessage = response.getBoolean("showmessage");
                                    String title = response.getString("title");
                                    String message = response.getString("message");
                                    String link = response.getString("link");
                                    textView.setText("" + title);
                                    textView2.setText("" + showMessage);
                                    TextView shordt = findViewById(R.id.show);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("" + link)));
                                        }
                                    });
                                    // Display the retrieved values in a toast
                                    String toastMessage = "App Version: " + appVersion +
                                            "\nShow Message: " + showMessage +
                                            "\nTitle: " + title +
                                            "\nMessage: " + message +
                                            "\nLink: " + link;

                                    shordt.setText("" + toastMessage);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (PackageManager.NameNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}