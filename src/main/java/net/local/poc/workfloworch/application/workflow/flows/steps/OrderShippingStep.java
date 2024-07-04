package net.local.poc.workfloworch.application.workflow.flows.steps;

import java.util.Random;

import net.local.poc.workfloworch.application.workflow.api.WorkflowStepStatus;
import reactor.core.publisher.Mono;

public class OrderShippingStep extends BaseWorkflowStep {
    
    private final Random random = new Random();

    @Override
    public Mono<Boolean> process() {
        return Mono.fromCallable(() -> {
            System.out.println("Enviando pedido...");
            if (random.nextBoolean()) {
                System.out.println("Falha no processamento do envio do pedido!");
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
            System.out.println("Revertendo envio do pedido...");
            status.set(WorkflowStepStatus.PENDING);
            return true;
        });
    }
}
