package net.savantly.nexus.audited.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

public class AuditedEntitySortedByDateDesc_test {

    @Test
    public void testCompare() {
        // given
        AuditedEntitySortedByDateDesc comparator = new AuditedEntitySortedByDateDesc();
        AuditedEntity o1 = new MockAuditedEntity();
        AuditedEntity o2 = new MockAuditedEntity();
        o1.setCreatedDate(ZonedDateTime.now().minusDays(1));
        o2.setCreatedDate(ZonedDateTime.now());

        // when
        int result = comparator.compare(o1, o2);

        // then
        assertThat(result).isPositive();

    }

    @Test
    public void testCompareWithNulls() {
        // given
        AuditedEntitySortedByDateDesc comparator = new AuditedEntitySortedByDateDesc();
        AuditedEntity o1 = new MockAuditedEntity();
        AuditedEntity o2 = new MockAuditedEntity();
        o1.setCreatedDate(ZonedDateTime.now().minusDays(1));
        o2.setCreatedDate(null);

        // when
        int result = comparator.compare(o1, o2);

        // then
        assertThat(result).isNegative();

    }

    @Test
    public void testCompareWithNullsBoth() {
        // given
        AuditedEntitySortedByDateDesc comparator = new AuditedEntitySortedByDateDesc();
        AuditedEntity o1 = new MockAuditedEntity();
        AuditedEntity o2 = new MockAuditedEntity();
        o1.setCreatedDate(null);
        o2.setCreatedDate(null);

        // when
        int result = comparator.compare(o1, o2);

        // then
        assertThat(result).isZero();

    }

    @Test
    public void testCompareWithNullsFirst() {
        // given
        AuditedEntitySortedByDateDesc comparator = new AuditedEntitySortedByDateDesc();
        AuditedEntity o1 = new MockAuditedEntity();
        AuditedEntity o2 = new MockAuditedEntity();
        o1.setCreatedDate(null);
        o2.setCreatedDate(ZonedDateTime.now().minusDays(1));

        // when
        int result = comparator.compare(o1, o2);

        // then
        assertThat(result).isPositive();

    }

    static class MockAuditedEntity extends AuditedEntity {
    }
}
