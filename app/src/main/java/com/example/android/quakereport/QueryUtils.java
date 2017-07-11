package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
  */

public final class QueryUtils {

     /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    // Crea un nuevo objeto URL a partir de la String stringUrl, si hay excepción se devuelve como
    // null.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problema construyendo la URL ", e);
        }
        return url;
    }
    // Crea la HTTP request para la URL obtenida y crea una String a partir de ella.
    private static String makeHttpRequest (URL url) throws IOException{
        String jsonResponse = "";

        // Si la URL = null se devuelve inmediatamente.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(50000 /* Segundos*/);
            urlConnection.setConnectTimeout(50000/* Segundos*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Si la request se realiza correctamente (código de respuesta 200) se lee el input
            // stream y se le hace parse a la respuesta.
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error de conexión: " + urlConnection.getResponseCode());
            }
            // Aquí simplemente hacemos catch a la IOException.
        } catch (IOException e) {
            Log.e (LOG_TAG, "Problema recuperando los resultados en JSON sobre el terremoto", e);
            // Independientemente de que se lance una exception o no en el bloque finally se realiza
            // una desconexión (o se "cierra" como en el caso del inputStream) para poder reusarlos.
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        // Se retorna la jsonResponse que albergará la String inputStream.
        return jsonResponse;
    }

    // Convierte la inputStream en una String con toda la respuesta en JSON del servidor.
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Devuelve una lista de objetos Earthquake creada a partir de hacer parse a una respuesta en
     * JSON.
     */
    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        // Si la String earthquakeJSON es nula o está vacía, se devuelve el resultado.
        if (TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }

        // Crea una ArrayList vacía a la que podemos añadir terremotos.
        List<Earthquake> earthquakes = new ArrayList<>();

        // Intenta hacer parse a la JSON response String.
        try {
            // Crea un JSONObject a partir de la JSON response String.
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extrae la JSONArray asociada a la key "features".
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // Por cada terremoto en la earthquakeArray añade un objeto Earthquake.
            for (int i = 0; i < earthquakeArray.length(); i++ ){
                // Escoge únicamente un terremoto según la posición de este en la lista.
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // Por cada terremoto coge el JSONObject asociado a la key "properties".
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // extrae el valor de la key llamada "mag".
                double magnitude = properties.getDouble("mag");
                // extrae el valor de la key llamada "place".
                String place = properties.getString("place");
                // extrae el valor de la key llamada "time".
                long time = properties.getLong("time");
                // extrae el valor de la key llamada "url".
                String url = properties.getString("url");

                // Crea un nuevo objeto Earthquake con estos 4 valores.
                Earthquake earthquake = new Earthquake(magnitude, place, time, url);

                // Añade este objeto a la ArrayList.
                earthquakes.add(earthquake);

            }

        } catch (JSONException e) {
            // Si ocurre algún error dentro del bloque try se capta aquí para evitar un cierre de la
            // App.
            Log.e("QueryUtils", "Problema parsing los resultados del terremoto en JSON", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    // PARTE MÁS IMPORTANTE: Aquí todos los pasos de abajo se recogerán en un único public method
    // con el que interactuará la EarthquakeActivity.
    public static List<Earthquake> fetchEarthquakeData(String requestUrl){
        // Crea el objeto URL.
        URL url = createUrl(requestUrl);

        // Realiza la HTTP request y obtiene la respuesta en JSON.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problema realizando la HTTP request.", e);
        }
        // Extrae los datos relevantes de la respuesta en JSON.
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        // Devuelve una lista de terremotos.
        return earthquakes;
    }

}