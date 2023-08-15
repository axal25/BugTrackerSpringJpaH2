package axal25.oles.jacek.controller;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.service.release.IReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static axal25.oles.jacek.constant.Constants.EndpointPaths.RELEASE_CONTROLLER;

@RestController
@RequestMapping(RELEASE_CONTROLLER)
public class ReleaseController {

    @Autowired
    private IReleaseService releaseService;

    @GetMapping("")
    public ResponseEntity<List<ReleaseEntity>> getAllReleases() {
        return new ResponseEntity<>(
                releaseService.getAllReleases(),
                HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ReleaseEntity> addRelease(
            @RequestBody ReleaseEntity release,
            UriComponentsBuilder uriComponentsBuilder) {
        releaseService.addRelease(release);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path(RELEASE_CONTROLLER + "/{id}")
                .buildAndExpand(release.getId())
                .toUri());
        return new ResponseEntity<>(
                release,
                httpHeaders,
                HttpStatus.CREATED);
    }

    @PutMapping("/{releaseId}/{applicationId}")
    public ResponseEntity<Void> addApplicationToRelease(
            @PathVariable("releaseId") Integer releaseId,
            @PathVariable("applicationId") Integer applicationId) {
        releaseService.addApplication(releaseId, applicationId);
        // TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
