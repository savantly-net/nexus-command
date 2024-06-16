package net.savantly.nexus.flow.nodes;

import java.util.List;

import org.apache.causeway.applib.services.email.EmailService;

import lombok.Getter;
import lombok.Setter;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowContext.VariableReplacement;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;
import net.savantly.nexus.flow.dom.flowNode.impl.BasicFlowNode;
import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameter;

@FlowNodeType(name = "Send Email", description = "Send an email")
public class SendEmailNode extends BasicFlowNode {

    private final EmailService emailService;

    public SendEmailNode(String id, EmailService emailService) {
        super(id);
        this.emailService = emailService;
    }

    @FlowNodeParameter("to")
    @Getter
    @Setter
    private String to;

    @FlowNodeParameter("cc")
    @Getter
    @Setter
    private String cc;

    @FlowNodeParameter("bcc")
    @Getter
    @Setter
    private String bcc;

    @FlowNodeParameter("subject")
    @Getter
    @Setter
    private String subject;

    @FlowNodeParameter("body")
    @Getter
    @Setter
    private String body;

    @Override
    public void execute(FlowContext context) {

        try {
            emailService.send(List.of(VariableReplacement.replaceVariables(to, context)), 
                    List.of(VariableReplacement.replaceVariables(cc, context)), 
                    List.of(VariableReplacement.replaceVariables(bcc, context)), 
                    VariableReplacement.replaceVariables(subject, context), 
                    VariableReplacement.replaceVariables(body, context));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
