package com.nug.serverHandler;
import io.netty.buffer.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.*;

public class EchoServerHandlerDefinition extends ChannelInboundHandlerAdapter{
	//int counter = 0;
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
    	//String body = (String)msg;
    	//System.out.println("This is " +counter+ " times receive client: ["+body+"]");
    	//body += "$_";
    	//ByteBuf echo  = Unpooled.copiedBuffer(body.getBytes());
    	//ctx.writeAndFlush(echo);
    	System.out.println("Receiving client: ["+msg+"]");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
    	cause.printStackTrace();
    	ctx.close();
    }
}
