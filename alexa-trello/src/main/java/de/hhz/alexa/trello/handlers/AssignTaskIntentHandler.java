package de.hhz.alexa.trello.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;

import de.hhz.alexa.trello.utils.Constant;
import de.hhz.alexa.trello.utils.TrelloController;
import kong.unirest.HttpResponse;
import unirest.shaded.org.apache.http.HttpStatus;

public class AssignTaskIntentHandler implements RequestHandler {
	private StringBuilder mStringBuilder;

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("AssignTaskIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {

		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
		String username = requestHelper.getSlotValue("name").get();
		String task = requestHelper.getSlotValue("task").get();
		String list = requestHelper.getSlotValue("list").get();

		
		try {
			HttpResponse<String> response = new TrelloController().assignCardToUser(username, task, Constant.BOARD, list);
		    if(response.getStatus()== HttpStatus.SC_ACCEPTED) {
		    	mStringBuilder = new StringBuilder();
				mStringBuilder.append("Task");
				mStringBuilder.append(" ");
				mStringBuilder.append(task);
				mStringBuilder.append(" ");
				mStringBuilder.append("wurde an ");
				mStringBuilder.append(username);
				mStringBuilder.append(" zugewiesen.");
		    } else {
				mStringBuilder.append("Fehler bei der Zuweisung des Tasks");
		    }
		} catch (Exception e) {
			mStringBuilder.append("Fehler bei der Zuweisung des Tasks");
		}
		return input.getResponseBuilder().withSpeech(mStringBuilder.toString()).withReprompt(Constant.REPROMT).build();
	}


}
