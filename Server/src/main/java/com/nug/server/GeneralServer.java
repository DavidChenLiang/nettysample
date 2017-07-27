package com.nug.server;

import com.nug.serverHandler.*;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class GeneralServer {
    private int port;
    public GeneralServer(int port){
    	this.port = port;
    }
    
    public static void main(String... args) throws Exception{
    	int port;
    	if (args.length > 0){
    		port = Integer.parseInt(args[0]);
    	} else {
    		port = 8080;
    	}
    	new GeneralServer(port).run();
    }

	private void run() throws Exception {
		// TODO Auto-generated method stub
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    //ch.pipeline().addLast(new DiscardServerHandler());
                	//ch.pipeline().addLast(new EchoServerHandler());
                	ch.pipeline().addLast(new TimeServerHandler());
                }
            })
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
