package top.kanetah.planH.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.format.FormatSaveProcessor;
import top.kanetah.planH.format.FormatType;

import java.util.List;

public class FormatSaveProcessorTool extends ApplicationObjectSupport {

    private static List<Class<Object>> processorInterfaces = InterfaceTool.getAllClassByInterface(
            FormatSaveProcessor.class
    );

    public FormatSaveProcessor findProcessorByTask(Task task){
        FormatSaveProcessor saveProcessor = null;
        int i;
        for (i = 0; i < processorInterfaces.size(); ++i)
            if (processorInterfaces.get(i).getAnnotation(FormatType.class).value()
                    .equals(task.getSaveProcessor()))
                try {
                    ApplicationContext context = super.getApplicationContext();
                    assert context != null;
                    saveProcessor =
                            (FormatSaveProcessor) context.getBean(processorInterfaces.get(i));
                    break;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        if (i == processorInterfaces.size())
            throw new ProcessorException();
        return saveProcessor;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "没有找到文件存储处理器")
    private class ProcessorException extends RuntimeException {
    }
}
