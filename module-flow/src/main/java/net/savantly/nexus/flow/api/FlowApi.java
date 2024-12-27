package net.savantly.nexus.flow.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.apache.causeway.applib.services.eventbus.EventBusService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.api.events.submitFormJson.SubmitFormJsonEvent;
import net.savantly.nexus.flow.api.events.submitFormJson.SubmitFormJsonEventData;
import net.savantly.nexus.flow.dom.files.FileEntities;
import net.savantly.nexus.flow.dom.files.FileEntityDto;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDescriptor;

@RestController
@RequestMapping("${nexus.modules.flow.api-path:/api/public/flow}")
@RequiredArgsConstructor
@Log4j2
public class FlowApi {

    private final FlowService flowService;
    private final FileEntities files;
    private final ObjectMapper mapper;
    private final EventBusService eventBusService;

    @GetMapping("/definitions")
    public FlowDefinitions getFlowDefinitions() {
        return flowService.getFlowDefinitions();
    }

    @GetMapping("/node-types")
    public Set<FlowNodeDescriptor> getFlowNodeDiscoveryService() {
        return flowService.getAvailableFlowNodeDescriptors();
    }

    @PostMapping("/execute/{flowId}")
    public void executeFlow(@PathVariable String flowId, @RequestBody Object payload,
            @RequestHeader(name = "api-key", required = false) String apiKey) {
        log.info("Requested to execute flow: {}", flowId);
        flowService.executeFlow(flowId, payload, apiKey);
    }

    @PostMapping(value = "/submit/{formId}", consumes = "application/json")
    public ResponseEntity submitFormJson(@PathVariable String formId, @RequestBody Object payload,
            @RequestHeader(name = "api-key", required = false) String apiKey,
            @RequestParam(name = "g-recaptcha-response", required = false) String recaptcha,
            @RequestHeader(name = "X-Forwarded-For", required = false) String clientIP, HttpServletRequest request)
            throws JsonProcessingException {
        log.info("Requested to submit form: {}", formId);

        Map<String, Object> payloadMap = null;
        if (payload instanceof Map) {
            payloadMap = (Map<String, Object>) payload;
        } else if (List.class.isAssignableFrom(payload.getClass())) {
            payloadMap = new HashMap<String, Object>();
            payloadMap.put("payload_array", payload);
        } else {
            payloadMap = mapper.readValue(mapper.writeValueAsString(payload), Map.class);
        }

        if (Objects.isNull(recaptcha)) {
            log.debug("attempting to get recaptcha from payload");
            if (payloadMap.containsKey("g-recaptcha-response")) {
                var rcFromPayload = payloadMap.get("g-recaptcha-response");
                if (rcFromPayload instanceof String) {
                    recaptcha = (String) rcFromPayload;
                } else {
                    recaptcha = mapper.writeValueAsString(rcFromPayload);
                }
            } else {
                log.debug("recaptcha not found in payload");
            }
        }

        try {
            var submission = flowService.submitForm(formId, payloadMap, apiKey, recaptcha, clientIP);
            log.info("publishing event for formId: {}", formId);
            var eventData = SubmitFormJsonEventData.builder().submission(submission).clientIP(clientIP)
                    .request(request).build();
            var event = new SubmitFormJsonEvent(eventData);
            eventBusService.post(event);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (IllegalArgumentException e) {
            var errBody = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(errBody);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/files/{organizationId}")
    public ResponseEntity createFile(@RequestParam("file") MultipartFile file, @PathVariable String organizationId,
            @RequestParam(name = "public", required = false) Boolean isPublic,
            HttpServletRequest request) {
        log.info("Requested to upload file");
        try {
            var entity = files.uploadFile(organizationId, file);
            if (Objects.nonNull(isPublic)) {
                entity.setPublicFile(isPublic);
            }
            log.info("File uploaded: {}", entity);

            // get the url used to download the file
            var url = request.getRequestURL().toString().replace(organizationId, entity.getId());
            var urlWithApiKey = url + "?api-key=" + entity.getApiKey();
            var dto = new FileEntityDto().setId(entity.getId()).setUrl(urlWithApiKey);
            return ResponseEntity.ok(dto);
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity downloadFile(@PathVariable String fileId,
            @RequestParam(name = "api-key", required = false) String apiKey) {
        log.info("Requested to get file: {}", fileId);
        var entity = files.findById(fileId);
        if (entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!entity.get().isPublicFile()) {
            if (Objects.isNull(apiKey) || apiKey.isBlank() || Objects.isNull(entity.get().getApiKey())
                    || entity.get().getApiKey().isBlank()) {
                return ResponseEntity.status(403).build();
            }
            if (!entity.get().getApiKey().equals(apiKey)) {
                return ResponseEntity.status(403).build();
            }
        }
        if (entity.isPresent()) {
            var file = entity.get().getFile();
            var bytes = file.getBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(bytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
