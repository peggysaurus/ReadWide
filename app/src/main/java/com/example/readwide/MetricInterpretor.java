package com.example.readwide;

import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MetricInterpretor {
    HashMap<String, ArrayList<String>> metrics = new HashMap<>();

    public MetricInterpretor(String jsonMetrics){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            List<HashMap<String, Object>> temp = mapper.readValue(jsonMetrics,new TypeReference<List<Map<String,Object>>>(){});
            Set<String> keys = temp.get(0).keySet();
            for(String key : keys){
                Log.d("Peggy","found: " + temp.get(0).get(key));
                parseObjectToArray(temp.get(0).get(key).toString(), key);
            }
        } catch (Exception e){
            Log.d("Peggy","interpretor error " + e);
        }
    }

    private void parseObjectToArray(String list, String key){
        ArrayList<String> toMap = new ArrayList<>();
        if(list.contains("[")||list.contains("]")){
            String next = list.substring(list.indexOf("[")+1,list.indexOf("]"));
            String[] results = next.split(", ");

            for(String s : results){
                toMap.add(s);
            }
        }
        else{
            toMap.add(list);
        }
        metrics.put(key,toMap);
    }

    public void addValue(String key, String value){
        metrics.get(key).add(value);
    }

    public String getMapAsJson (){
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> temp = new HashMap<String, Object>();
            for(String key : metrics.keySet()){
                temp.put(key,metrics.get(key));
            }
            String output = mapper.writeValueAsString(temp);
            Log.d("Peggy","New metrics = " + output);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<String> getKeys(){
        return metrics.keySet();
    }

    public ArrayList<String> getList(String key){
        return metrics.get(key);
    }

}
