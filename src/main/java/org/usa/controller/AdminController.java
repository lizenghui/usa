package org.usa.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.usa.util.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
public class AdminController {

	@Resource(name = "jedisPool")
	private JedisPool jedisPool;
	
	@RequestMapping(value="/admin/get_user_list")
	public String getUserList(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		Jedis jedis = jedisPool.getResource();
		try {
			
			Set<String> set = jedis.smembers("user_list");
			Set<String> online = jedis.keys("heart.*");
			
			JSONArray jsonArray = new JSONArray();
			
			for(String user : set) {
				JSONObject obj = new JSONObject();
				obj.put("user", user);
				obj.put("online", online.contains("heart." + user));
				jsonArray.add(obj);
			}
			
			PrintWriter writer = response.getWriter();
			writer.write(jsonArray.toJSONString());
			writer.close();
			
			jedisPool.returnResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			jedisPool.returnBrokenResource(jedis);
		} 
		
		
		return null;
	}
		
}
