package net.local.poc.workfloworch.application.workflow.flows.steps;

import java.util.Random;

import net.local.poc.workfloworch.application.workflow.api.WorkflowStepStatus;
import reactor.core.publisher.Mono;

public class PaymentProcessingStep extends BaseWorkflowStep {
    
    private final Random random = new Random();

    @Override
    public Mono<Boolean> process() {
        return Mono.fromCallable(() -> {
            System.out.println("Processando pagamento...");
            if (random.nextBoolean()) {
                System.out.println("Falha no processamento do pagamento!");
                status.set(WorkflowStepStatus.FAILED);
                return false;
            }
            status.set(WorkflowStepStatus.COMPLETE);
            return true;
        });
    }

    @Override
    public Mono<Boolean> revert() {
        return Mono.fromCallable(() -> {
            System.out.println("Revertendo processamento de pagamento...");
            status.set(WorkflowStepStatus.PENDING);
            return true;
        });
    }
}
