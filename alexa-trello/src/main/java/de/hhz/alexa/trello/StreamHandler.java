package de.hhz.alexa.trello;
/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;

import de.hhz.alexa.trello.handlers.AssignTaskIntentHandler;
import de.hhz.alexa.trello.handlers.CancelandStopIntentHandler;
import de.hhz.alexa.trello.handlers.HelpIntentHandler;
import de.hhz.alexa.trello.handlers.LaunchRequestHandler;
import de.hhz.alexa.trello.handlers.ListTasksIntentHandler;
import de.hhz.alexa.trello.handlers.MoveTaskIntentHandler;
import de.hhz.alexa.trello.handlers.CreateTaskIntentHandler;
import de.hhz.alexa.trello.handlers.DeleteTaskIntentHandler;
import de.hhz.alexa.trello.handlers.FallbackIntentHandler;
import de.hhz.alexa.trello.handlers.SessionEndedRequestHandler;

import com.amazon.ask.SkillStreamHandler;

//mvn assembly:assembly -DdescriptorId=jar-with-dependencies package

public class StreamHandler extends SkillStreamHandler {

	@SuppressWarnings("unchecked")
	private static Skill getSkill() {
		return Skills.standard()
				.addRequestHandlers(new CreateTaskIntentHandler(), new ListTasksIntentHandler(),
						new AssignTaskIntentHandler(), new MoveTaskIntentHandler(), new CancelandStopIntentHandler(),
						new HelpIntentHandler(), new DeleteTaskIntentHandler(),new MoveTaskIntentHandler(),
						new LaunchRequestHandler(), new SessionEndedRequestHandler(), new FallbackIntentHandler())
				.build();
	}

	public StreamHandler() {
		super(getSkill());
	}

}
