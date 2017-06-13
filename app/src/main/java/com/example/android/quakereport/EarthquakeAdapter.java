package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jesus on 13/06/2017.
 */
//Creamos el EarthquakeAdapter (el cual es un ArrayAdapter) para mostrar una lista de objetos
// Earthquake

public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{

    //Aquí creamos nuestro EarthquakeAdapter.
    public EarthquakeAdapter (Activity context, ArrayList<Earthquake> earthquakes){
    super(context,0,earthquakes);
    }
    //Le damos una view al Adapter.


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Comprueba si la view está siendo reusada, si no, la infla.
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
    }

    //Obtiene el objeto Earthquake según su determinada posición.
        Earthquake currentEarthquake = getItem(position);

    //Localiza la TextView que muestra la magnitud.
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
    //Obtiene el nombre correspondiente a la posición del objeto Earthquakes, se lo asigna, y lo
    //muestra en forma de texto en la TextView magnitude.
        magnitudeTextView.setText(currentEarthquake.getmMagnitude());

    //Localiza la TextView que muestra el lugar.
        TextView placeTextView = (TextView) listItemView.findViewById(R.id.place);
    //Se obtiene el nombre correspondiente a la posición del objeto Earthquakes, se lo asigna, y
    //lo muestra en forma de texto en la TextView place.
        placeTextView.setText(currentEarthquake.getmPlace());
        //Localiza la TextView que muestra la fecha.
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        //Se obtiene el nombre correspondiente a la posición del objeto Earthquakes, se lo asigna, y
        //lo muestra en forma de texto en la TextView date.
        dateTextView.setText(currentEarthquake.getmDate());

    //Devuelve la ListItem (que posee 3 TextViews) de manera que sea mostrada en el ListView.
        return listItemView;
    }
}
