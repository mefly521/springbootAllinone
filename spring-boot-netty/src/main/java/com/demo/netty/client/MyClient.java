package com.demo.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
	public static void main(String[] args){
		EventLoopGroup  eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new MyClientInitailizer());
			ChannelFuture channelFuture = bootstrap.connect("localhost",8899).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e){
			e.printStackTrace();
		}
		finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}