/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.junit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
//import org.apache.activemq.artemis.core.server.Queue;
import javax.jms.Queue;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

public class EmbeddedMirroringFileConfigurationTest {

   // These values must match the contents of the configuration file
   static final String TEST_QUEUE = "exampleQueue";
   static final String TEST_ADDRESS = "exampleQueue";

   private EmbeddedActiveMQResource server0 = new EmbeddedActiveMQResource("broker0.xml");
   private EmbeddedActiveMQResource server1 = new EmbeddedActiveMQResource("broker1.xml");

   @Rule
   public RuleChain rulechain0 = RuleChain.outerRule(server0);

   @Rule
   public RuleChain rulechain1 = RuleChain.outerRule(server1);

   @After
   public void tear() {
      server0.stop();
      server1.stop();
   }

   @Test
   public void TestMirroring() throws Exception {
	   try {
		   System.out.println("050. Server1 Message Count: " + server1.getMessageCount(TEST_QUEUE));
		   System.out.println("100. Server0 Message Count: " + server0.getMessageCount(TEST_QUEUE));
		   server0.sendMessage(TEST_ADDRESS, "Hello");
		   System.out.println("200. Server0 Message Count: " + server0.getMessageCount(TEST_QUEUE));
		   Thread.sleep(30000); // Sleep and see if mirroring works?
		   System.out.println("300. Server1 Message Count: " + server1.getMessageCount(TEST_QUEUE));
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
   }

//   @Test
//   public void testConfiguredQueue() throws Exception {
//      assertNotNull(TEST_QUEUE + " should exist", server0.locateQueue(TEST_QUEUE));
//      List<Queue> boundQueues = server0.getBoundQueues(TEST_ADDRESS);
//      assertNotNull("List should never be null", boundQueues);
//      assertEquals("Should have one queue bound to address " + TEST_ADDRESS, 1, boundQueues.size());
//
//      assertNotNull(TEST_QUEUE + " should exist", server1.locateQueue(TEST_QUEUE));
//      boundQueues = server1.getBoundQueues(TEST_ADDRESS);
//      assertNotNull("List should never be null", boundQueues);
//      assertEquals("Should have one queue bound to address " + TEST_ADDRESS, 1, boundQueues.size());
//      
//   }

//   @Test
//   public void testMirroring() throws Exception {
//	   ConnectionFactory cfServer1 = new JmsConnectionFactory("amqp://localhost:25661");
//      ConnectionFactory cfServer0 = new JmsConnectionFactory("amqp://localhost:25660");
//	
//	  try (Connection connection = cfServer0.createConnection()) {
//	     Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//	     Queue queue = session.createQueue("exampleQueue");
//	     MessageProducer producer = session.createProducer(queue);
//	
//	     for (int i = 0; i < 100; i++) {
//	        producer.send(session.createTextMessage("Message " + i));
//	     }
//	  } catch (Exception e) {
//		  e.printStackTrace();
//	  }
//	
//	  // Every message send on server0, will be mirrored into server1
//	  try (Connection connection = cfServer1.createConnection()) {
//	     Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//	     Queue queue = session.createQueue("exampleQueue");
//	     connection.start();
//	     MessageConsumer consumer = session.createConsumer(queue);
//	     // we will consume only half of the messages on this server
//	     for (int i = 0; i < 50; i++) {
//	        TextMessage message = (TextMessage) consumer.receive(5000);
//	        System.out.println("Received Message on server1: " + message.getText());
//	        if (!message.getText().equals("Message " + i)) {
//	           // This is really not supposed to happen. We will throw an exception and in case it happens it needs to be investigated
//	           throw new IllegalStateException("Mirror Example is not working as expected");
//	        }
//	     }
//	  }
//	
//	  // mirroring of acknowledgemnts are asynchronous They are fast but still asynchronous. So lets wait some time to let the ack be up to date between the servers
//	  // a few milliseconds would do, but I'm waiting a second just in case
//	  Thread.sleep(1000);
//	
//	  // Every message send on server0, will be mirrored into server1
//	  try (Connection connection = cfServer0.createConnection()) {
//	     Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//	     Queue queue = session.createQueue("exampleQueue");
//	     connection.start();
//	     MessageConsumer consumer = session.createConsumer(queue);
//	     // we will consume only half of the messages on this server
//	     for (int i = 50; i < 100; i++) {
//	        TextMessage message = (TextMessage) consumer.receive(5000);
//	        System.out.println("Received Message on the original server0: " + message.getText());
//	        if (!message.getText().equals("Message " + i)) {
//	           // This is really not supposed to happen. We will throw an exception and in case it happens it needs to be investigated
//	           throw new IllegalStateException("Mirror Example is not working as expected");
//		            }
//		         }
//		      }
//		   }
	   }
