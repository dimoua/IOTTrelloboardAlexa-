package de.hhz.alexa.trello.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;

import de.hhz.alexa.trello.utils.Constant;
import de.hhz.alexa.trello.utils.TrelloController;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ListTasksIntentHandler implements RequestHandler {
	private StringBuilder mStringBuilder;

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("ListTasksIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		TrelloController trello = new TrelloController();
		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
		mStringBuilder = new StringBuilder();
		String trelloList = requestHelper.getSlotValue("list").get();
		Map<String, String> map = trello.listCards(Constant.BOARD, trelloList);
		mStringBuilder.append("Auf ");
		mStringBuilder.append(trelloList);
		mStringBuilder.append(" stehen: ");


		try {
			mStringBuilder.append(trello.toString(map));
		} catch (Exception e) {
			mStringBuilder.append(e.getMessage());
		}
		return input.getResponseBuilder().withSpeech(mStringBuilder.toString()).build();
	}
}
