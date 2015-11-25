package edu.usc.csci572.teamten.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SolrService {

	
	
	public String getCitySales() {
		SolrServer solr = new HttpSolrServer("http://localhost:8983/solr/memex");
		JsonArray contentArray = new JsonArray();
		SolrQuery query = new SolrQuery();
		query.setQuery("tika_location.geo_name:[* TO *]");
		query.set(GroupParams.GROUP, true);
		query.set(GroupParams.GROUP_FIELD, "tika_location.geo_name");
		
		//query.setParam("group", true);
		//query.setParam("group.field", "tika_location.geo_name");
		
		try {
			QueryResponse response = solr.query(query);
			GroupResponse groupResponse = response.getGroupResponse();
			List<Group> groups = groupResponse.getValues().get(0).getValues();
			
			if (groups == null || groups.isEmpty()){
				System.out.println("Chak Out");
				return null;
			}
			
			for(int i = 0; i < groups.size(); i++) {
				Group group = groups.get(i);
				JsonObject object = new JsonObject();
				object.addProperty("key", group.getGroupValue());
				object.addProperty("value", group.getResult().getNumFound());
				//System.out.println(group.getGroupValue());
				//System.out.println(group.getResult().getNumFound());
				contentArray.add(object);
			}
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return contentArray.toString();
	}
	
	public String getTimeBasedTrendMonthManufacturer() {
		SolrServer solr = new HttpSolrServer("http://localhost:8983/solr/memex");
		JsonArray contentArray = new JsonArray();
		SolrQuery query = new SolrQuery();
		query.setQuery("seller.memberOf.startDate:[* TO *] AND itemOffered.manufacturer:[* TO *]");
		query.setSort("itemOffered.manufacturer", ORDER.asc);
		query.setRows(10);
		
		try {
			QueryResponse response = solr.query(query);
			SolrDocumentList documents = response.getResults();
			
			if (documents == null || documents.isEmpty()){
				System.out.println("Chak Out");
				return null;
			}
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("key", documents.get(0).getFieldValue("itemOffered.manufacturer").toString());
			JsonArray values = new JsonArray();
			
			for(int i = 0; i < documents.size(); i++) {
				SolrDocument document = documents.get(i);
				
				if (i != 0 && !documents.get(i - 1).getFieldValue("itemOffered.manufacturer").toString()
						.equals(document.getFieldValue("itemOffered.manufacturer").toString())) {
					
					jsonObject.add("values", values);
					contentArray.add(jsonObject);
					jsonObject = new JsonObject();
					values = new JsonArray();
					jsonObject.addProperty("key", documents.get(i).getFieldValue("itemOffered.manufacturer").toString());
				}
				
				JsonArray temp = new JsonArray();
				temp.add(getDateValue(document.getFieldValue("seller.memberOf.startDate").toString()));
				temp.add(1);
				values.add(temp);
				
				//System.out.println(i + documents.get(i).getFieldValue("itemOffered.manufacturer").toString());
			}
			jsonObject.add("values", values);
			contentArray.add(jsonObject);
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(contentArray));
		return gson.toJson(contentArray);
	}
	
	private int getDateValue(String dateTime) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date d = sdf.parse(dateTime);
		return d.getMonth();
	}
	
}
