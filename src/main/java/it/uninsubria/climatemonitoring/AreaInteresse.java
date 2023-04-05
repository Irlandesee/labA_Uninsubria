package it.uninsubria.climatemonitoring;

import java.io.Serializable;

/**
 * @author : Mattia Mauro Lunardi, 736898, mmlunardi@studenti.uninsubria.it, VA
 * @author : Andrea Quaglia, 753166, aquaglia2@studenti.uninsubria.it, VA
 **/
public class AreaInteresse implements Serializable {

    private String geonameID;
    private String asciiName;
    private String country;
    private String countryCode;
    private float latitude;
    private float longitude;

    /**
     *
     * @param geonameID
     * @param asciiName
     * @param countryCode
     * @param country
     * @param latitude
     * @param longitude
     */
    public AreaInteresse(String geonameID, String asciiName, String countryCode, String country, float latitude, float longitude){
        this.geonameID = geonameID;
        this.asciiName = asciiName;
        this.country = country;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getGeonameID() {
        return geonameID;
    }

    public void setGeonameID(String geonameID) {
        this.geonameID = geonameID;
    }

    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(this.geonameID).append(";")
                .append(this.asciiName).append(";")
                .append(this.countryCode).append(";")
                .append(this.country).append(";")
                .append(this.latitude).append(",")
                .append(this.longitude);
        return builder.toString();
    }
}
