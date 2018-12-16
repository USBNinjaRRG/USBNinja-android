package com.RRG.usbninja;

import org.json.*;
import java.util.*;


public class ButtonModel{

    private String name = "";
    private String touchDown = "";
    private String touchUp = "";

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setTouchDown(String touchDown){
        this.touchDown = touchDown;
    }
    public String getTouchDown(){
        return this.touchDown;
    }
    public void setTouchUp(String touchUp){
        this.touchUp = touchUp;
    }
    public String getTouchUp(){
        return this.touchUp;
    }

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public ButtonModel(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        name = jsonObject.optString("name", "");
        touchDown = jsonObject.optString("touchDown", "");
        touchUp = jsonObject.optString("touchUp", "");
    }

    public ButtonModel(String jsonObjectStr){
        if(jsonObjectStr == null){
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonObjectStr);

            if(jsonObject == null){
                return;
            }
            name = jsonObject.optString("name", "");
            touchDown = jsonObject.optString("touchDown", "");
            touchUp = jsonObject.optString("touchUp", "");

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("touchDown", touchDown);
            jsonObject.put("touchUp", touchUp);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String toJsonObjectStr() {

        return this.toJsonObject().toString();
    }

}
