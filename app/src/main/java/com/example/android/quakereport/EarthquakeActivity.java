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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    //URL que nos proporciona los datos de los últimos 10 terremotos ocurridos según la USGS.
    private static final String USGS_REQUEST_URL =
    "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    // Adapter para la lista de terremotos
    private EarthquakeAdapter mAdapter;

    //Aquí realizaremos la petición en una tarea secundaria.
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>{
        @Override
        protected List<Earthquake> doInBackground (String... urls){
            // No realizaremos la request si no hay urls o si la primera vale null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;

        }
        @Override
        protected void onPostExecute (List<Earthquake> data){
            // Limpia el adapter de posibles datos anteriores.
            mAdapter.clear();

            // Si hay una lista de Earthquake válida los añade al adapter y se actualizará la
            // ListView.

            if (data !=null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }

        }
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
        // Inicia la AsyncTask para extraer los datos del servidor de la USGS.
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }
}
