package org.usa.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import org.usa.util.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
public class UserController {

	@Resource(name = "jedisPool")
	private JedisPool jedisPool;
	
	@RequestMapping(value="/{user}/push")
	public String userPush(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("user") String user,
			@RequestParam String data) throws IOException{
		response.setCharacterEncoding("utf-8");
		Jedis jedis = jedisPool.getResource();
		try {
			
			jedis.sadd("user_list", user);
			if(data != null) {
				jedis.lpush(user, data);
				Logger.info("[PUSH]" + user + " -> " + data);
			}
			jedisPool.returnResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			jedisPool.returnBrokenResource(jedis);
		} 
		
		
		return null;
	}
	
	@RequestMapping(value="/{user}/pop")
	public String userPop(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("user") String user
			) throws IOException{
		response.setCharacterEncoding("utf-8");
		Jedis jedis = jedisPool.getResource();
		String data = null;
		try {
			data = jedis.rpop(user);
			jedis.setex("heart." + user, 60, "@");
			jedisPool.returnResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			jedisPool.returnBrokenResource(jedis);
		} 
		
		PrintWriter writer = response.getWriter();
		
		writer.write(data != null ? data : "");
		
		writer.close();
		
		return null;
	}
	
	@RequestMapping(value="/{user}/list")
	public String userList(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("user") String user
			) throws IOException{
		response.setCharacterEncoding("utf-8");
		Jedis jedis = jedisPool.getResource();
		List<String> list = null;
		try {
			list = jedis.lrange(user, 0, -1);
			
			jedisPool.returnResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			jedisPool.returnBrokenResource(jedis);
		} 
		
		PrintWriter writer = response.getWriter();
		
		writer.write(JSONArray.toJSONString(list));
		
		writer.close();
		
		return null;
	}
	
}
