/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    //URL que nos proporciona los datos de los últimos 10 terremotos ocurridos según la USGS.
    private static final String USGS_REQUEST_URL =
    "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    // Adapter para la lista de terremotos
    private EarthquakeAdapter mAdapter;

    // Simplemente añadimos un ID a nuestro loader, en caso de que en un futuro haya más de uno.
    private static final int EARTHQUAKE_LOADER_ID = 1;

    // Ahora tenemos que reescribir los 3 métodos propios de la interfaz LoaderCallbacks.

    // Primero reescribiremos onCreateLoader() para cuando el LoaderManager ha determinado que
    // nuestro Loader no sirve y hace falta uno nuevo.
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle){
        // Crea un nuevo Loader para la URL dada.
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    // En segundo lugar utilizaremos onLoadFinished() para actualizar la UI.
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes){
        // Limpia el Adapter de datos anteriores.
        mAdapter.clear();

        // Si hay una lista válida de Earthquakes los añade al Adapter haciendo que se actualice la
        // UI.
        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
        }
    }

    // Y en tercer lugar utilizamos onLoaderReset() para eliminar el loader en caso de que no sea
    // válido.
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader){
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        //Buscamos la ListView que hicimos en la earthquake_activity.xml.
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Crea un ArrayAdapter de earthquakes.
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        //Coloca el Adapter en la ListView, así logramos que la lista aparezca en la interfaz de
        //usuario.
        earthquakeListView.setAdapter(mAdapter);

        // Creamos un OnItemClickListener en la earthquakeListView.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Detecta cuál Earthquake has pulsado.
                Earthquake currentEarthquake = mAdapter.getItem(position);

                //Convierte la String url en Uri para que pueda ser usada en el Intent.
                Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());

                //Se crea una Intent para ver la Earthquake Uri.
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                //Manda a la Intent a generar una nueva Acivity.
                startActivity(websiteIntent);

            }
        });

        // Hacemos referenica al LoaderManager, para así poder interactuar con Loaders.
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }
}
