package com.project.sveglia;

import android.graphics.Bitmap;

/**
 * Created by simonerigon on 28/02/18.
 */

public class DataModelGoogleMaps {

    String route_detail;
    String time_to_travel;
    String origin_location;
    String destination_location;
    long time_to_travel_in_seconds;
    long arrival_time;
    long departure_time;
    String distance;
    String departure;
    String start_address;
    String end_address;
    String googleMapsRequest;


    public DataModelGoogleMaps(String route_detail,
                               String time_to_travel,
                               long time_to_travel_in_seconds,
                               long arrival_time,
                               long departure_time,
                               String distance,
                               String departure,
                               String start_address,
                               String end_address,
                               String googleMapsRequest,
                               String origin_location,
                               String destination_location){

        this.route_detail = route_detail;
        this.time_to_travel = time_to_travel;
        this.time_to_travel_in_seconds = time_to_travel_in_seconds;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.distance = distance;
        this.departure = departure;
        this.start_address = start_address;
        this.end_address = end_address;
        this.googleMapsRequest = googleMapsRequest;
        this.origin_location = origin_location;
        this.destination_location = destination_location;

    }

    public String getOrigin_location() {
        return origin_location;
    }

    public void setOrigin_location(String origin_location) {
        this.origin_location = origin_location;
    }

    public String getDestination_location() {
        return destination_location;
    }

    public void setDestination_location(String destination_location) {
        this.destination_location = destination_location;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public long getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(long departure_time) {
        this.departure_time = departure_time;
    }

    public long getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(long arrival_time) {
        this.arrival_time = arrival_time;
    }

    public long getTime_to_travel_in_seconds() {
        return time_to_travel_in_seconds;
    }

    public void setTime_to_travel_in_seconds(long time_to_travel_in_seconds) {
        this.time_to_travel_in_seconds = time_to_travel_in_seconds;
    }

    public String getRoute_detail() {
        return route_detail;
    }

    public void setRoute_detail(String route_detail) {
        this.route_detail = route_detail;
    }

    public String getTime_to_travel() {
        return time_to_travel;
    }

    public void setTime_to_travel(String time_to_travel) {
        this.time_to_travel = time_to_travel;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getGoogleMapsRequest() {
        return googleMapsRequest;
    }

    public void setGoogleMapsRequest(String googleMapsRequest) {
        this.googleMapsRequest = googleMapsRequest;
    }

}
