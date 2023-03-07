package persistence;

import org.json.JSONObject;

// describes objects that can be written to json
public interface Writeable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
