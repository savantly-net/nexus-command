package net.savantly.nexus.organizations.dom.organizationEvent;

import lombok.Getter;
import net.savantly.nexus.organizations.dom.organization.Organization;

public class OrganizationEvent<T> {

    @Getter
    private final T source;

    @Getter
    private final Organization organization;

    public OrganizationEvent(Organization organization, T source) {
        this.organization = organization;
        this.source = source;
    }

    
}
