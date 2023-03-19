package axal25.oles.jacek.controller;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.service.ticket.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static axal25.oles.jacek.constant.Constants.EndpointPaths.TICKET_CONTROLLER;

@RestController
@RequestMapping(TICKET_CONTROLLER)
public class TicketController {

    @Autowired
    private ITicketService ticketService;

    @GetMapping("")
    public ResponseEntity<List<TicketEntity>> getAllTickets() {
        return new ResponseEntity<>(
                ticketService.getAllTickets(),
                HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<TicketEntity> addTicket(
            @RequestBody TicketEntity ticket,
            UriComponentsBuilder uriComponentsBuilder) {
        ticketService.addTicket(ticket);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path(TICKET_CONTROLLER + "/{id}")
                .buildAndExpand(ticket.getId())
                .toUri());
        return new ResponseEntity<>(ticket, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<TicketEntity> updateTicket(
            @RequestBody TicketEntity ticket,
            UriComponentsBuilder uriComponentsBuilder) {
        ticketService.updateTicket(ticket);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path(TICKET_CONTROLLER + "/{id}")
                .buildAndExpand(ticket.getId())
                .toUri());
        return new ResponseEntity<>(ticket, httpHeaders, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketEntity> closeTicket(
            @PathVariable("id") Integer ticketId,
            UriComponentsBuilder uriComponentsBuilder) {
        ticketService.closeTicket(ticketId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path(TICKET_CONTROLLER + "/{id}")
                .buildAndExpand(ticketId)
                .toUri());
        // TODO: fix
        return new ResponseEntity<>(
                new TicketEntity(
                        ticketId,
                        null,
                        "Resolved",
                        null,
                        null,
                        null
                ),
                httpHeaders,
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") Integer ticketId) {
        ticketService.deleteTicket(ticketId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
