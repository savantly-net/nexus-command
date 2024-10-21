package net.savantly.nexus.audited.api;

import java.util.Comparator;

public class AuditedEntitySortedByDateDesc implements Comparator<AuditedEntity> {

    @Override
    public int compare(AuditedEntity o1, AuditedEntity o2) {
        if (o1.getCreatedDate() == null || o2.getCreatedDate() == null) {
            return 0;
        }
        return o2.getCreatedDate().compareTo(o1.getCreatedDate());
    }
}