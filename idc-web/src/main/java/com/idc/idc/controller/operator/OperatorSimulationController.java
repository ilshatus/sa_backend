package com.idc.idc.controller.operator;



import com.google.gson.Gson;
import com.idc.idc.response.Response;
import com.idc.idc.util.SplittingPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.idc.idc.util.SplittingPath.Path;

@Api(tags = {"Operator.Simulation"})
@RestController
@RequestMapping(OperatorSimulationController.ROOT_URL)
@Slf4j
public class OperatorSimulationController {
    public static final String ROOT_URL = "/v1/operators/simulation";
    public static final String SPLIT_PATH = "/split/path";

    @ApiOperation("Get splitted path")
    @PostMapping(SPLIT_PATH)
    public ResponseEntity<Response<Path>> splitPath(@RequestBody String points) {
        try {
            Gson gson = new Gson();
            Double[][] pointsConverted = gson.fromJson(points, Double[][].class);
            return new ResponseEntity<>(new Response<>(SplittingPath.split(pointsConverted)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
