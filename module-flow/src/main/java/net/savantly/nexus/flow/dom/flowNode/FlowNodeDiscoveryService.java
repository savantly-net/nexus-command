package net.savantly.nexus.flow.dom.flowNode;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.util.AnnotatedTypeScanner;

public class FlowNodeDiscoveryService {

    public Set<FlowNodeDescriptor> discoverFlowNodes() {

        // find all classes annotated with @FlowNodeType
        // create a FlowNodeDescriptor for each class
        // return the set of FlowNodeDescriptors

        var scanner = new AnnotatedTypeScanner(FlowNodeType.class);
        var classes = scanner.findTypes("net.savantly.nexus.flow.nodes");

        return classes.stream()
                .map(clazz -> {
                    var annotation = clazz.getAnnotation(FlowNodeType.class);
                    var descriptor = new FlowNodeDescriptor();
                    descriptor.setClassName(clazz.getName());
                    descriptor.setName(annotation.name());
                    descriptor.setDescription(annotation.description());
                    descriptor.setInputParameters(getInputParameters(clazz));
                    return descriptor;
                })
                .collect(Collectors.toSet());
    }

    private Set<ParameterDescriptor> getInputParameters(Class<?> clazz) {
        // find all fields annotated with @Parameter
        // create a map of field name to field description

        var result = new HashSet<ParameterDescriptor>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Parameter.class)) {
                Parameter parameter = field.getAnnotation(Parameter.class);
                var descriptor = new ParameterDescriptor();
                descriptor.setName(parameter.value());
                descriptor.setDescription(parameter.description());
                descriptor.setType(field.getType().getName());
                result.add(descriptor);
            }
        }
        return result;
    }
}
