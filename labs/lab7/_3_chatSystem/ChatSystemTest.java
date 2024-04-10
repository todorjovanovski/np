package lab7._3_chatSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class ChatRoom {
    String name;
    Set<String> users;

    public ChatRoom(String name) {
        this.name = name;
        this.users = new TreeSet<>();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean hasUser(String username) {
        return users.contains(username);
    }

    public int numUsers() {
        return users.size();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("%s\n", name));
        if(numUsers() > 0) {
            users.forEach(user -> result.append(String.format("%s\n", user)));
        } else {
            result.append("EMPTY\n");
        }
        return result.toString();
    }
}

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}

class ChatSystem {
    Map<String, ChatRoom> chatRoomsByName;
    Set<String> registeredUsers;

    public ChatSystem() {
        this.chatRoomsByName = new TreeMap<>();
        this.registeredUsers = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        chatRoomsByName.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        chatRoomsByName.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(!chatRoomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        return chatRoomsByName.get(roomName);
    }

    public void register(String username) {
        registeredUsers.add(username);
        int min = chatRoomsByName.values().stream().mapToInt(ChatRoom::numUsers).min().orElse(0);
        for(String roomName : chatRoomsByName.keySet()) {
            ChatRoom chatRoom = chatRoomsByName.get(roomName);
            if(chatRoom.numUsers() == min) {
                chatRoom.addUser(username);
                return;
            }
        }
    }

    public void registerAndJoin(String username, String roomName) {
        registeredUsers.add(username);
        chatRoomsByName.get(roomName).addUser(username);
    }

    public void joinRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!chatRoomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if(!registeredUsers.contains(username))
            throw new NoSuchUserException(username);
        chatRoomsByName.get(roomName).addUser(username);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!chatRoomsByName.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if(!registeredUsers.contains(username))
            throw new NoSuchUserException(username);
        chatRoomsByName.get(roomName).removeUser(username);
    }

    public void followFriend(String username, String friendUsername) throws NoSuchUserException {
        if(!registeredUsers.contains(friendUsername))
            throw new NoSuchUserException(friendUsername);
        for(String room : chatRoomsByName.keySet()) {
            ChatRoom chatRoom = chatRoomsByName.get(room);
            if(chatRoom.hasUser(friendUsername))
                chatRoom.addUser(username);
        }
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method[] mts = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String[] params = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}