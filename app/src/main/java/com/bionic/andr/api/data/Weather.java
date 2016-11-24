package com.bionic.andr.api.data;

import com.google.gson.annotations.SerializedName;

import com.bionic.andr.BuildConfig;

import android.support.annotation.Nullable;

import java.util.List;

/**  */
public class Weather {
    /** City id. */
    @SerializedName("id")
    private int id;
    /** City name. */
    @SerializedName("name")
    private String name;
    @SerializedName("main")
    private Temp temp;
    @SerializedName("weather")
    private List<Condition> conditions;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Temp getTemp() {
        return temp;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    @Nullable
    public String getIconUrl() {
        if (conditions.isEmpty()) {
            return null;
        }
        String icon = conditions.get(0).getIcon();
        if (icon == null) {
            return null;
        }
        return String.format(BuildConfig.ICON_TEMPLATE, icon);
    }

    public static class Condition {
        @SerializedName("id")
        private int id;
        @SerializedName("description")
        private String decr;
        @SerializedName("icon")
        private String icon;

        public int getId() {
            return id;
        }

        public String getDecr() {
            return decr;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static class Temp {
        @SerializedName("temp")
        private float current;
        @SerializedName("temp_min")
        private float min;
        @SerializedName("temp_max")
        private float max;

        public float getCurrent() {
            return current;
        }

        public float getMin() {
            return min;
        }

        public float getMax() {
            return max;
        }
    }

}
