package com.caleb.cardviewrecyclerviewsqlite1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {

    /**
     * Get a string value from a JSON object by key.
     *
     * @param jsonObject The JSON object to parse.main
     * @param key         The key of the value to retrieve.
     * @return The string value for the given key, or null if the key is not found or the value is not a string.
     */
    public static String getString(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && !jsonObject.isNull(key)) {
                return jsonObject.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get an integer value from a JSON object by key.
     *
     * @param jsonObject The JSON object to parse.
     * @param key         The key of the value to retrieve.
     * @return The integer value for the given key, or 0 if the key is not found or the value is not an integer.
     */
    public static int getInt(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && !jsonObject.isNull(key)) {
                return jsonObject.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get a boolean value from a JSON object by key.
     *
     * @param jsonObject The JSON object to parse.
     * @param key         The key of the value to retrieve.
     * @return The boolean value for the given key, or false if the key is not found or the value is not a boolean.
     */
    public static boolean getBoolean(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && !jsonObject.isNull(key)) {
                return jsonObject.getBoolean(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get a JSON object from a JSON object by key.
     *
     * @param jsonObject The JSON object to parse.
     * @param key         The key of the value to retrieve.
     * @return The JSON object for the given key, or null if the key is not found or the value is not a JSON object.
     */
    public static JSONObject getJSONObject(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && !jsonObject.isNull(key)) {
                return jsonObject.getJSONObject(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a JSON array from a JSON object by key.
     *
     * @param jsonObject The JSON object to parse.
     * @param key         The key of the value to retrieve.
     * @return The JSON array for the given key, or null if the key is not found or the value is not a JSON array.
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && !jsonObject.isNull(key)) {
                return jsonObject.getJSONArray(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // You can add more helper methods as needed for your specific use case

}
