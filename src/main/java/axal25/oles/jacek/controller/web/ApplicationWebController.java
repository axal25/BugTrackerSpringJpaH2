package axal25.oles.jacek.controller.web;

import axal25.oles.jacek.context.info.EndpointsProvider;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.service.application.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static axal25.oles.jacek.constant.Constants.EndpointPaths.WebEndpointPaths.APPLICATION_WEB_CONTROLLER;
import static axal25.oles.jacek.constant.Constants.Templates.ALL_APPLICATIONS;

@Controller
@RequestMapping(APPLICATION_WEB_CONTROLLER)
public class ApplicationWebController {
    private final IApplicationService applicationService;
    private final EndpointsProvider endpointsProvider;

    @Autowired
    ApplicationWebController(IApplicationService applicationService, EndpointsProvider endpointsProvider) {
        this.applicationService = applicationService;
        this.endpointsProvider = endpointsProvider;
    }

    @GetMapping("")
    public String getAllApplications(Model model) {
        List<ApplicationEntity> allApplications = applicationService.getAllApplications();
        model.addAttribute("allApplications", allApplications);
        model.addAttribute("descriptionToEndpointMap", endpointsProvider.getDescriptionToEndpointMap());
        return ALL_APPLICATIONS;
    }
}
