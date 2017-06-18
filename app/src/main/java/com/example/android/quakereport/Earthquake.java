package com.example.android.quakereport;

/**
 * Created by jesus on 13/06/2017.
 */
// Por ahora este objeto customizado tendrá 3 valores fijos (magnitud, lugar y fecha).
public class Earthquake {

    //Intensidad de la magnitud; p. ej: 4.1, 7.5...etc.
    private double mMagnitude;
    //Lugar dónde ocurrió el terremoto; p. ej: San Francisco, Japón... etc.
    private String mPlace;
    //Fecha y año a la que ocurrieron el terremoro; p. ej: MM/DD, YYYY; y en letra, Aug 21, 2016.
    private long mTimeInMilliseconds;
    //url respectiva al terremoto.
    private String mUrl;

    //Creamos el objeto Earthquake con sus 3 parámetros declarados previamente.
    public Earthquake (double magnitude, String place, long timeInMilliseconds, String url){
        mMagnitude = magnitude;
        mPlace = place;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }
    //Ahora obtenemos el valor de las variables mMagnitude, mPlace, mDate y mUrl; respectivamente.
    public double getmMagnitude(){
        return mMagnitude;
    }
    public long getmTimeInMilliseconds(){ return mTimeInMilliseconds; }
    public String getmPlace() {
        return mPlace;
    }
    public String getmUrl() { return  mUrl; }
}


