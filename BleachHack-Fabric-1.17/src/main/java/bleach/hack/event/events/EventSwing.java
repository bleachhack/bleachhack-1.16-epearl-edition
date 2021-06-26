package bleach.hack.event.events;

import bleach.hack.event.Event;
import net.minecraft.util.Hand;

public class EventSwing extends Event {
    private Hand hand;

    public EventSwing(Hand hand) {
        this.setHand(hand);
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}