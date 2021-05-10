package es.uca.codemotionapp.items;

import java.util.Calendar;

public class Date{
    private String _sDate; //I prefer store more memory than consume more processor.
    private Calendar _cDate;

    public Date(){
        _cDate = Calendar.getInstance();
        removeFields();
    }

    public Date(String sDate){
        _sDate = sDate;
        String[] splitDate = sDate.split("/");

        _cDate = Calendar.getInstance();
        _cDate.set(Integer.parseInt(splitDate[2]), (Integer.parseInt(splitDate[1]) - 1),
                Integer.parseInt(splitDate[0]));
        removeFields();
    }

    //This method allows us to remove the unimportant fields.
    private void removeFields(){
        _cDate.set(Calendar.HOUR_OF_DAY, 0);
        _cDate.set(Calendar.MINUTE, 0);
        _cDate.set(Calendar.SECOND, 0);
        _cDate.set(Calendar.MILLISECOND, 0);
    }

    //Returns -1, 0 or 1.
    public int compare(Date date){
        return _cDate.compareTo(date._cDate);
    }

    public long getTimeInMillis(){
        return _cDate.getTimeInMillis();
    }
    public String getDate(){
        return _sDate;
    }
}
