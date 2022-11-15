package com.example.nettyrpc.Util;

import com.example.nettyrpc.Exception.IncorrectMagicNumberException;
import com.example.nettyrpc.net.Message;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.example.nettyrpc.net.DefaultMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

public class MessageUtil {


    public static final byte[] MAGIC_NUMBER ={1,0,2,6};
    public static final String MAGIC =new String(MAGIC_NUMBER);



    public static final int MESSAGE_PREFIX_LENGTH =16;

    public static final byte VERSION =1;

    public static final SerializableType DEFAULT_SERIALIZABLETYPE=SerializableType.JSON;

    public static final CommandType DEFAULT_COMMANDTYPE=CommandType.request;

    private static int seq=1;

    //魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
    public static Message byteToMessage(ByteBuf buf) throws IncorrectMagicNumberException {
        byte[] mn = new byte[4];
        buf.readBytes(mn);
        String magicNumber = new String(mn);
        if (!magicNumber.equals(MAGIC))
        {
            throw new IncorrectMagicNumberException();
        }

        int version = buf.readByte();
        buf.readByte();

        SerializableType serializableType = SerializableType.forInt(buf.readByte());

        CommandType commandType=CommandType.forInt(buf.readByte());

        int seq = buf.readInt();

        int len = buf.readInt();

        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        String content=new String(bytes);

        return new DefaultMessage(magicNumber, version, serializableType, commandType, len, content,seq);
    }

    //魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
    public static void messageToByteBuf(Message message, ByteBuf buffer)
    {
        buffer.writeBytes(message.getMagicNumber().getBytes(StandardCharsets.UTF_8));

        buffer.writeByte((byte)message.getVersion());
        buffer.writeByte(0);

        buffer.writeByte((byte)message.getSerializableType().getValue());

        buffer.writeByte((byte)message.getCommandType().getValue());

        buffer.writeInt(message.getSeq());

        buffer.writeInt(message.size());

        buffer.writeBytes(message.content().getBytes(StandardCharsets.UTF_8));
    }



    public static int getNextSeq()
    {
        return seq++;
    }

    public static Byte[] intToByteArray(int i)
    {
        Byte[] bytes = new Byte[4];

        bytes[3]= (byte) (i | 0xff);
        bytes[2]= (byte) (i>>8 | 0xff);
        bytes[1]= (byte) (i>>8 | 0xff);
        bytes[0]= (byte) (i>>8 | 0xff);
        return bytes;
    }



}
