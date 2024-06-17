package net.savantly.nexus.orgfees.dom.invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Title;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.savantly.nexus.common.types.MonthType;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.orgfees.dom.onetime.OneTimePurchase;
import net.savantly.nexus.orgfees.dom.onetime.OneTimePurchaseRepository;
import net.savantly.nexus.orgfees.dom.subscription.Subscription;
import net.savantly.nexus.orgfees.dom.subscription.SubscriptionRepository;
import net.savantly.nexus.products.dom.product.Product;
import net.savantly.nexus.products.dom.product.Products;

@DomainObject(nature = Nature.VIEW_MODEL, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@Named(OrganizationsModule.NAMESPACE + ".MonthlyOrgReport")
@DomainObjectLayout(cssClassFa = "report", named = "Monthly Organization Report")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(onlyExplicitlyIncluded = true)
@Slf4j
@XmlRootElement(name = "monthlyOrgReport")
@XmlType(propOrder = {
        "organization",
        "year",
        "month",
})
@XmlAccessorType(XmlAccessType.FIELD)
public class MonthlyOrgReport {

    @Inject
    @Transient
    @XmlTransient
    OneTimePurchaseRepository otpRepository;

    @Inject
    @Transient
    @XmlTransient
    SubscriptionRepository subscriptionRepository;

    @Inject
    @Transient
    @XmlTransient
    Products products;

    public static MonthlyOrgReport withRequiredFields(Organization organization, MonthType month, int year) {
        log.info("Creating MonthlyOrgReport for {} {} {}", organization.getName(), month, year);
        var object = new MonthlyOrgReport();
        object.organization = organization;
        object.month = month;
        object.year = year;
        return object;
    }

    @XmlTransient
    @Title
    public String getTitle() {
        return String.format("%s %s %d", organization.getName(), month, year);
    }

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private Organization organization;

    @Getter
    @Setter
    @Enumerated(value = EnumType.STRING)
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private MonthType month;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity")
    @XmlElement(required = true)
    private int year;

    @XmlTransient
    @Transient
    @Property
    @PropertyLayout(fieldSetId = "identity")
    public double getTotalAmount() {
        return getLineItems().stream().mapToDouble(MonthlyOrgReportItem::getTotalAmount).sum();
    }

    @XmlTransient
    @Transient
    @Collection
    @CollectionLayout(named = "Line Items", describedAs = "The line items for this report")
    public java.util.Collection<MonthlyOrgReportItem> getLineItems() {
        List<MonthlyOrgReportItem> items = new ArrayList<>();

        LocalDate startDate = LocalDate.of(year, month.getValue(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        var otps = otpRepository.findByOrganizationIdAndDateRange(organization.getId(), startDate, endDate);
        var subs = subscriptionRepository.findByOrganizationIdAndDateRange(organization.getId(), startDate, endDate);
        otps.forEach(otp -> {
            items.add(fromOneTimePurchase(otp));
        });
        subs.forEach(sub -> {
            items.add(fromSubscription(sub));
        });
        return items;
    }

    private MonthlyOrgReportItem fromOneTimePurchase(OneTimePurchase otp) {
        double productPrice = calculatePriceFromProduct(otp.getProduct());
        double exactAmount = productPrice * otp.getQuantity();
        double roundedTotalAmount = Math.round(exactAmount * 100.0) / 100.0;

        return new MonthlyOrgReportItem()
                .setOrganizationName(organization.getName())
                .setProductBillingAmount(calculatePriceFromProduct(otp.getProduct()))
                .setProductBillingInterval("ONE-TIME")
                .setProductDescription(otp.getProduct().getDescription())
                .setProductName(otp.getProduct().getName())
                .setProductQuantity(otp.getQuantity())
                .setProductPurchaseDate(otp.getPurchaseDate().toString())
                .setTotalAmount(roundedTotalAmount);
    }

    private MonthlyOrgReportItem fromSubscription(Subscription s) {
        double productPrice = calculatePriceFromProduct(s.getProduct());
        double totalAmount = productPrice * 1;
        return new MonthlyOrgReportItem()
                .setOrganizationName(organization.getName())
                .setProductBillingAmount(productPrice)
                .setProductBillingInterval(s.getProduct().getBillingInterval().name())
                .setProductDescription(s.getProduct().getDescription())
                .setProductName(s.getProduct().getName())
                .setProductQuantity(1d)
                .setProductPurchaseDate(s.getStartDate().toString())
                .setTotalAmount(totalAmount);
    }

    private double calculatePriceFromProduct(Product product) {
        return products.calculatePriceAtDate(product, LocalDate.of(year, month.getValue(), 1));
    }

}
