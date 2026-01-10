package com.example.mechanicaldesignmanager.service;

import com.example.mechanicaldesignmanager.Task;
import com.example.mechanicaldesignmanager.TaskWorkLog;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaskWorkLogService {

    public BigDecimal calculateTaskTotalHours(Task task) {


        BigDecimal total = BigDecimal.ZERO;
        List<TaskWorkLog> taskWorkLogs = task.getTaskWorkLogs();
        if (taskWorkLogs == null || taskWorkLogs.isEmpty()) {
            return total;
        }

        for (TaskWorkLog log : taskWorkLogs) {
            if (log.getHours() != null) {
                total = total.add(log.getHours());
            }

        }
        return total;
    }


}
