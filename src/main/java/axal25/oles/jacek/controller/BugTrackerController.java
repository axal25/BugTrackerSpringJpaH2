package axal25.oles.jacek.controller;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.context.info.BugTrackerAppStatusProducer;
import axal25.oles.jacek.model.BugTrackerAppStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(Constants.EndpointPaths.BUG_TRACKER_CONTROLLER)
public class BugTrackerController {
    @Resource
    private BugTrackerAppStatusProducer bugTrackerAppStatusProducer;

    @GetMapping("/status")
    public ResponseEntity<BugTrackerAppStatus> getStatus() {
        return new ResponseEntity<>(bugTrackerAppStatusProducer.getBugTrackerAppStatus(), HttpStatus.OK);
    }
}
