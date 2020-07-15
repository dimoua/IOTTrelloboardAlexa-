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

public class CreateTaskIntentHandler implements RequestHandler {
	private StringBuilder mStringBuilder;

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("CreateTaskIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {

		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
		String trelloList = requestHelper.getSlotValue("list").get();
		String task = requestHelper.getSlotValue("task").get();

		try {
			HttpResponse<String> response = new TrelloController().createCard(task, trelloList, Constant.BOARD);
			if (response.getStatus() == HttpStatus.SC_OK) {
				mStringBuilder = new StringBuilder();
				mStringBuilder.append("Task");
				mStringBuilder.append(" ");
				mStringBuilder.append(task);
				mStringBuilder.append(" ");
				mStringBuilder.append("wurde auf ");
				mStringBuilder.append(trelloList);
				mStringBuilder.append(" erstellt.");

			} else {
				mStringBuilder.append("Task ");
				mStringBuilder.append(task);
				mStringBuilder.append(" könnte nicht erstellt werden");
			}
		} catch (Exception e) {
			mStringBuilder.append("Task ");
			mStringBuilder.append(task);
			mStringBuilder.append(" könnte nicht erstellt werden");
		}
		return input.getResponseBuilder().withSpeech(mStringBuilder.toString()).build();
	}

}
