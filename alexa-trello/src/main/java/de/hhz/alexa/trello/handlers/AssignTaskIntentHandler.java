package de.hhz.alexa.trello.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;

import de.hhz.alexa.trello.utils.Constant;
import de.hhz.alexa.trello.utils.TrelloController;

public class AssignTaskIntentHandler implements RequestHandler {
	private StringBuilder mStringBuilder;

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("AssignTaskIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {

		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
		String username = requestHelper.getSlotValue("name").get();
		String task = requestHelper.getSlotValue("aufgabe").get();
		//String list = requestHelper.getSlotValue("list").get();

		mStringBuilder = new StringBuilder();
		mStringBuilder.append("Task");
		mStringBuilder.append(" ");
		mStringBuilder.append("Einkaufen");
		mStringBuilder.append(" ");
		mStringBuilder.append("wurde an ");
		mStringBuilder.append(username);
		mStringBuilder.append(" zugewiesen.");
		try {
			new TrelloController().assignCardToUser(username, task, Constant.BOARD, "Meine Tasks");

		} catch (Exception e) {
			mStringBuilder = new StringBuilder();
			mStringBuilder.append("Fehler Erstellung des Tasks");
		}
		return input.getResponseBuilder().withSpeech(mStringBuilder.toString()).build();
	}


}
