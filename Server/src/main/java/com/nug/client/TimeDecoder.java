package com.nug.client;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TimeDecoder extends ByteToMessageDecoder{
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
    	if (in.readableBytes() < 4){
    		return;
    	}
    	//out.add(in.readBytes(4));
    	out.add(new UnixTime(in.readUnsignedInt()));
    }
}
