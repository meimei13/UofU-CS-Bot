package com.mitchtalmadge.uofu_cs_bot.event;

import com.mitchtalmadge.uofu_cs_bot.event.listeners.EventListenerAbstract;
import com.mitchtalmadge.uofu_cs_bot.service.discord.DiscordService;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class EventDistributor {

    private final DiscordService discordService;

    /**
     * Maps Event Listeners to their parametrized Event types.
     */
    private final Map<Class<? extends Event>, EventListenerAbstract> eventListenerMap = new HashMap<>();

    @Autowired
    public EventDistributor(DiscordService discordService,
                            Set<EventListenerAbstract> eventListeners) {
        this.discordService = discordService;

        // Get the generic types and map them to the listeners.
        eventListeners.forEach(eventListener -> {
            //noinspection unchecked
            eventListenerMap.put(
                    (Class<? extends Event>) GenericTypeResolver.resolveTypeArgument(eventListener.getClass(), EventListenerAbstract.class),
                    eventListener
            );
        });
    }

    @PostConstruct
    private void init() {
        discordService.getJDA().addEventListener((EventListener) this::onEvent);
    }

    /**
     * Called when a Discord event takes place.
     *
     * @param event The event that took place.
     */
    public void onEvent(Event event) {
        // Check for a specific listener for the event.
        eventListenerMap.forEach((aClass, listener) -> {
            if (aClass.isAssignableFrom(event.getClass()))
                //noinspection unchecked
                listener.onEvent(event);
        });
    }

}
