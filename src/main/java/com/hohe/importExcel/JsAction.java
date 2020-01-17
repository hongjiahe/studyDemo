package com.hohe.importExcel;

import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JsAction implements ScriptAction {
	
	private final static String FUNCTION_STR="function callJsAction(){";
	private final static String FUNCTION_NAME="callJsAction";

	@Override
	public void executeAction(Map params,String script)
			throws Exception {
			ScriptEngineManager factory = new ScriptEngineManager(); 
			ScriptEngine engine = factory.getEngineByName ("JavaScript");
			Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
			if(params!=null && !params.isEmpty()){
				bindings.put("aopParams",params);
			}
			StringBuffer sbf=new StringBuffer();
			sbf.append("importPackage(java.util);");//加入java package
			sbf.append(FUNCTION_STR).append(script).append("}");
			engine.eval(sbf.toString(),bindings);
			if (engine instanceof Invocable) {//判断脚本引擎是否支持编译操作  
		        Invocable invocable = (Invocable)engine;//Invocable允许从java代码调用定义在脚本中的单个函数  
		        invocable.invokeFunction(FUNCTION_NAME,null);// 调用脚本中定义的顶层程序和函数  
		    }
	}

}
