package es.uca.codemotionapp.items;

public class Program {
    private String _sHour, _sTitle, _sLocation, _sSpeaker;

    public Program(String sHour, String sTitle, String sSpeaker, String sLocation){
        _sHour = "Horario: "  + sHour;
        _sTitle = "Titulo: " + sTitle;
        _sSpeaker = "Ponente: " + sSpeaker;
        _sLocation = "Localizacion: " + sLocation;

        if(sSpeaker.equals(""))
            _sSpeaker = "Le ofrecemos un Sandwich Mixto gratis";
    }

    public String getHour(){
        return _sHour;
    }

    public String getTitle(){
        return _sTitle;
    }

    public String getSpeaker() { return _sSpeaker; }

    public String getLocation(){
        return _sLocation;
    }

}
