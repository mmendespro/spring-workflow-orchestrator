package net.local.poc.workfloworch;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.local.poc.workfloworch.application.workflow.api.Workflow;
import net.local.poc.workfloworch.application.workflow.api.WorkflowStep;
import net.local.poc.workfloworch.application.workflow.api.WorkflowStepStatus;
import net.local.poc.workfloworch.application.workflow.flows.OrderWorkflow;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class WorkflowOrchestratorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowOrchestratorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
 
        Workflow orderWorkflow = new OrderWorkflow();
        
        Mono.just(orderWorkflow)
            .flatMap(workflow -> processWorkflow(workflow.getSteps()))
            .doOnSuccess(success -> {
                if (success) {
                    System.out.println("Workflow concluído com sucesso!");
                } else {
                    System.out.println("Workflow falhou. Reversão concluída.");
                }
            })
            .doFinally(signalType -> {
                System.out.println("Resultado final do workflow:");
                orderWorkflow.getSteps().forEach(step -> 
                    System.out.println(step.getClass().getSimpleName() + ": " + step.getStatus())
                );
            })
            .subscribe();
    }

    private Mono<Boolean> processWorkflow(List<WorkflowStep> steps) {
        return  processSteps(steps, 0).onErrorResume(e -> {
                    System.out.println("Erro durante o processamento: " + e.getMessage());
                    return revertSteps(steps, steps.size() - 1).thenReturn(false);
                });
    }

    private Mono<Boolean> processSteps(List<WorkflowStep> steps, int index) {
        if (index >= steps.size()) {
            return Mono.just(true);
        }
        return steps.get(index).process()
            .flatMap(success -> {
                if (success) {
                    return processSteps(steps, index + 1);
                } else {
                    return revertSteps(steps, index).thenReturn(false);
                }
            });
    }

    private Mono<Void> revertSteps(List<WorkflowStep> steps, int index) {
        if (index < 0) {
            return Mono.empty();
        }
        WorkflowStep step = steps.get(index);
        return (step.getStatus() == WorkflowStepStatus.COMPLETE || step.getStatus() == WorkflowStepStatus.FAILED ? step.revert() : Mono.just(true)).then(revertSteps(steps, index - 1));
    }
}