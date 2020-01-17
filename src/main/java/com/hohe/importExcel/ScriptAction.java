package com.hohe.importExcel;

import java.util.Map;

public interface ScriptAction {
	
	public void executeAction(Map params, String script) throws Exception;

}
