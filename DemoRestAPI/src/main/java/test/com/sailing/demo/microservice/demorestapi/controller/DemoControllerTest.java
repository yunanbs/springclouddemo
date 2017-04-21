package test.com.sailing.demo.microservice.demorestapi.controller; 

import com.sailing.demo.microservice.demorestapi.controller.DemoController;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** 
* DemoController Tester. 
* 
* @author <Authors name> 
* @since <pre>2017-04-21</pre>
* @version 1.0 
*/ 
public class DemoControllerTest { 

    private MockMvc mockMvc;
@Before
public void before() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(new DemoController()).build();
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: helloWorld() 
* 
*/ 
@Test
public void testHelloWorld() throws Exception { 
//TODO: Test goes here...
    mockMvc.perform(MockMvcRequestBuilders.post("/helloworld"))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
} 

/** 
* 
* Method: add(@RequestBody int x, @RequestBody int y) 
* 
*/ 
@Test
public void testAdd() throws Exception { 
//TODO: Test goes here...

    JSONObject tmp = new JSONObject();
    tmp.put("x",1);
    tmp.put("y",2);

    mockMvc.perform(MockMvcRequestBuilders.post("/add")
            .contentType("application/json")
            .content("x=1,y=2"))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
} 


} 
