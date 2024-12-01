package net.savantly.nexus.kafka.dom.userActions;

import java.util.List;
import java.util.Objects;

import org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent;
import org.apache.causeway.applib.services.metamodel.MetaModelService;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class KafkaUserActionService {

    private final MetaModelService metaModelService;

    public KafkaUserActionService(MetaModelService metaModelService) {
        this.metaModelService = metaModelService;
    }

    public List<String> visibleDomainMembersForUser() {
        var mmc = MetaModelContext.instanceElseFail();
        var authorization = mmc.getAuthorizationManager();
        var interactionContext = mmc.getInteractionService().currentInteractionContextElseFail();
        var specificationLoader = mmc.getSpecificationLoader();

        var visibleActions = metaModelService.getDomainModel().getDomainMembers().stream()
                .map(domainMember -> {
                    var typeName = domainMember.getLogicalTypeName();
                    var memberName = domainMember.getMemberName();
                    var fullTypeName = String.format("%s#%s", typeName, memberName);
                    var logicalType = specificationLoader.lookupLogicalTypeElseFail(domainMember.getLogicalTypeName());
                    var spec = specificationLoader.specForLogicalTypeElseFail(logicalType);
                    var member = spec.getMemberElseFail(domainMember.getMemberName());
                    var isAction = member.getFeatureType().isAction();
                    var identifier = member.getFeatureIdentifier();

                    if (isAction && authorization.isUsable(interactionContext, identifier)) {
                        return fullTypeName;
                    }
                    return null;
                }).distinct().filter(Objects::nonNull).toList();

        return visibleActions;
    }

    public List<String> getDomainActionClasses() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        // scanner.addIncludeFilter(new AnnotationTypeFilter(VisibleToWebhook.class));
        scanner.addIncludeFilter(new AssignableTypeFilter(ActionDomainEvent.class));

        var classes = scanner.findCandidateComponents("net.savantly");
        for (BeanDefinition bd : classes)
            log.info("Found class: " + bd.getBeanClassName());

        return classes.stream().map(BeanDefinition::getBeanClassName).toList();
    }

}
