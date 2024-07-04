package net.local.poc.workfloworch.application.workflow.flows.steps;

import java.util.concurrent.atomic.AtomicReference;

import net.local.poc.workfloworch.application.workflow.api.WorkflowStep;
import net.local.poc.workfloworch.application.workflow.api.WorkflowStepStatus;

abstract class BaseWorkflowStep implements WorkflowStep {
    
    protected AtomicReference<WorkflowStepStatus> status = new AtomicReference<>(WorkflowStepStatus.PENDING);

    @Override
    public WorkflowStepStatus getStatus() {
        return status.get();
    }
}
