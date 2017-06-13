package com.example.android.quakereport;

/**
 * Created by jesus on 13/06/2017.
 */
// Por ahora este objeto customizado tendrá 3 valores fijos (magnitud, lugar y fecha).
public class Earthquake {

    //Intensidad de la magnitud; p. ej: 4.1, 7.5...etc.
    private String mMagnitude;
    //Lugar dónde ocurrió el terremoto; p. ej: San Francisco, Japón... etc.
    private String mPlace;
    //Fecha y año a la que ocurrieron el terremoro; p. ej: MM/DD, YYYY; y en letra, Aug 21, 2016.
    private String mDate;

    //Creamos el objeto Earthquake con sus 3 parámetros declarados previamente.
    public Earthquake (String magnitude, String place, String date){
        mMagnitude = magnitude;
        mPlace = place;
        mDate = date;
    }
    //Ahora obtenemos el valor de las variables mMagnitude, mPlace y mDate; respectivamente.
    public String getmMagnitude(){
        return mMagnitude;
    }
    public String getmDate(){
        return mDate;
    }

    public String getmPlace() {
        return mPlace;
    }
}


