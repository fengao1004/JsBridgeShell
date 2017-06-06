package com.dayang.cmtools.bean;

import java.util.List;

/**
 * Created by 冯傲 on 2017/3/22.
 * e-mail 897840134@qq.com
 */

public class LocationResultInfo {

    /**
     * success : true
     * description :
     * locationCoordinate : {"latitude":"44.2324","longitude":"102.1213"}
     * locations : [{"locationName":"中关村软件园11号楼"},{"locationName":"中关村软件园联想大厦"}]
     */

    private String success;
    private String description;
    private LocationCoordinateEntity locationCoordinate;
    private List<LocationsEntity> locations;

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocationCoordinate(LocationCoordinateEntity locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    public void setLocations(List<LocationsEntity> locations) {
        this.locations = locations;
    }

    public String getSuccess() {
        return success;
    }

    public String getDescription() {
        return description;
    }

    public LocationCoordinateEntity getLocationCoordinate() {
        return locationCoordinate;
    }

    public List<LocationsEntity> getLocations() {
        return locations;
    }

    public static class LocationCoordinateEntity {
        /**
         * latitude : 44.2324
         * longitude : 102.1213
         */

        private String latitude;
        private String longitude;

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }

    public static class LocationsEntity {
        /**
         * locationName : 中关村软件园11号楼
         */

        private String locationName;

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLocationName() {
            return locationName;
        }
    }
}
