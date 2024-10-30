package net.savantly.nexus.audited.api;

import java.util.Comparator;

public class AuditedEntitySortedByDateDesc implements Comparator<AuditedEntity> {

    @Override
    public int compare(AuditedEntity o1, AuditedEntity o2) {

        // sort with nulls last
        if (o1.getCreatedDate() == null && o2.getCreatedDate() == null) {
            return 0;
        }

        if (o1.getCreatedDate() == null) {
            return 1;
        }

        if (o2.getCreatedDate() == null) {
            return -1;
        }

        return o2.getCreatedDate().compareTo(o1.getCreatedDate());
    }
}