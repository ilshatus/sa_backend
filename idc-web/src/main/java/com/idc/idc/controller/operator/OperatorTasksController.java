package com.idc.idc.controller.operator;

import com.idc.idc.dto.json.TaskJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Task;
import com.idc.idc.response.Response;
import com.idc.idc.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Operator.Tasks"})
@RestController
@RequestMapping(OperatorTasksController.ROOT_URL)
@Slf4j
public class OperatorTasksController {
    public static final String ROOT_URL = "/v1/operator/tasks";
    public static final String TASK_URL = "/{task_id}";

    private TaskService taskService;

    @Autowired
    public OperatorTasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiOperation("Get all not complete tasks by limit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<TaskJson>>> getAllTasks(@RequestParam Integer limit,
                                                                @RequestParam Integer offset) {
        List<Task> tasks = taskService.getNotCompleteTasks(limit, offset);
        List<TaskJson> taskJsons = tasks.stream().map(TaskJson::mapFromTask).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(taskJsons), HttpStatus.OK);
    }

    @ApiOperation("Get task by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(TASK_URL)
    public ResponseEntity<Response<TaskJson>> getTask(@PathVariable("task_id") Long taskId) {
        try {
            Task task = taskService.getTask(taskId);
            return new ResponseEntity<>(new Response<>(TaskJson.mapFromTask(task)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
