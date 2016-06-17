/*
 *  Copyright 2016 __0x277F <0x277F@gmail.com>
 *  and other copyright owners as documented in the project's IP log.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License&quot￼;
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.basinmc.sink.event;

import org.basinmc.faucet.event.Event;
import org.basinmc.faucet.event.EventHandler;
import org.basinmc.faucet.event.EventSubscribe;
import org.junit.Assert;
import org.junit.Test;

@EventSubscribe
public class SinkEventBusTest {
    private static StringBuilder sb;

    @Test
    public void testBasicHandling() {
        SinkEventBus eventBus = new SinkEventBus();
        sb = new StringBuilder();
        EventHandler<Event> handler = new TestEventHandler();
        eventBus.subscribe(handler, Event.class);
        Event event = new Event() {};
        eventBus.post(event);
        Assert.assertEquals(event.toString(), sb.toString());
    }

    @Test
    public void testWrapperCreation() {
        SinkEventBus eventBus = new SinkEventBus();
        sb = new StringBuilder();
        eventBus.subscribe(this);
        Event event = new Event() {};
        eventBus.post(event);
        Assert.assertEquals(event.toString(), sb.toString());
    }

    @EventSubscribe
    public void handleWrapper(Event event) {
        sb.append(event.toString());
    }

    @EventSubscribe
    private static class TestEventHandler implements EventHandler<Event> {

        @Override
        public void handle(Event event) {
            sb.append(event.toString());
        }
    }
}
