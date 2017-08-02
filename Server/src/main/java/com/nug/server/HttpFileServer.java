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
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.*;

public class HttpFileServer {
	
	private static final String DEFAULT_URL = "/src/main/java/com/nug/server";
	
	String url;
	int port;
	public HttpFileServer(int port,String url){
		this.url = url;
		this.port = port;
	}
    
    public static void main(String... args) throws Exception{
    	int port;
    	if (args.length > 0){
    		port = Integer.parseInt(args[0]);
    	} else {
    		port = 8080;
    	}
    	String url = DEFAULT_URL;
    	new HttpFileServer(port, url).run(port, url);
    }

	private void run(final int port, final String url) throws Exception {
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
                    ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                	ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                	ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                	ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                	ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                }
            });

			
			ChannelFuture f = b.bind("127.0.0.1",port).sync();
			System.out.println("Http File Server started on http://127.0.0.1:"+port+url);
			f.channel().closeFuture().sync();
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
