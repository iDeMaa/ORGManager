package admin;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class HttpAdapter {

	//private final String UPKey;
	//private final String UPPrivate;
	private HttpResponse<JsonNode> response;
	
	public HttpAdapter(/*String UPKey, String UPPrivate*/) {
		//this.UPKey = UPKey;
		//this.UPPrivate = UPPrivate;
	}

	private String requestUserId(String discordId) {
		String url = "https://unplayer.com/api/1.0/user?type=discord&user_id=" + discordId;
		try {
			response = Unirest.get(url).asJson();
			return response.getBody().getObject().getJSONObject("ids").get("gtav").toString();
		} catch (UnirestException e) {
			System.out.println("Hubo un error al obtener el usuario");
		}
		return null;
	}
	
	public String requestUserName(String discordId) {
		String userId = this.requestUserId(discordId);
		String url = "https://unplayer.com/api/1.0/gtav/user/" + userId;
		try {
			response = Unirest.get(url).asJson();
		} catch (UnirestException e) {
			System.out.println("Error al obtener el nombre del usuario");
		}
		
		if(response != null) {
			return response.getBody().getObject().getString("name");
		} else {
			return null;
		}
	}
	
	public int getMemberRank(String discordId, int orgId) {
		String userId = this.requestUserId(discordId);
		String url = "https://unplayer.com/api/1.0/gtav/user/" + userId;
		try {
			response = Unirest.get(url).asJson();
			JSONArray array = response.getBody().getObject().getJSONArray("orgs");
			for(int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String id = object.get("org_id").toString();
				if(Integer.parseInt(id) == orgId) {
					return object.getInt("idx");
				}
			}
		} catch (UnirestException e) {
			System.out.println("Error al comprobar las ORG del usuario");
		}
		return -1;
	}
	
	public JSONArray requestORGRanks(int orgId) {
		String url = "https://unplayer.com/api/1.0/gtav/org/" + orgId;
		try {
			response = Unirest.get(url).asJson();
			return response.getBody().getObject().getJSONArray("ranks");
		} catch(UnirestException e) {
			System.out.println("Error al obtener los rangos de la ORG");
		}
		return null;
	}
	
	public String requestORGRankName(int orgId, int orgRankIndex) {
		String url = "https://unplayer.com/api/1.0/gtav/org/" + orgId;
		try {
			response = Unirest.get(url).asJson();
			return ((JSONObject)response.getBody().getObject().getJSONArray("ranks").get(orgRankIndex)).getString("name");
		} catch(UnirestException e) {
			System.out.println("Error al obtener el nombre de la ORG");
		}
		return null;
	}
	
	public String requestORGName(int orgId) {
		String url = "https://unplayer.com/api/1.0/gtav/org/" + orgId;
		try {
			response = Unirest.get(url).asJson();
			return response.getBody().getObject().getString("name");
		} catch(UnirestException e) {
			System.out.println("Error al obtener el nombre de la ORG");
		}
		return null;
	}
	
	public String requestUserId(String discordId, String service) {
		String url = "https://unplayer.com/api/1.0/user?type=discord&user_id=" + discordId;
		try {
			return Unirest.get(url).asJson().getBody().getObject().getJSONObject("ids").get(service).toString();
		} catch (UnirestException e) {
			System.out.println("Hubo un error al obtener el ID del usuario");
		}
		return null;
	}
	
	public JSONObject requestUserData(String discordId) {
		String url = "https://unplayer.com/api/1.0/gtav/user/" + this.requestUserId(discordId);
		try {
			response = Unirest.get(url).asJson();
			return response.getBody().getObject();
		} catch(UnirestException e) {
			System.out.println("Hubo un error al obtener la informaci\u00F3n del usuario");
		}
		return null;
	}
	
}
