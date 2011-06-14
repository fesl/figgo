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
package br.octahedron.commons.eventbus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.taskqueue.Queue;

/**
 * @author Danilo Queiroz
 */
public class EventBusTest {

	private Subscriber consumerOne;
	private Subscriber consumerTwo;
	private Queue queue;

	@Before
	public void setUp() {
		EventBus.reset();
		this.consumerOne = createMock(Subscriber.class);
		this.consumerTwo = createMock(Subscriber.class);
		this.queue = createMock(Queue.class);
		EventBus.setEventPublisher(new AppEngineEventPublisher(this.queue));
	}

	@Test
	public void consumeTest() {
		Event event = new EventOne();
		new AppEngineEventPublisher.PublishTask(SubscriberOne.class, event).run();
		assertEquals(EventOne.class, SubscriberOne.receivedEvent.getClass());
	}

	@Test
	public void subscribeTest() {
		replay(this.consumerOne, this.consumerTwo, this.queue);

		assertFalse(EventBus.subscribers.containsKey(EventOne.class));
		assertFalse(EventBus.subscribers.containsKey(EventTwo.class));

		EventBus.subscribe(SubscriberOne.class);
		EventBus.subscribe(SubscriberTwo.class);

		assertTrue(EventBus.subscribers.containsKey(EventOne.class));
		assertEquals(1, EventBus.subscribers.get(EventOne.class).size());
		assertTrue(EventBus.subscribers.containsKey(EventTwo.class));
		assertEquals(2, EventBus.subscribers.get(EventTwo.class).size());

		verify(this.consumerOne, this.consumerTwo, this.queue);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void publishTest() {
		/*
		 * How this test can be better? I've tried to do a better mock setup, specifying the
		 * parameters to be passed, but with out success. EasyMock doesn't recognize the both lists
		 * (the passed one and the one that EventBus passes) as equals. Sadly.
		 */
		Event event = new EventTwo();
		expect(this.queue.add(notNull(Iterable.class))).andReturn(null);
		replay(this.consumerOne, this.consumerTwo, this.queue);

		EventBus.subscribe(SubscriberOne.class);
		EventBus.subscribe(SubscriberTwo.class);
		EventBus.publish(event);

		verify(this.consumerOne, this.consumerTwo, this.queue);
	}
}

