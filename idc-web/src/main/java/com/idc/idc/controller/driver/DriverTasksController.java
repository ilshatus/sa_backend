package com.idc.idc.controller.driver;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.json.TaskJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.exception.UnauthorizedException;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Driver.Tasks"})
@RestController
@RequestMapping(DriverTasksController.ROOT_URL)
@Slf4j
public class DriverTasksController {
    public static final String ROOT_URL = "/v1/driver/tasks";
    public static final String TASK = "/{task_id}";
    public static final String ACTIVATE = TASK + "/activate";
    public static final String COMPLETE = TASK + "/complete";

    private TaskService taskService;

    @Autowired
    public DriverTasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiOperation("Get all tasks of current driver, which has active status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<TaskJson>>> getTasksInProgress(@AuthenticationPrincipal CurrentUser currentUser) {
        List<Task> tasks = taskService.getTasksByDriverAndStatus(currentUser.getId(), TaskStatus.IN_PROGRESS);
        List<TaskJson> taskJsons = tasks.stream().map(TaskJson::mapFromTask).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(taskJsons), HttpStatus.OK);
    }

    @ApiOperation("Activate task")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(ACTIVATE)
    public ResponseEntity<Response<String>> activateTask(@PathVariable("task_id") Long taskId,
                                                         @AuthenticationPrincipal CurrentUser currentUser) {
        try {
            taskService.changeStatus(taskId, TaskStatus.IN_PROGRESS, currentUser.getId());
            return new ResponseEntity<>(new Response<>("ok"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation("Activate task")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(COMPLETE)
    public ResponseEntity<Response<String>> completeTask(@PathVariable("task_id") Long taskId,
                                                         @AuthenticationPrincipal CurrentUser currentUser) {
        try {
            taskService.changeStatus(taskId, TaskStatus.PENDING_COMPLETE, currentUser.getId());
            return new ResponseEntity<>(new Response<>("ok"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
