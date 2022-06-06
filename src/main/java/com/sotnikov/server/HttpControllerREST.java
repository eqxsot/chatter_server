package com.sotnikov.server;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;

import java.io.*;
import java.util.logging.Handler;

@RestController
public class HttpControllerREST extends HttpServlet {
    String jsonMsgs;
    String datapath = "C:\\Users\\Second User\\IdeaProjects\\server\\src\\main\\java\\com\\sotnikov\\server\\data.json";
    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        new Thread() {
            public void run() {
                try
                {
                    File file = new File(datapath);
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    jsonMsgs = br.readLine();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }.start();
        return jsonMsgs;
    }


    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getMessages() {
        new Thread() {
            public void run() {
                try
                {
                    File file = new File(datapath);
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    jsonMsgs = br.readLine();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
        return jsonMsgs;
    }


    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void putMessage(@RequestBody Message request) {
        new Thread() {
            @SneakyThrows
            public void run() {
                try
                {
                    File file = new File(datapath);
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    jsonMsgs = br.readLine();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Gson gson = new Gson();
                MessagesJson msgs = gson.fromJson(jsonMsgs, MessagesJson.class);
                msgs.names.remove(0);
                msgs.messages.remove(0);
                if (request.name.equals(":")) {
                    msgs.names.add("\uD835\uDE3C\uD835\uDE43\uD835\uDE4A\uD835\uDE43\uD835\uDE50\uD835\uDE48:");
                }
                else {
                    msgs.names.add(request.name.replace('"', '\''));
                }
                if (request.message.equals("")) {
                    msgs.messages.add("empty");
                }
                else {
                    msgs.messages.add(request.message.replace('"', '\''));
                }

                jsonMsgs = gson.toJson(msgs);

                try
                {
                    FileWriter writer = new FileWriter(datapath, false);
                    writer.write(jsonMsgs);
                    writer.flush();
                }
                catch(IOException ex){
                    System.out.println(ex.getMessage());

                }
                catch(NullPointerException ex) {
                    FileWriter writer = new FileWriter(datapath, false);
                    writer.write("{\"messages\":[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],\"names\":[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]}");
                    writer.flush();
                }
            }
        }.start();

    }
}