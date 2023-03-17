package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketControllerTest {
    private TicketService ticketService;

    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    public void whenBuyTicketThenGetSuccessPage() {
        var ticket1 = new Ticket(1, 2, 3, 4, 5);
        var expectedMessage = "Ваш сеанс номер: 2, ряд: 3, место: 4";
        when(ticketService.save(any())).thenReturn(Optional.of(ticket1));

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(model, ticket1);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("tickets/success");
        assertThat(actualMessage).isEqualTo("Ваш сеанс номер: 2, ряд: 3, место: 4");
    }

    @Test
    public void whenBuyTicketThenGetErrorPage() {
        var expectedMessage = "Данный билет уже был приобретен ранее";
        when(ticketService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(model, any());
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
