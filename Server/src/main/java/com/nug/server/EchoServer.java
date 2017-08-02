package com.nug.server;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.buffer.*;
import io.netty.handler.logging.*;
import io.netty.channel.socket.nio.*;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.*;

import com.nug.serverHandler.*;

public class EchoServer{
	public static void main(String[] args) throws Exception{
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		new EchoServer().bind(port);
	}
	
	public void bind(int port) throws Exception{
		EventLoopGroup  bossGroup = new NioEventLoopGroup();
		EventLoopGroup  workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				public void initChannel(SocketChannel ch) throws Exception{
					//ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
					//ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
					ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new EchoServerHandlerDefinition());
				}
			});
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
				
		}finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}