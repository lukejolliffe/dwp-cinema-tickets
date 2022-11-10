package uk.gov.dwp.uc.pairtest.ticket;

public class TicketImpl implements Ticket{

    private int seatsTaken;
    private int seatPrice;
    public TicketImpl(int seatsTaken, int seatsPrice) {
        this.seatsTaken = seatsTaken;
        this.seatPrice = seatsPrice;
    }

    @Override
    public int getSeatsTaken() {
        return seatsTaken;
    }

    @Override
    public int getSeatPrice() {
        return seatPrice;
    }
}
