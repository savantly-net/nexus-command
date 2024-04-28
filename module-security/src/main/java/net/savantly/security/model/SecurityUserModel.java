package net.savantly.security.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SecurityUserModel {
    
    private String uid;
    private String username;
    private String email;
    private List<String> groups;
}
