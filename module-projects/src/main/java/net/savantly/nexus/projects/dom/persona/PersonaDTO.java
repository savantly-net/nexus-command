package net.savantly.nexus.projects.dom.persona;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private long version;
    private String name;
    private String description;
    private String goals;
    private String painPoints;
    private String behaviors;
    private String technologyUse;
    private int age;
    private String gender;
    private String educationLevel;
    private String occupation;
    private String location;
    private String industry;
    private String jobRole;
    private String careerPath;
    private String skills;
    private String personalityTraits;
    private String personalValues;
    private String motivations;
    private String frustrations;
}
