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

public class DeleteTaskIntentHandler implements RequestHandler {
	private StringBuilder mStringBuilder;

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("DeleteTaskIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {

		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
		String task = requestHelper.getSlotValue("task").get();
		String list = requestHelper.getSlotValue("list").get();

		mStringBuilder = new StringBuilder();
		
		try {
			HttpResponse<String> response = new TrelloController().deleteCard(task, list, Constant.BOARD);
			new TrelloController().deleteCard(task, list, Constant.BOARD);
			if (response.getStatus() == HttpStatus.SC_OK) {
				mStringBuilder.append("Task");
				mStringBuilder.append(" ");
				mStringBuilder.append(task);
				mStringBuilder.append(" ");
				mStringBuilder.append("wurde von ");
				mStringBuilder.append(list);
				mStringBuilder.append(" gelöscht.");
			} else {
				mStringBuilder.append("Task");
				mStringBuilder.append(" ");
				mStringBuilder.append(task);
				mStringBuilder.append(" ");
				mStringBuilder.append("könnte nicht gelöscht werden");
			}

		} catch (Exception e) {
			mStringBuilder = new StringBuilder();
			mStringBuilder.append("Task");
			mStringBuilder.append(" ");
			mStringBuilder.append(task);
			mStringBuilder.append(" ");
			mStringBuilder.append("könnte nicht gelöscht werden");
		}
		return input.getResponseBuilder().withSpeech(mStringBuilder.toString()).build();
	}

}
