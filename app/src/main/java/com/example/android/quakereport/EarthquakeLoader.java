package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by jesus on 08/07/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    // Tag para los mensajes de los logs.
    private static final String LOG_TAG_LOADER = EarthquakeLoader.class.getName();

    // Query URL.
    private String mUrl;

    // Construye un nuevo custom Loader llamado EarthquakeLoader.
    public  EarthquakeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    // Se reescribe onStartLoading() para llamar al método forceLoad() y asi hacer que se active el
    // loadInBackground().
    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    //Aquí realizaremos la tarea en segundo plano.
    @Override
    public List<Earthquake> loadInBackground () {
        // No realizaremos la request si no hay urls.
        if (mUrl == null) {
            return null;
        }
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }


}
