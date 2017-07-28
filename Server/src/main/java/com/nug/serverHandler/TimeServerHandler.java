package com.nug.serverHandler;

import com.nug.client.UnixTime;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;

public class TimeServerHandler extends ChannelInboundHandlerAdapter{
    
	@Override
    public void channelActive(final ChannelHandlerContext ctx){
    	final ByteBuf time = ctx.alloc().buffer(4); 
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
    	
    	final ChannelFuture f = ctx.writeAndFlush(time);
    	f.addListener(new ChannelFutureListener(){
    		@Override
    		public void operationComplete(ChannelFuture future){
    			assert f == future;
    			ctx.close();
    		}
    	});
    }
    
	/*
    @Override
    public void channelActive(final ChannelHandlerContext ctx){
    	ChannelFuture f = ctx.writeAndFlush(new UnixTime());
    	f.addListener(ChannelFutureListener.CLOSE);
    }
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
    	cause.printStackTrace();
    	ctx.close();
    }
}
