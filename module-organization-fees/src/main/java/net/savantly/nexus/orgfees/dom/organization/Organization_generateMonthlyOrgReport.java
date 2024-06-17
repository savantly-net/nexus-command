package net.savantly.nexus.orgfees.dom.organization;

import java.util.Calendar;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.nexus.common.types.MonthType;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.orgfees.dom.invoice.MonthlyOrgReport;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_generateMonthlyOrgReport {

    final Organization organization;

    public static class ActionEvent
            extends
            org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_generateMonthlyOrgReport> {
    }

    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "Draft Monthly report", associateWith = "invoices", promptStyle = PromptStyle.DIALOG)
    public MonthlyOrgReport act(
            @ParameterLayout(named = "Month") final MonthType month,
            @ParameterLayout(named = "Year") final int year) {
        return MonthlyOrgReport.withRequiredFields(organization, month, year);
    }

    @MemberSupport
    public MonthType default0Act() {
        return MonthType.JANUARY;
    }

    @MemberSupport
    public int default1Act() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

}
