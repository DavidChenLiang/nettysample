package chat;

import java.util.Arrays;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SimpleChatServer {
    private int port;
    public SimpleChatServer(int port){
    	this.port = port;
    }
	private void run () throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new SimpleChatServerInitialize())
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			System.out.println("SimpleChatServer started...");
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			System.out.println("SimpleChatServer stopped...");
		}
	}
	public static void main(String...args) throws Exception{
		List<String> list = Arrays.asList(args);
		for(String s : list){
			System.out.println(s);
		}
		int port;
		if (args.length > 0){
			port = Integer.parseInt(args[0]);
		} else{
			port = 8080;
		}
		new SimpleChatServer(port).run();
				
	}
}
