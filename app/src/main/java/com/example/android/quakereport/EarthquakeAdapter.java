package com.example.android.quakereport;


import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.R.id.date;

/**
 * Created by jesus on 13/06/2017.
 */

//Creamos el EarthquakeAdapter (el cual es un ArrayAdapter) para mostrar una lista de objetos
// Earthquake

public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{

    private static final String LOCATION_SEPARATOR = "of";

    //Aquí creamos nuestro EarthquakeAdapter.
    public EarthquakeAdapter (Activity context, ArrayList<Earthquake> earthquakes){
    super(context,0,earthquakes);
    }

    //Creamos el formateador para que la fecha sea mostrada en una sintaxis más correcta.
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    //Creamos el formateador para que la hora sea mostrada en una sitaxis más correcta.
    private String formatTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(dateObject);
    }
    //Creamos el formateador para que la magnitud muestre solo un decimal.
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
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

        String originalLocation = currentEarthquake.getmPlace();
        String primaryLocation;
        String locationOffset;


        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts [1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }



    //Localiza la TextView que muestra la magnitud.
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
    //Se formatea la magnitud para solo mostrar un decimal.
        String formattedMagnitude = formatMagnitude(currentEarthquake.getmMagnitude());
    //Obtiene el nombre correspondiente a la posición del objeto Earthquake, se lo asigna, y lo
    //muestra en forma de texto en la TextView magnitude.
        magnitudeTextView.setText(formattedMagnitude);
        //Colocamos el color de fondo adecuado al círculo.
        //Extraemos el fondo de la TextView, que es un GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        //Obtiene el color adecuado según la magnitud.
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());
        //Coloca el color en el círculo.
        magnitudeCircle.setColor(magnitudeColor);

    //Localiza la TextView que muestra la localización principal.
    TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
    //Obtiene el nombre correspondiente a la posición del objeto Earthquake, se lo asigna, y lo
    //muestra en forma de texto en la TextView primary_location.
        primaryLocationView.setText(primaryLocation);


    //Localiza la TextView que muestra datos secundarios sobre la localización principal.
    TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
    //Obtiene el nombre correspondiente a la posición del objeto Earthquake, se lo asigna, y lo
    //muestra en forma de texto en la TextView location_offset.
    locationOffsetView.setText(locationOffset);

    //Crea un nuevo Date Object con el tiempo dado en milisegundos.
        Date dateObject = new Date(currentEarthquake.getmTimeInMilliseconds());
        //Encuentra la TextView que muestra la fecha.
        TextView dateTextView = (TextView) listItemView.findViewById(date);
        //Establece la fecha para darla en este formato: i.e. "Mar 3, 2017".
        String formattedDate = formatDate(dateObject);
        //Lo muestra en forma de texto en la TextView Date.
        dateTextView.setText(formattedDate);

        //Localiza la TextView que muestra la hora.
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        //Establece la hora para darla en este formato: i.e. "4:30PM"
        String formattedTime = formatTime(dateObject);
        //Lo muestra en forma de texto en la TextView Time.
        timeTextView.setText(formattedTime);


    //Devuelve la ListItem (que posee 3 TextViews) de manera que sea mostrada en el ListView.
        return listItemView;
    }

}
