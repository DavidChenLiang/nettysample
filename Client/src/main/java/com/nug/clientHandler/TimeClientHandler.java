package com.nug.clientHandler;

import com.nug.client.UnixTime;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	/*
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		ByteBuf m = (ByteBuf) msg; // (1)
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            m.release();
        }
	}
	*/
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		UnixTime m = (UnixTime)msg;
		System.out.println(m);
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}

}
