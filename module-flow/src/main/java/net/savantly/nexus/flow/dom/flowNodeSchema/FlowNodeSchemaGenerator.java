package net.savantly.nexus.flow.dom.flowNodeSchema;

import com.fasterxml.classmate.AnnotationInclusion;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.swagger2.Swagger2Module;

import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameter;

public class FlowNodeSchemaGenerator {

    private final SchemaGenerator generator;

    public FlowNodeSchemaGenerator() {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12,
                OptionPreset.PLAIN_JSON);


        JacksonModule jacksonModule = new JacksonModule(JacksonOption.ALWAYS_REF_SUBTYPES, JacksonOption.INLINE_TRANSFORMED_SUBTYPES);
        SchemaGeneratorConfig config = configBuilder.with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
                .with(new JakartaValidationModule())
                .with(jacksonModule)
                .with(new Swagger2Module())
                .without(Option.NONPUBLIC_NONSTATIC_FIELDS_WITHOUT_GETTERS)
                .without(Option.PUBLIC_NONSTATIC_FIELDS)
                .withAnnotationInclusionOverride(FlowNodeParameter.class, AnnotationInclusion.INCLUDE_AND_INHERIT)
                .build();

        generator = new SchemaGenerator(config);
    }

    public JsonNode generateSchema(Class<?> clazz) {
        return generator.generateSchema(clazz);
    }
}
