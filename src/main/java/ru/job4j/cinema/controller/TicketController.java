package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

@ThreadSafe
@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String buyTicket(Model model, @ModelAttribute Ticket ticket) {
        var savedTicket = ticketService.save(ticket);
        if (savedTicket.isEmpty()) {
            model.addAttribute("message", "Данный билет уже был приобретен ранее");
            return "errors/404";
        }
        String message = String.format("Ваш сеанс номер: %s, ряд: %s, место: %s",
                savedTicket.get().getSessionId(),
                savedTicket.get().getRowNumber(),
                savedTicket.get().getPlaceNumber());
        model.addAttribute("message", message);
        return "tickets/success";
    }
}
