package net.savantly.nexus.flow.dom.flowNode;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.util.AnnotatedTypeScanner;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameter;
import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameterDescriptor;
import net.savantly.nexus.flow.dom.flowNodeSchema.FlowNodeSchemaGenerator;

@RequiredArgsConstructor
public class FlowNodeDiscoveryService {

    private final FlowNodeSchemaGenerator schemaGenerator;

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
                    //descriptor.setInputParameters(getInputParameters(clazz));
                    descriptor.setSchema(generateSchema(clazz));
                    return descriptor;
                })
                .collect(Collectors.toSet());
    }

    private JsonNode generateSchema(Class<?> clazz) {
        return schemaGenerator.generateSchema(clazz);
    }

    private Set<FlowNodeParameterDescriptor> getInputParameters(Class<?> clazz) {
        // find all fields annotated with @Parameter
        // create a map of field name to field description

        var result = new HashSet<FlowNodeParameterDescriptor>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FlowNodeParameter.class)) {
                FlowNodeParameter parameter = field.getAnnotation(FlowNodeParameter.class);
                var descriptor = new FlowNodeParameterDescriptor();
                descriptor.setName(parameter.value());
                descriptor.setDescription(parameter.description());
                descriptor.setType(field.getType().getName());
                result.add(descriptor);
            }
        }
        return result;
    }
}
