package uk.ac.abertay.notsnapchat;

import android.util.Pair;

public class RestfulDataObject {
    private String _key;
    private Object _value;

    public RestfulDataObject(String key, Object value) {
        this._key = key;
        this._value = value;
    }

    public String GetKey() {
        return this._key;
    }

    public Object GetValue() {
        return this._value;
    }

    public Pair<String, Object> GetRDO() {
        return new Pair<>(this._key, this._value);
    }

    public String getKey() {
        return this._key;
    }

    public Object getValue(){
        return this._value;
    }

    public String getValueAsString(){
        return this._value.toString();
    }
}
