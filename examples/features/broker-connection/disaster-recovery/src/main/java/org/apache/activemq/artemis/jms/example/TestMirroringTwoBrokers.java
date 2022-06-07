/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.jms.example;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.qpid.jms.JmsConnectionFactory;

/**
 * On this example, two brokers are mirrored.
 * Everything that is happening on the first broker will be mirrored on the second, and Vice Versa.
 */
public class TestMirroringTwoBrokers {

   public static void main(final String[] args) throws Exception {
      ConnectionFactory cfServer0 = new JmsConnectionFactory("amqp://localhost:5660");
      ConnectionFactory cfServer1 = new JmsConnectionFactory("amqp://localhost:5661");

      //Send on Server 0 and Consume from Serve1
      try (Connection connection = cfServer0.createConnection()) {
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Queue queue = session.createQueue("exampleQueue");
         MessageProducer producer = session.createProducer(queue);

         for (int i = 0; i < 2; i++) {
        	 System.out.println("Sending message to Server 0: " + i);
            producer.send(session.createTextMessage("Message " + i));
         }
      } catch (Exception e) {
    	  e.printStackTrace();
	  }

      // Every message send on server0, will be mirrored into server1
      try (Connection connection = cfServer1.createConnection()) {
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Queue queue = session.createQueue("exampleQueue");
         connection.start();
         MessageConsumer consumer = session.createConsumer(queue);
         for (int i = 0; i < 2; i++) {
            TextMessage message = (TextMessage) consumer.receive(5000);
            System.out.println("Received Message on server1: " + message.getText());
         }
      } catch (Exception e) {
    	  e.printStackTrace();
	  }
      
      //Send on Server 1 and Consume from Serve 0
      try (Connection connection = cfServer1.createConnection()) {
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Queue queue = session.createQueue("exampleQueue");
         MessageProducer producer = session.createProducer(queue);

         for (int i = 0; i < 2; i++) {
        	 System.out.println("Sending message to Server 1: " + i);
            producer.send(session.createTextMessage("Message " + i));
         }
      } catch (Exception e) {
    	  e.printStackTrace();
	  }

      // Every message send on server0, will be mirrored into server1
      try (Connection connection = cfServer0.createConnection()) {
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Queue queue = session.createQueue("exampleQueue");
         connection.start();
         MessageConsumer consumer = session.createConsumer(queue);
         for (int i = 0; i < 2; i++) {
            TextMessage message = (TextMessage) consumer.receive(5000);
            System.out.println("Received Message on server0: " + message.getText());
         }
      } catch (Exception e) {
    	  e.printStackTrace();
	  }
   }
}
