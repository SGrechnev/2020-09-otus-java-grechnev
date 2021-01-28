package ru.otus;

import com.google.gson.Gson;
import ru.otus.gson.MyGson;
import ru.otus.model.AnyObject;
import ru.otus.model.Node;

import java.util.ArrayList;
import java.util.List;

public class GsonDemo {
    public static void main(String[] args) {
        // Init serializable objects
        Node child = new Node("child", null);
        Node parent = new Node("parent", child);
        Node grandpa = new Node("grandpa", parent);

        AnyObject obj = new AnyObject().toBuilder()
                .fieldByte((byte) 123)
                .fieldInt(1337)
                .fieldShort((short) -1)
                .fieldLong(9_223_300_000_000_000_000L)
                .fieldBoolean(true)
                .fieldFloat(13.37f)
                .fieldDouble(1.337)
                .fieldString("leet")
                .fieldIntArray(new int[]{1, 3, 3, 7})
                .fieldStringArray(new String[]{"s1", "s2", "s3"})
                .fieldCollection(new ArrayList<>(List.of(1, 3, 3, 7)))
                .build();

        // Gson work
        Gson gson = new Gson();
        String gsonNull = gson.toJson(null);
        System.out.println("Gson(null):   " + gsonNull);

        String gsonGrandpa = gson.toJson(grandpa/*, Node.class*/);
        System.out.println("Gson(Node):   " + gsonGrandpa);

        String gsonObj = gson.toJson(obj/*, AnyObject.class*/);
        System.out.println("Gson(AnyObject):   " + gsonObj);
        System.out.println("-----------------------------------------------------------------------------------------");

        // MyGson work
        MyGson myGson = new MyGson();
        String myGsonNull = myGson.toJson(null);
        System.out.println("MyGson(null): " + myGsonNull);

        String myGsonGrandpa = myGson.toJson(grandpa);
        System.out.println("MyGson(Node): " + myGsonGrandpa);

        String myGsonObj = myGson.toJson(obj);
        System.out.println("MyGson(AnyObject): " + myGsonObj);
        System.out.println("-----------------------------------------------------------------------------------------");

        Object mustBeNull = gson.fromJson(myGsonNull, Object.class);
        Node mustBeGrandpa = gson.fromJson(myGsonGrandpa, Node.class);
        AnyObject mustBeObj = gson.fromJson(myGsonObj, AnyObject.class);
        System.out.printf("Gson.fromJson(MyGson.toJson(null))    %s null%n", mustBeNull == null ? "==" : "!=");
        System.out.printf("Gson.fromJson(MyGson.toJson(grandpa)) %s grandpa%n", grandpa.equals(mustBeGrandpa) ? "==" : "!=");
        System.out.printf("Gson.fromJson(MyGson.toJson(obj))     %s obj%n", obj.equals(mustBeObj) ? "==" : "!=");
    }
}