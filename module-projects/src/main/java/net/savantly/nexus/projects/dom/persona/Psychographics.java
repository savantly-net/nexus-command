package net.savantly.nexus.projects.dom.persona;

import java.util.List;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.experimental.Accessors;

@Embeddable
@Data
@Accessors(chain = true)
class Psychographics {
    private List<String> personalityTraits;
    private List<String> values;
    private List<String> motivations;
    private List<String> frustrations;
}