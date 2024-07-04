package net.local.poc.workfloworch.application.workflow.flows;

import java.util.Arrays;
import java.util.List;

import net.local.poc.workfloworch.application.workflow.api.Workflow;
import net.local.poc.workfloworch.application.workflow.api.WorkflowStep;
import net.local.poc.workfloworch.application.workflow.flows.steps.OrderShippingStep;
import net.local.poc.workfloworch.application.workflow.flows.steps.PaymentProcessingStep;
import net.local.poc.workfloworch.application.workflow.flows.steps.StockVerificationStep;

public class OrderWorkflow implements Workflow {
    
    private final List<WorkflowStep> steps;

    public OrderWorkflow() {
        this.steps = Arrays.asList(
            new StockVerificationStep(),
            new PaymentProcessingStep(),
            new OrderShippingStep()
        );
    }

    @Override
    public List<WorkflowStep> getSteps() {
        return steps;
    }
}