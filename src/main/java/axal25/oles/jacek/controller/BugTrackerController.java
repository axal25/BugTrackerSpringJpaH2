package axal25.oles.jacek.controller;

import axal25.oles.jacek.config.AppInfoListener;
import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.model.BugTrackerAppStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.EndpointPaths.BUG_TRACKER_CONTROLLER)
public class BugTrackerController {

    @Autowired
    private AppInfoListener appInfoListener;

    @GetMapping("/status")
    public ResponseEntity<BugTrackerAppStatus> getApplicationById() {
        String fullHostAddress = null;
        try {
            fullHostAddress = appInfoListener.getFullHostAddress();
        } catch (RuntimeException e) {
        }
        return new ResponseEntity<>(
                new BugTrackerAppStatus(
                        true,
                        true,
                        appInfoListener.getLocal(),
                        appInfoListener.getRemote(),
                        appInfoListener.getPort(),
                        fullHostAddress),
                HttpStatus.OK);
    }
}
