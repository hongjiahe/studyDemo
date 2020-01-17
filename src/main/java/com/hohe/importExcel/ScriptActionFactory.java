package com.hohe.importExcel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ScriptActionFactory {
	
	private static ScriptAction action=null;
	
	public static Properties p=new Properties();
	static{
		InputStream input=ScriptActionFactory.class.getClassLoader().getResourceAsStream("config.properties");
		try{
			p.load(input);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static ScriptAction getDefaultAction() throws Exception{
		if(action==null){
			String cname=p.getProperty("script.action.impl");
			return (ScriptAction)Class.forName(cname).newInstance();
		}
		return action;
	}

}
