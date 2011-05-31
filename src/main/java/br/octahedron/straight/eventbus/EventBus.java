/*
 *  Straight - A system to manage financial demands for small and decentralized
 *  organizations.
 *  Copyright (C) 2011  Octahedron 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.straight.eventbus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This entity is responsible by manage subscriptions to {@link Event} and by publish {@link Event}.
 * 
 * @author Danilo Penna Queiroz
 */
public class EventBus {

	private static Lock monitor = new ReentrantLock();
	protected static Map<Class<? extends Event>, LinkedList<Subscriber>> subscribers = new HashMap<Class<? extends Event>, LinkedList<Subscriber>>();
	protected static EventPublisher eventPublisher = new AppEngineEventPublisher();

	/**
	 * To be used by tests
	 */
	protected static void reset() {
		subscribers.clear();
	}

	/**
	 * To be used by tests
	 */
	protected static void setEventPublisher(EventPublisher publisher) {
		eventPublisher = publisher;
	}

	/**
	 * Subscribes to receive notifications for the given {@link Event} classes. Once subscribed, the
	 * subscriber will start receive notifications each time an {@link Event} of one of the given
	 * types be published by any one.
	 */
	public static void subscribe(Subscriber subscriber, Class<? extends Event>... interestedEvents) {
		try {
			monitor.lock();
			for (Class<? extends Event> interestedEvent : interestedEvents) {
				if (!subscribers.containsKey(interestedEvent)) {
					subscribers.put(interestedEvent, new LinkedList<Subscriber>());
				}
				subscribers.get(interestedEvent).add(subscriber);
			}
		} finally {
			monitor.unlock();
		}
	}

	/**
	 * Publishes the given event to every {@link Subscriber} interested in the given event.
	 */
	@SuppressWarnings("unchecked")
	public static void publish(Event event) {
		try {
			monitor.lock();
			if (subscribers.containsKey(event.getClass())) {
				eventPublisher.publish((LinkedList<Subscriber>) subscribers.get(event.getClass()).clone(), event);
			}
		} finally {
			monitor.unlock();
		}
	}
}
