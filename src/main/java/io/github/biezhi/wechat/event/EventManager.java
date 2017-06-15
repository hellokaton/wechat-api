package io.github.biezhi.wechat.event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 事件管理器
 *
 * @author biezhi
 *         15/06/2017
 */
public class EventManager {

    private Map<EventType, List<EventListener>> listenerMap;

    public EventManager() {
        EventType[] eventTypes = EventType.values();
        for (EventType eventType : eventTypes) {
            this.listenerMap.put(eventType, new LinkedList<EventListener>());
        }
    }

    public void addEventListener(EventType type, EventListener listener) {
        listenerMap.get(type).add(listener);
    }

    public void fireEvent(EventType type) {
        List<EventListener> eventListeners = listenerMap.get(type);
        for (EventListener eventListener : eventListeners) {
            eventListener.handleEvent(new Event(type));
        }
    }

}
