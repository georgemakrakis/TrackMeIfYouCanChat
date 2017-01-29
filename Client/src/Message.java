import java.io.Serializable;
import java.util.Date;


public class Message implements Serializable
{
    private String nickname;
    private String i2p_dest;
    private boolean HB;
    private Date d;
    private String chatid;
    private String msg;
    private boolean new_chat;
    
    Message()
    {
        this.new_chat=false;
    }
    Message (String chatid,String msg,String name)
    {
        this.chatid=chatid;
        this.msg=msg;
        this.nickname=name;
        this.new_chat=false;
    }
    Message (String chatid,String msg,String name,String destination)
    {
        this.chatid=chatid;
        this.msg=msg;
        this.nickname=name;
        this.i2p_dest=destination;
        this.new_chat=false;
    }
    Message (String chatid,String msg,String name,String destination,boolean newchat)
    {
        this.chatid=chatid;
        this.msg=msg;
        this.nickname=name;
        this.i2p_dest=destination;
        this.new_chat=newchat;
    }
    Message(String nickname,String i2p_dest)
    {
        this.nickname=nickname;
        this.i2p_dest=i2p_dest;
        this.HB=false;
        this.new_chat=false;
    }
    Message(String i2p_dest)
    {
        this.HB=true;
        this.i2p_dest=i2p_dest;
        this.new_chat=false;
    }
    public String get_nickname()
    {
        return nickname;
    }
    public String get_i2p_dest()
    {
        return i2p_dest;
    }
    public boolean get_HB()
    {
        return HB;
    }
    public Date get_d()
    {
        return d;
    }
    public void set_d(Date d)
    {
        this.d=d;
    }
    public String get_chatid()
    {
        return chatid;
    }
    public void set_chatid(String id)
    {
        this.chatid=id;
    }
    public String get_msg()
    {
        return msg;
    }
    public void set_msg(String msg)
    {
        this.msg=msg;
    }
    public void set_name(String name)
    {
        this.nickname=name;
    }
    public void set_i2p_des(String dest)
    {
        this.i2p_dest=dest;
    }
    public void set_newchat()
    {
        this.new_chat=true;
    }
    
    public boolean get_newchat()
    {
        return new_chat;
    }
    
    public void set_HB()
    {
        this.HB=true;
    }
}