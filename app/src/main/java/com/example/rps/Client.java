package com.example.rps;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends AsyncTask<JSONObject, Void, JSONObject>
{
    private final static String IP_ADDRESS = " 192.168.0.106";
    private final static int PORT = 15321;
    private final static int SIZE = 1024;

    private JSONObject received;
    private JSONObject toSend;
    private Socket socket;
    private InputStreamReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;

    public Client(JSONObject object)
    {
        this.toSend = object;
    }

    private void send()
    {
        String data = this.toSend.toString();
        try
        {
            this.outputStreamWriter.write(data);
            this.outputStreamWriter.flush();
            System.out.println("Successfully sent");
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    private void receive()
    {
        try
        {
            char[] charBuffer = new char[SIZE];
            StringBuilder stringBuilder = new StringBuilder();
            this.inputStreamReader.read(charBuffer);
            stringBuilder.append(charBuffer);
            this.inputStreamReader.close();
            this.received = new JSONObject(stringBuilder.toString());
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects)
    {
        try
        {
            this.socket = new Socket(IP_ADDRESS, PORT);
            this.inputStreamReader = new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8);
            this.outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8);
            send();
            receive();
            this.socket.close();
            return this.received;
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
        }
        return null;
    }
}
