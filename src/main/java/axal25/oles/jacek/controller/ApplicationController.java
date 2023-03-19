package axal25.oles.jacek.controller;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.service.application.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static axal25.oles.jacek.constant.Constants.EndpointPaths.APPLICATION_CONTROLLER;

@RestController
@RequestMapping(APPLICATION_CONTROLLER)
public class ApplicationController {

    @Autowired
    private IApplicationService applicationService;

    @GetMapping("")
    public ResponseEntity<List<ApplicationEntity>> getAllApplications() {
        return new ResponseEntity<>(
                applicationService.getAllApplications(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationEntity> getApplicationById(@PathVariable("id") Integer id) {
        ApplicationEntity application = applicationService.getApplicationById(id);
        return new ResponseEntity<>(
                application,
                application == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ApplicationEntity> addApplication(
            @RequestBody ApplicationEntity application,
            UriComponentsBuilder uriComponentsBuilder) {
        boolean isCreated = applicationService.addApplication(application);
        if (!isCreated) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.CONFLICT);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path(APPLICATION_CONTROLLER + "/{id}")
                .buildAndExpand(application.getId())
                .toUri());
        return new ResponseEntity<>(application, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<ApplicationEntity> updateApplication(@RequestBody ApplicationEntity updated) {
        applicationService.updateApplication(updated);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable("id") Integer applicationId) {
        applicationService.deleteApplication(applicationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
