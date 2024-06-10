package net.savantly.nexus.flow.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowNodeDto {

    private String id;
    private String type;
    private Map<String,Object> data = new HashMap<>();
    private Map<String,Object> position = new HashMap<>();
    private Map<String,Object> style = new HashMap<>();
    private int width = 100;
    private int height = 50;

    public FlowNodeDto addData(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public FlowNodeDto addPosition(String key, Object value) {
        position.put(key, value);
        return this;
    }

    public FlowNodeDto addStyle(String key, Object value) {
        style.put(key, value);
        return this;
    }
}
