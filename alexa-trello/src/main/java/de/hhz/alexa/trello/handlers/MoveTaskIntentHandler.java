package de.hhz.alexa.trello.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;

import de.hhz.alexa.trello.utils.Constant;
import de.hhz.alexa.trello.utils.TrelloController;
import kong.unirest.HttpResponse;
import unirest.shaded.org.apache.http.HttpStatus;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MoveTaskIntentHandler implements RequestHandler {
	private StringBuilder mStringBuilder;

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("MoveTaskIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {

		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
		String toList = requestHelper.getSlotValue("toList").get();
		String task = requestHelper.getSlotValue("task").get();

		
		try {
			HttpResponse<String> response = new TrelloController().moveCardToList(task,toList, Constant.BOARD);
			if (response.getStatus() == HttpStatus.SC_ACCEPTED) {
				mStringBuilder = new StringBuilder();
				mStringBuilder.append("Task");
				mStringBuilder.append(" ");
				mStringBuilder.append(task);
				mStringBuilder.append(" ");
				mStringBuilder.append("wurde auf  ");
				mStringBuilder.append(toList);
				mStringBuilder.append(" gesetzt.");
			} else {
				mStringBuilder = new StringBuilder();
				mStringBuilder.append("Task");
				mStringBuilder.append(" ");
				mStringBuilder.append(task);
				mStringBuilder.append(" ");
				mStringBuilder.append("könnte nicht geschoben werden.");
			}


		} catch (Exception e) {
			mStringBuilder = new StringBuilder();
			mStringBuilder.append("Ein Fehler is bei verschiebung des Task");
			mStringBuilder.append(task);
			mStringBuilder.append(" aufgetretten");

		}
		return input.getResponseBuilder().withSpeech(mStringBuilder.toString()).withReprompt(Constant.REPROMT).build();
	}

}
