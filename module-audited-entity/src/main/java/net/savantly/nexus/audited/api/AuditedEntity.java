package net.savantly.nexus.audited.api;

import java.time.ZonedDateTime;

import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditedEntity {

    @Getter
    @Setter
    @CreatedBy
    @PropertyLayout(fieldSetId = "metadata", sequence = "10.1", hidden = Where.PARENTED_TABLES)
    private String createdBy;

    @Getter
    @Setter
    @LastModifiedBy
    @PropertyLayout(fieldSetId = "metadata", sequence = "10.2", hidden = Where.PARENTED_TABLES)
    private String lastModifiedBy;

    @Getter
    @Setter
    @CreatedDate
    @PropertyLayout(fieldSetId = "metadata", sequence = "10.3", hidden = Where.PARENTED_TABLES)
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Getter
    @Setter
    @LastModifiedDate
    @PropertyLayout(fieldSetId = "metadata", sequence = "10.4", hidden = Where.PARENTED_TABLES)
    private ZonedDateTime modifiedDate = ZonedDateTime.now();
}
