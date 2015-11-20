package com.spring.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jws.WebResult;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebEndpoint;

import org.bson.BasicBSONObject;
import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.operation.bootstrap_server;
import com.mongodb.operation.register_server;
import com.spring.Entity.BootstrapMsg;
import com.spring.Entity.BootstrapRsp;
import com.spring.Entity.ObjRscDesMap;
import com.spring.Entity.RegisterRsp;




@Controller
//@RestController  ---------> this is to define the whole class to be rendered as json
public class jsonController {

	
	ArrayList<String> notify = new ArrayList<String>();
	
	@RequestMapping(value="/bootstrap",method= RequestMethod.POST)
	@ResponseBody
//	public BootstrapRsp bootstrap_request(@RequestBody BootstrapMsg evd){
	public BootstrapRsp bootstrap_request(@RequestParam(value="endpoint_client_name", defaultValue="default")String endpoint_client_name
			,HttpServletRequest request){
		System.out.println("bootstrap");
//		System.out.println(headers.toString());		
		
		ObjectMapper mapper = new ObjectMapper();
        bootstrap_server bt = new bootstrap_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(endpoint_client_name,fields);

		BootstrapRsp reply = new BootstrapRsp();
		
		
		try {
			reply = mapper.readValue(doc.toJson(), BootstrapRsp.class);
		} catch (JsonParseException e) {
			System.out.println("JsonParseException");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			System.out.println("JsonMappingException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
			e.printStackTrace();
		}
		List<String> test = reply.getRegister_server_uri();
		for(String x:test)
		{
		System.out.println(x);
		}
		reply.setBootstrap_time_stamp((new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
		System.out.println(reply.getBootstrap_time_stamp());
		bt.update(reply);
		System.out.println("***Bootstrap response messge from Boostrap server to client:  ");
		System.out.println(doc.toJson());
		return reply;
	}
	
	@RequestMapping(value="/encrypt/register",method= RequestMethod.POST)
	@ResponseBody
//	public RegisterRsp register_request(@RequestBody RegisterMsg evd){
	public RegisterRsp register_request(@RequestBody String evd,HttpServletRequest request){
		
		String client_ip = "http://"+request.getRemoteAddr()+":"+request.getRemotePort();
		
		
		RegisterRsp reply = new RegisterRsp();
		System.out.println("*****register from client******");
		System.out.println(evd);
		register_server rt = new register_server();
		
		Document myDoc = Document.parse(evd);
		String epn = myDoc.get("endpoint_client_name").toString();
		
		rt.insert(evd);
		Document ipDoc = new Document("device_ip_port",client_ip);
		rt.update(epn, ipDoc);
		reply.setEndpoint_client_name(myDoc.get("endpoint_client_name").toString());
		reply.setReturn_code(100);

		
		return reply;
	}
	
	@RequestMapping(value="/encrypt/register/deregister",method= RequestMethod.POST)
	@ResponseBody
	public String deregister(@RequestParam(value="endpoint_client_name", defaultValue="default")String endpoint_client_name){
		String reply="";
		System.out.println("****de-register from client****");
		System.out.println(endpoint_client_name);
		register_server rt = new register_server();
		Document degDoc = new Document("endpoint_client_name",endpoint_client_name);
		rt.dereg(degDoc);
		reply="100";
		return reply;
	}
	
	@RequestMapping(value="/encrypt/register/update",method= RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody String upd){
//		System.out.println("id="+evd.getId()+" product id= "+evd.getProduct_id()+ " with new seq"+evd.getNewseq()+ " locates at "+evd.getLatitude()+" - "+evd.getLongitude());

		System.out.println("*****update from client*********");
		System.out.println(upd);
		String reply = "";
		
		register_server rt = new register_server();
		
		Document myDoc = Document.parse(upd);
		String epn = myDoc.get("endpoint_client_name").toString();

		myDoc.remove("endpoint_client_name");

		//Document rsDoc = new Document("object_list",myDoc.toJson());
		rt.update(epn, myDoc);

		reply="100";		
		return reply;
	}
	
	@RequestMapping(value="/encrypt/register/singleupdate",method= RequestMethod.POST)
	@ResponseBody
	public String singleupdate(@RequestBody String upd){
		System.out.println("*****single update from client*********");
		System.out.println(upd);
		String reply = "";
		
		register_server rt = new register_server();
		
		Document myDoc = Document.parse(upd);
		String epn = myDoc.get("endpoint_client_name").toString();
		String rsc_dtl = myDoc.getString("src_detail").toString();
		String[] rsc_helper = rsc_dtl.split(":");
		String src_path = rsc_dtl.substring(0,rsc_dtl.indexOf(":"));
		String src_val = rsc_dtl.substring(rsc_dtl.indexOf(":")+1);
		System.out.println("single update for: " +rsc_dtl);
		rt.singleupdate(epn, src_path, src_val);
		
		notify.add(rsc_dtl);
		
		return "single update done";
	}
	
    
    @RequestMapping(value="/getnotify",method= RequestMethod.GET)
    @ResponseBody
    public String getnotify(){

    	ArrayList<String> tmp=new ArrayList<String>(notify); 
    	notify.clear();
    	
			ObjectMapper mapper = new ObjectMapper();
			String output=null;
			try {
				output = mapper.writeValueAsString(tmp);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}

    	
		return output;
    	
    }
	
	
    @RequestMapping(value = "/reply")
    public String getAliveResponseJSON(Model model)
    {
    	System.out.println("server");

		
		BootstrapMsg bmsg = new BootstrapMsg();
		bmsg.setEndpoint_client_name("urn:uuid:00000001-0001-0001-000000000001");
		
        return "jsonTemplate";
    }
    
    @RequestMapping(value = "/controlpanel")
    public String controlpanel()
    {
    	System.out.println("control panel test");

        return "control_panel";
    }
    
    @RequestMapping(value="/manage_device/{devid}",method= RequestMethod.GET)
    public String manage_device(@PathVariable("devid") String devid,Model model){

    	 model.addAttribute("devid", devid);
    	System.out.println("manage device test");

        return "manage_device";

    
    }
    
    @RequestMapping(value = "/getobjrsc",method= RequestMethod.GET)
    @ResponseBody
    public String getobjrsc()
    {
    	ArrayList<String> rstarray = new ArrayList<String>();
    	for (Map.Entry<String, String> entry : ObjRscDesMap.descHelper.entrySet())
   		{
    		rstarray.add(entry.getKey() + "!" + entry.getValue());
 		}
    	
		ObjectMapper mapper = new ObjectMapper();
		String output=null;
		try {
			output = mapper.writeValueAsString(rstarray);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		System.out.println(output);
        return output;
    }
    
    @RequestMapping(value = "/readclient/{devid}/{srcpath}",method= RequestMethod.GET)
    @ResponseBody
    public String readclient(@PathVariable("srcpath") String srcpath,@PathVariable("devid") String devid)
    {
    	System.out.println(devid+ " " + srcpath);
    	srcpath = srcpath.replace("*", "/");
    	
    	System.out.println(srcpath);
    	register_server bt = new register_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(devid,fields);
		Document objlst = (Document)doc.get("object_list");
		Document objistlst = (Document)objlst.get("4");
		Document objistsrclst = (Document)objistlst.get("0");
		String objistsrcval = objistsrclst.get("4").toString();
		
		System.out.println(objistsrcval);
		objistsrcval=objistsrcval+"rslst/"+srcpath;
		
		RestTemplate restTemplate = new RestTemplate();	
		String output = restTemplate.getForObject(objistsrcval, String.class);
    	return output;
    }
    
    @RequestMapping(value = "/discoverclient/{devid}/{srcpath}",method= RequestMethod.GET)
    @ResponseBody
    public String discoverclient(@PathVariable("srcpath") String srcpath,@PathVariable("devid") String devid)
    {
    	System.out.println(devid+ " " + srcpath);
    	srcpath = srcpath.replace("*", "/");
    	
    	System.out.println(srcpath);
    	register_server bt = new register_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(devid,fields);
		Document objlst = (Document)doc.get("object_list");
		Document objistlst = (Document)objlst.get("4");
		Document objistsrclst = (Document)objistlst.get("0");
		String objistsrcval = objistsrclst.get("4").toString();
		
		System.out.println(objistsrcval);
		objistsrcval=objistsrcval+"discover/"+srcpath;
		
		RestTemplate restTemplate = new RestTemplate();	
		String output = restTemplate.getForObject(objistsrcval, String.class);
    	return output;
    }
    
    @RequestMapping(value = "/writeclient/{devid}/{srcpath}/{rscval}",method= RequestMethod.GET)
    @ResponseBody
    public String writeclient(@PathVariable("srcpath") String srcpath,@PathVariable("devid") String devid,
    		@PathVariable("rscval") String rscval)
    {
    	System.out.println(devid+ " " + srcpath);
    	srcpath = srcpath.replace("*", "/");
    	
    	System.out.println(srcpath);
    	register_server bt = new register_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(devid,fields);
		Document objlst = (Document)doc.get("object_list");
		Document objistlst = (Document)objlst.get("4");
		Document objistsrclst = (Document)objistlst.get("0");
		String objistsrcval = objistsrclst.get("4").toString();
		
		System.out.println(objistsrcval);
		objistsrcval=objistsrcval+"write/"+srcpath+"/"+rscval;
		
		RestTemplate restTemplate = new RestTemplate();	
		String output = restTemplate.getForObject(objistsrcval, String.class);
    	return output;
    }
    
    @RequestMapping(value = "/writeattclient/{devid}/{attpath}/{att}/{attv}",method= RequestMethod.GET)
    @ResponseBody
    public String writeattclient(@PathVariable("attpath") String attpath,@PathVariable("devid") String devid,
    		@PathVariable("att") String att,@PathVariable("attv") String attv)
    {
    	System.out.println(devid+ " " + attpath);
    	attpath = attpath.replace("*", "/");
    	
    	System.out.println(attpath);
    	register_server bt = new register_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(devid,fields);
		Document objlst = (Document)doc.get("object_list");
		Document objistlst = (Document)objlst.get("4");
		Document objistsrclst = (Document)objistlst.get("0");
		String objistsrcval = objistsrclst.get("4").toString();
		
		System.out.println(objistsrcval);
		objistsrcval=objistsrcval+"writeattr/"+attpath+"/"+att + "/" + attv;
		
		RestTemplate restTemplate = new RestTemplate();	
		String output = restTemplate.getForObject(objistsrcval, String.class);
    	return output;
    }
    
    @RequestMapping(value = "/executeclient/{devid}/{rscpath}/{cmd}",method= RequestMethod.GET)
    @ResponseBody
    public String executeclient(@PathVariable("rscpath") String rscpath,@PathVariable("devid") String devid,
    		@PathVariable("cmd") String cmd)
    {
    	System.out.println(devid+ " " + rscpath);
    	rscpath = rscpath.replace("*", "/");
    	
    	System.out.println(rscpath);
    	register_server bt = new register_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(devid,fields);
		Document objlst = (Document)doc.get("object_list");
		Document objistlst = (Document)objlst.get("4");
		Document objistsrclst = (Document)objistlst.get("0");
		String objistsrcval = objistsrclst.get("4").toString();
		
		System.out.println(objistsrcval);
		objistsrcval=objistsrcval+"execute/"+rscpath+"/"+cmd;
		
		RestTemplate restTemplate = new RestTemplate();	
		String output = restTemplate.getForObject(objistsrcval, String.class);
    	return output;
    }
    
    @RequestMapping(value = "/createinstanceclient/{devid}/{srcpath}/{rscval}",method= RequestMethod.GET)
    @ResponseBody
    public String createinstanceclient(@PathVariable("srcpath") String srcpath,@PathVariable("devid") String devid,
    		@PathVariable("rscval") String rscval)
    {
    	System.out.println(devid+ " " + srcpath);
    	srcpath = srcpath.replace("*", "/");
    	
    	System.out.println(srcpath);
    	register_server bt = new register_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(devid,fields);
		Document objlst = (Document)doc.get("object_list");
		Document objistlst = (Document)objlst.get("4");
		Document objistsrclst = (Document)objistlst.get("0");
		String objistsrcval = objistsrclst.get("4").toString();
		
		System.out.println(objistsrcval);
		objistsrcval=objistsrcval+"createinstance/"+srcpath+"/"+rscval;
		
		RestTemplate restTemplate = new RestTemplate();	
		String output = restTemplate.getForObject(objistsrcval, String.class);
    	return output;
    }
    
    
    @RequestMapping(value = "/deleteinstanceclient/{devid}/{srcpath}",method= RequestMethod.GET)
    @ResponseBody
    public String deleteinstanceclient(@PathVariable("srcpath") String srcpath,@PathVariable("devid") String devid)
    {
    	System.out.println(devid+ " " + srcpath);
    	srcpath = srcpath.replace("*", "/");
    	
    	System.out.println(srcpath);
    	register_server bt = new register_server();
		
        BasicDBObject fields = new BasicDBObject().append("_id", 0);
        
		Document doc = bt.find(devid,fields);
		Document objlst = (Document)doc.get("object_list");
		Document objistlst = (Document)objlst.get("4");
		Document objistsrclst = (Document)objistlst.get("0");
		String objistsrcval = objistsrclst.get("4").toString();
		
		System.out.println(objistsrcval);
		objistsrcval=objistsrcval+"deleteinst/"+srcpath;
		
		RestTemplate restTemplate = new RestTemplate();	
		String output = restTemplate.getForObject(objistsrcval, String.class);
    	return output;
    }
    
}
