package de.hhz.alexa.trello.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class TrelloController {
	private Map<String, String> listCards;
	private Map<String, String> boardCards;

	private final String token = "6a29dd2e909b4be2028860a930e7105a8b0aa639ccc330ee447380f4d0e722da";
	private final String apiKey = "9d971b9633371f6a833ab87a8d588781";
	private final String baseUrl = "https://api.trello.com/1/";
	private final String APPLICATION_TYPE = "application/json";
	private final String ACCEPT = "Accept";
	Map<String, String> boards;
	Map<String, String> trelloLists;
	Map<String, String> members;

	public TrelloController() {
		this.loadBoards();
	}

	public Map<String, String> listCards(final String board, final String list) {
		String url = "lists/";
		String identifier = "/cards";
		this.loadLists(board);
		listCards = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append(this.trelloLists.get(list.toLowerCase()));
		sb.append(identifier);
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		HttpResponse<JsonNode> response = Unirest.get(sb.toString()).header(ACCEPT, APPLICATION_TYPE).asJson();
		JsonNode node = response.getBody();
		JSONArray a = node.getArray();
		for (Object o : a) {
			JSONObject jsonLineItem = (JSONObject) o;
			String name = jsonLineItem.getString("name");
			String id = jsonLineItem.getString("id");

			if (name != null) {
				listCards.put(name, id);
			}

		}
		return listCards;
	}

	public Map<String, String> listBoardCards(final String board) {
		String url = "boards/";
		String identifier = "/cards";
		this.loadLists(board);
		boardCards = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append(this.boards.get(board));
		sb.append(identifier);
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		HttpResponse<JsonNode> response = Unirest.get(sb.toString()).header(ACCEPT, APPLICATION_TYPE).asJson();
		JsonNode node = response.getBody();
		JSONArray a = node.getArray();
		for (Object o : a) {
			JSONObject jsonLineItem = (JSONObject) o;
			String name = jsonLineItem.getString("name");
			String id = jsonLineItem.getString("id");

			if (name != null) {
				boardCards.put(name, id);
			}

		}
		return listCards;
	}

	public void loadBoards() {
		boards = new HashMap<String, String>();
		String url = "members/me/boards";
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		HttpResponse<JsonNode> response = Unirest.get(sb.toString()).header(ACCEPT, APPLICATION_TYPE).asJson();
		JsonNode node = response.getBody();
		JSONArray a = node.getArray();
		for (Object o : a) {
			JSONObject jsonLineItem = (JSONObject) o;
			String name = jsonLineItem.getString("name");
			String id = jsonLineItem.getString("id");

			if ((name != null) && (id != null)) {
				boards.put(name, id);
			}
		}
	}

	public void createCard(final String name, final String list, String board) {
		String url = "cards";
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		this.loadLists(board);
		Unirest.post(sb.toString()).queryString("idList", this.trelloLists.get(list)).queryString("name", name)
				.asString();
	}

	public void moveCardToList(String cards, String toList, String board) {
		this.loadLists(board);
		this.listBoardCards(board);
		String url = "cards/";
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append(this.boardCards.get(cards));
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		Unirest.put(sb.toString()).queryString("idList", this.trelloLists.get(toList)).asString();

	}

	public void deleteCard(final String cards, final String trelloList, final String board) {
		String url = "cards/";
		this.listCards(board, trelloList);
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append(this.listCards.get(cards));
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		Unirest.delete(sb.toString()).asString();
	}

	public void assignCardToUser(String username, String card, String board, String list) {
		String url = "cards/";
		this.listMembers(board);
		this.listCards(board, list);
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append(listCards.get(card));
		sb.append("/");
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		Unirest.put(sb.toString()).queryString("idMembers", this.members.get(username))
				.asString();
	}

	public void loadLists(final String board) {
		this.trelloLists = new HashMap<String, String>();
		String url = "boards/";
		String identifier = "/lists";
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append(this.boards.get(board));
		sb.append(identifier);
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		HttpResponse<JsonNode> response = Unirest.get(sb.toString()).header(ACCEPT, APPLICATION_TYPE).asJson();
		JsonNode node = response.getBody();
		JSONArray a = node.getArray();
		for (Object o : a) {
			JSONObject jsonLineItem = (JSONObject) o;
			String name = jsonLineItem.getString("name");
			String id = jsonLineItem.getString("id");

			if (!name.equals("null")) {
				trelloLists.put(name.toLowerCase(), id);
			}

		}
	}

	public void listMembers(String board) {
		String url = "boards/";
		members = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(url);
		sb.append(this.boards.get(board));
		sb.append("/members");
		sb.append("?");
		sb.append("key=");
		sb.append(apiKey);
		sb.append("&");
		sb.append("token=");
		sb.append(token);
		HttpResponse<JsonNode> response = Unirest.get(sb.toString()).header(ACCEPT, APPLICATION_TYPE).asJson();
		JsonNode node = response.getBody();
		JSONArray a = node.getArray();
		for (Object o : a) {
			JSONObject jsonLineItem = (JSONObject) o;
			String name = jsonLineItem.getString("username");
			String id = jsonLineItem.getString("id");
			members.put(name, id);

		}

	}

	public String toString(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			sb.append(entry.getKey());
			if (iter.hasNext()) {
				sb.append(',').append(' ');
			}
		}
		return sb.toString();

	}

//	public static void main(String[] args) {
//		TrelloController trello = new TrelloController();
//		StringBuilder mStringBuilder;
//		// trello.createCard("test", "Meine Tasks","IOT");
//		// Map<String, String> map = trello.listCards("IOT", "Meine Tasks");
//		// map.forEach((key, value) -> System.out.println(key + ":" + value));
//		// System.out.println(trello.toString(trello.listCards("IOT", "Meine
//		// Tasks")));
//		// trello.moveCardToList("besuch", "fertig", "IOT");
//		// trello.deleteCard("trinken", "To Do", "IOT");
//		// trello.listMembers("IOT");
//		// 5e26d89d410d2170ad460c95
//		// trello.assignCardToUser("iotreutlingen", "trinken", "IOT", "To Do");
//	}
}
