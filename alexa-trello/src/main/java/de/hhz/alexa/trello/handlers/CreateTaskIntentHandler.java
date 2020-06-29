package de.hhz.alexa.trello.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;

import de.hhz.alexa.trello.utils.Constant;
import de.hhz.alexa.trello.utils.TrelloController;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CreateTaskIntentHandler implements RequestHandler {
	private StringBuilder mStringBuilder;

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("CreateTaskIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {

		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
		String trelloList = requestHelper.getSlotValue("liste").get();
		String task = requestHelper.getSlotValue("task").get();

		try {
			new TrelloController().createCard(task, trelloList, Constant.BOARD);
			mStringBuilder = new StringBuilder();
			mStringBuilder.append("Task");
			mStringBuilder.append(" ");
			mStringBuilder.append(task);
			mStringBuilder.append(" ");
			mStringBuilder.append("wurde auf ");
			mStringBuilder.append(trelloList);
			mStringBuilder.append(" erstellt.");

		} catch (Exception e) {
			mStringBuilder = new StringBuilder();
			mStringBuilder.append("Fehler Erstellung des Tasks");
		}
		return input.getResponseBuilder().withSpeech(mStringBuilder.toString()).build();
	}

}
